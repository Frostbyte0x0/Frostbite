package org.exodusstudio.frostbite.common.entity.custom.bard;

import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.MemoryModuleTypeRegistry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.EnchantingTableBlock.BOOKSHELF_OFFSETS;

public class BardEntity extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_PLAYING =
            SynchedEntityData.defineId(BardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final Component BARD_NAME_COMPONENT = Component.translatable("entity.bard.boss_bar");
    private final ServerBossEvent bossEvent = (ServerBossEvent)
            new ServerBossEvent(BARD_NAME_COMPONENT, BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);
    public final AnimationState playAnimationState = new AnimationState();
    public static final int ATTACK_COOLDOWN = 60;

    public BardEntity(EntityType<? extends BardEntity> ignored, Level level) {
        super(EntityRegistry.BARD.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 8)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_PLAYING, false);
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        super.customServerAiStep(serverLevel);
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        BardAI.updateActivity(this);
        ((Brain<BardEntity>) getBrain()).tick(serverLevel, this);
    }

    @Override
    protected @NotNull Brain<?> makeBrain(Dynamic<?> dynamic) {
        return BardAI.makeBrain(this, dynamic);
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

            if (getBrain().getMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get()).get() == 30 && level() instanceof ServerLevel serverLevel && isPlaying()) {
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


        if (getAttackableFromBrain() != null && isPlaying() && playAnimationState.getTimeInMillis(tickCount) / 50 == 9) {
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
            setPlaying(false);
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

    public boolean isPlaying() {
        return this.entityData.get(DATA_PLAYING);
    }

    public void setPlaying(boolean playing) {
        this.entityData.set(DATA_PLAYING, playing);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
