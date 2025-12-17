package org.exodusstudio.frostbite.common.entity.custom.monk;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Dynamic;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.MemoryModuleTypeRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;
import org.exodusstudio.frostbite.common.registry.SoundRegistry;
import org.exodusstudio.frostbite.common.util.Util;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.List;

import static net.minecraft.world.level.block.EnchantingTableBlock.BOOKSHELF_OFFSETS;
import static org.exodusstudio.frostbite.common.util.Util.calculateDir;

public class MonkEntity extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_CLAPPING =
            SynchedEntityData.defineId(MonkEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ILLUSION =
            SynchedEntityData.defineId(MonkEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DATA_LAST_CONFUSION =
            SynchedEntityData.defineId(MonkEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> DATA_SWIRL_LENGTH =
            SynchedEntityData.defineId(MonkEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Vector3fc> DATA_SWIRL_DIR =
            SynchedEntityData.defineId(MonkEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Vector3fc> DATA_ARENA =
            SynchedEntityData.defineId(MonkEntity.class, EntityDataSerializers.VECTOR3);
    private static final Component MONK_NAME_COMPONENT = Component.translatable("entity.monk.boss_bar");
    private final ServerBossEvent bossEvent = (ServerBossEvent)
            new ServerBossEvent(MONK_NAME_COMPONENT, BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);
    public final AnimationState clapAnimationState = new AnimationState();
    public static final int TP_DIAMETER = 15;
    public static final int ILLUSION_AMOUNT = 10;
    public static final int ATTACK_COOLDOWN = 60;
    public static final int REPEL_RANGE = 6;
    public static final int ARENA_SIZE = 20;

    public MonkEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.MONK.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("illusion", isIllusion());
        output.putFloat("arenaX", getArenaCenter().x());
        output.putFloat("arenaY", getArenaCenter().y());
        output.putFloat("arenaZ", getArenaCenter().z());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setIllusion(input.getBooleanOr("illusion", false));
        if (input.getFloatOr("arenaX", 0.0F) != 0.0F) {
            setArenaCenter(new Vector3f(
                    input.getFloatOr("arenaX", 0f),
                    input.getFloatOr("arenaY", 0f),
                    input.getFloatOr("arenaZ", 0f)));
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_CLAPPING, false);
        builder.define(DATA_ILLUSION, false);
        builder.define(DATA_LAST_CONFUSION, "");
        builder.define(DATA_SWIRL_LENGTH, 0f);
        builder.define(DATA_SWIRL_DIR, new Vector3f(0, 0, 0));
        builder.define(DATA_ARENA, new Vector3f(0, 0, 0));
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        super.customServerAiStep(serverLevel);
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        MonkAI.updateActivity(this);
        ((Brain<MonkEntity>) getBrain()).tick(serverLevel, this);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        if (!isIllusion()) {
            this.bossEvent.addPlayer(serverPlayer);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        if (!isIllusion()) {
            this.bossEvent.removePlayer(serverPlayer);
        }
    }

    @Override
    protected @NotNull Brain<?> makeBrain(Dynamic<?> dynamic) {
        return MonkAI.makeBrain(this, dynamic);
    }

    @Contract(value = "null->false")
    public boolean canTargetEntity(@Nullable Entity entity) {
        if (!(entity instanceof Player player) ||
                this.level() != entity.level() ||
                !EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) ||
                this.isAlliedTo(entity) ||
                player.isInvulnerable() || player.isDeadOrDying()) {
            return false;
        }
        return this.level().getWorldBorder().isWithinBounds(player.getBoundingBox());
    }

    @Override
    public void tick() {
        super.tick();
        if (!isDeadOrDying() && getBrain().getMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get()).isPresent()) {
            this.getBrain().setMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), getBrain().getMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get()).get() - 1);
            if (getBrain().getMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get()).get() < -10) {
                getBrain().setMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), ATTACK_COOLDOWN);
            }

            if (getBrain().getMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get()).get() == 30 && level() instanceof ServerLevel serverLevel && isClapping()) {
                for (int i = 0; i < 5; i++) {
                    for (BlockPos blockpos : BOOKSHELF_OFFSETS) {
                        serverLevel.sendParticles(
                                ParticleTypes.ENCHANT,
                                getX() + (0.5 - level().random.nextFloat()) * 0.5,
                                getEyeY() + (0.5 - level().random.nextFloat()) * 0.5 + 1,
                                getZ() + (0.5 - level().random.nextFloat()) * 0.5,
                                0,
                                blockpos.getX() + level().random.nextFloat() - 0.5,
                                blockpos.getY() - level().random.nextFloat() - 1.0F,
                                blockpos.getZ() + level().random.nextFloat() - 0.5,
                                1
                        );
                    }
                }
            }
        }


        if (getAttackableFromBrain() != null && isClapping() && clapAnimationState.getTimeInMillis(tickCount) / 50 == 9) {
            level().playSound(null, this.getOnPos(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.HOSTILE,
                    1f, level().getRandom().nextFloat() * 0.1F + 0.9F);
            undoLastConfusionAttack();

            String attack = chooseAttack(getAttackableFromBrain());
            switch (attack) {
                case "confuse" -> confuseAttack(getAttackableFromBrain());
                case "clone" -> cloneAttack();
                case "repel" -> repelAttack();
                case "tp" -> tpRandomly(level());
                case "swirl" -> swirlAttack(getAttackableFromBrain());
            }
            setClapping(false);
        }

        if (getSwirlLength() >= 0.1 && getSwirlLength() < 10f) {
            setSwirlLength(getSwirlLength() + 0.3f);
            doSwirl();
        } else {
            setSwirlLength(0f);
        }
    }

    @Nullable
    protected LivingEntity getAttackableFromBrain() {
        return this.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE).orElse(null);
    }

    public String chooseAttack(LivingEntity target) {
        if (isIllusion()) return "tp";

        if (getRandom().nextFloat() < 0.2) {
            return "confuse";
        } else if (getRandom().nextFloat() < 0.2) {
            return "clone";
        } else if (getRandom().nextFloat() < 0.2 && target.distanceTo(this) < REPEL_RANGE - 2) {
            return "repel";
        } else {
            return "swirl";
        }
    }

    public void confuseAttack(LivingEntity target) {
        if (getRandom().nextFloat() < 0.3) {
            invertControls();
            setLastConfusionAttack("invert_controls");
        } else if (getRandom().nextFloat() < 0.3) {
            swapPlaces(target);
        } else {
            invertCamera();
            setLastConfusionAttack("invert_camera");
        }
    }

    public void invertControls() {
        Options options = Minecraft.getInstance().options;
        InputConstants.Key keyLeft = options.keyLeft.getKey();
        options.keyLeft.setKey(options.keyRight.getKey());
        options.keyRight.setKey(keyLeft);
        InputConstants.Key keyUp = options.keyUp.getKey();
        options.keyUp.setKey(options.keyDown.getKey());
        options.keyDown.setKey(keyUp);

        options.keyLeft.setDown(false);
        options.keyRight.setDown(false);
        options.keyUp.setDown(false);
        options.keyDown.setDown(false);

        KeyMapping.resetMapping();
    }

    public void invertCamera() {
        Options options = Minecraft.getInstance().options;
        options.invertMouseY().set(!options.invertMouseY().get());
    }

    public void swapPlaces(LivingEntity target) {
        double monkPosX = this.position().x;
        double monkPosY = this.position().y;
        double monkPosZ = this.position().z;
        Vec3 targetPos = target.position();
        this.setPos(targetPos.x, targetPos.y, targetPos.z);
        target.teleportTo(monkPosX, monkPosY, monkPosZ);
    }

    public void cloneAttack() {
        tpRandomly(level());
        if (level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < ILLUSION_AMOUNT; i++) {
                MonkEntity monk = new MonkEntity(null, serverLevel);
                monk.setPos(this.blockPosition().getX(), this.blockPosition().getY(), this.blockPosition().getZ());
                monk.setIllusion(true);
                monk.tpRandomly(serverLevel);
                serverLevel.addFreshEntityWithPassengers(monk);
                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, this.blockPosition(), GameEvent.Context.of(this));
            }
        }
    }

    public void repelAttack() {
        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class,
                new AABB(new Vec3(getBlockX() - REPEL_RANGE, getBlockY() - REPEL_RANGE, getBlockZ() - REPEL_RANGE),
                        new Vec3(getBlockX() + REPEL_RANGE, getBlockY() + REPEL_RANGE, getBlockZ() + REPEL_RANGE)));

        if (!entities.isEmpty()) {
            for (LivingEntity entity : entities) {
                if (!entity.is(this)) {
                    float num = 10f / (distanceTo(entity) + 3f);
                    Vec3 mul = new Vec3(num, num * 0.5, num);
                    Vec3 v = calculateDir(this, entity, mul);
                    if (entity instanceof Player) {
                        Minecraft.getInstance().player.push(v); // no idea why this works, but it does
                        continue;
                    }
                    entity.push(v);
                }
            }
        }

        level().playSound(null, blockPosition(), SoundRegistry.STUNNING_BELL_RING.get(), SoundSource.PLAYERS, 1f, 1f);
    }

    public void swirlAttack(LivingEntity target) {
        setSwirlLength(0.1f);
        setSwirlDir(target.position().subtract(position()).normalize().toVector3f());
    }

    public void doSwirl() {
        LivingEntity target = getAttackableFromBrain();
        if (target == null) return;
        Vec3 vec3 = position().add(0, getEyeHeight(), 0);
        Vec3 vec32 = new Vec3(getSwirlDir());

        Vec3 vec33 = vec3.add(vec32.scale(getSwirlLength()));

        Vector3f add = vec32.toVector3f()
                .rotate(Util.getRotationQuaternionAroundVector(getSwirlLength() * 2, vec32)).mul(0.75f);

        Vec3 vec34 = vec33.add(
                add.x,
                add.y,
                add.z);
        ((ServerLevel) level()).sendParticles(ParticleRegistry.SHOCKWAVE_PARTICLE.get(),
                vec34.x, vec34.y, vec34.z,
                0,
                0,
                0,
                0,
                1);

        if (vec34.distanceTo(target.position().add(0, target.getEyeHeight(), 0)) < 2.5 && level() instanceof ServerLevel serverLevel) {
            serverLevel.explode(this, vec34.x, vec34.y, vec34.z, 3f, false, Level.ExplosionInteraction.NONE);
            setSwirlLength(0);
        }
    }

    public void undoLastConfusionAttack() {
        String lastAttack = getLastConfusionAttack();
        if (lastAttack.isEmpty()) return;
        switch (lastAttack) {
            case "invert_controls" -> invertControls();
            case "invert_camera" -> invertCamera();
        }
        setLastConfusionAttack("");
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_CLAPPING.equals(accessor)) {
            this.clapAnimationState.stop();
            if (isClapping()) {
                this.clapAnimationState.startIfStopped(tickCount);
            }
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource source, float p_376610_) {
        if (isIllusion()) {
            Player player = Minecraft.getInstance().player;
            assert player != null;
            for (int i = 0; i < 80; i++) {
                player.level().addAlwaysVisibleParticle(
                        ParticleRegistry.SWIRLING_LEAF_PARTICLE.get(),
                        this.getX() + 0.5f * random.nextDouble() - Math.sin(this.yHeadRot * Math.PI / 180) / 1.5f,
                        player.getY() + 0.5f * random.nextDouble() + 1.25f,
                        this.getZ() + 0.5f * random.nextDouble() + Math.cos(this.yHeadRot * Math.PI / 180) / 1.5f,
                        (0.5 - random.nextDouble()) * 0.3,
                        (0.5 - random.nextDouble()) * 0.3,
                        (0.5 - random.nextDouble()) * 0.3);

            }
            serverLevel.playSound(null, this.getOnPos(), SoundEvents.LAVA_EXTINGUISH, SoundSource.HOSTILE, 1f, serverLevel.getRandom().nextFloat() * 0.1F + 0.9F);
            this.discard();
            return false;
        }

        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            if (source.getDirectEntity() instanceof Projectile projectile && projectile.getOwner() instanceof LivingEntity livingEntity) {
                reflectProjectile(projectile, livingEntity);
            }

            return false;
        }

        if (random.nextFloat() < 0.25f) {
            if (source.getEntity() instanceof Player) {
                Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
                if (cameraEntity != null) {
                    cameraEntity.yRotO += 180;
                }
            }
            if (random.nextFloat() < 0.5f) {
                tpRandomly(serverLevel);
            }
            return false;
        }

        tpRandomly(serverLevel);

        return super.hurtServer(serverLevel, source, p_376610_);
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
        if (!level().isClientSide()) undoLastConfusionAttack();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public void reflectProjectile(Projectile projectile, LivingEntity owner) {
        double t = owner.distanceTo(this) / 5;
        double vx = (projectile.getX() - owner.getX()) / t;
        double vy = (projectile.getY() - owner.getY() - 3 * t * t) / t;
        double vz = (projectile.getZ() - owner.getZ()) / t;
        projectile.setDeltaMovement(vx, vy, vz);
    }

    @Override
    public ProjectileDeflection deflection(Projectile projectile) {
        return ProjectileDeflection.NONE;
    }

    public void tpRandomly(Level level) {
        for (int i = 0; i < 16; i++) {
            double d0 = this.getX() + (this.getRandom().nextDouble() - 0.5) * TP_DIAMETER;
            double d1 = Mth.clamp(
                    this.getY() + (this.getRandom().nextDouble() - 0.5) * TP_DIAMETER,
                    level.getMinY(),
                    (level.getMinY() + ((ServerLevel)level).getLogicalHeight() - 1)
            );
            double d2 = this.getZ() + (this.getRandom().nextDouble() - 0.5) * TP_DIAMETER;
            if (this.isPassenger()) {
                this.stopRiding();
            }

            if (!(Util.squareAABB(new Vec3(getArenaCenter()), ARENA_SIZE)).contains(new Vec3(d0, d1, d2))) {
                continue;
            }

            Vec3 vec3 = this.position();
            if (this.randomTeleport(d0, d1, d2, true)) {
                level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(this));
                level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS);
                this.resetFallDistance();
                break;
            }
        }
    }

    public void setClapping(boolean clapping) {
        this.entityData.set(DATA_CLAPPING, clapping);
        if (!clapping) {
            getBrain().setMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), ATTACK_COOLDOWN);
        }
    }

    public boolean isClapping() {
        return this.entityData.get(DATA_CLAPPING);
    }

    public void setIllusion(boolean illusion) {
        this.entityData.set(DATA_ILLUSION, illusion);
    }

    public boolean isIllusion() {
        return this.entityData.get(DATA_ILLUSION);
    }

    public void setLastConfusionAttack(String attack) {
        this.entityData.set(DATA_LAST_CONFUSION, attack);
    }

    public String getLastConfusionAttack() {
        return this.entityData.get(DATA_LAST_CONFUSION);
    }

    public void setSwirlLength(float length) {
        this.entityData.set(DATA_SWIRL_LENGTH, length);
    }

    public float getSwirlLength() {
        return this.entityData.get(DATA_SWIRL_LENGTH);
    }

    public void setSwirlDir(Vector3f dir) {
        this.entityData.set(DATA_SWIRL_DIR, dir);
    }

    public Vector3fc getSwirlDir() {
        return this.entityData.get(DATA_SWIRL_DIR);
    }

    public void setArenaCenter(Vector3f pos) {
        this.entityData.set(DATA_ARENA, pos);
    }

    public Vector3fc getArenaCenter() {
        return this.entityData.get(DATA_ARENA);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
