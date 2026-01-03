package org.exodusstudio.frostbite.common.entity.custom.shaman;

import com.mojang.serialization.Dynamic;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.IcedCreeperEntity;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.IcedSkeletonEntity;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.IcedZombieEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealHammerEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealHandsEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealSwordEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealWeaponEntity;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.MemoryModuleTypeRegistry;
import org.exodusstudio.frostbite.common.util.TargetingEntity;
import org.exodusstudio.frostbite.common.util.Util;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ShamanEntity extends Monster implements TargetingEntity {
    private static final EntityDataAccessor<String> DATA_LAST_STATE =
            SynchedEntityData.defineId(ShamanEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_STATE =
            SynchedEntityData.defineId(ShamanEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_TICKS_SINCE_LAST_CHANGE =
            SynchedEntityData.defineId(ShamanEntity.class, EntityDataSerializers.INT);
    private static final Component SHAMAN_NAME_COMPONENT = Component.translatable("entity.shaman.boss_bar");
    private final ServerBossEvent bossEvent = (ServerBossEvent)
            new ServerBossEvent(SHAMAN_NAME_COMPONENT, BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);
    public final AnimationState lastAnimationState = new AnimationState();
    public final AnimationState currentAnimationState = new AnimationState();
    public static final int ETHEREAL_COOLDOWN = 90;
    public static final int WHIRLPOOL_COOLDOWN = 90;
    public static final int SUMMON_COOLDOWN = 90;
    public static final int CURSE_COOLDOWN = 90;
    public static final int ETHEREAL_DURATION = 30;
    public static final int WHIRLPOOL_DURATION = 30;
    public static final int SUMMON_DURATION = 30;
    public static final int CURSE_DURATION = 30;
    public static final int BLEND_TICKS = 10;
    private static final Map<MemoryModuleType<Integer>, Integer> cooldowns = Map.of(
            MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), ETHEREAL_COOLDOWN,
            MemoryModuleTypeRegistry.WHIRLPOOL_COOLDOWN.get(), WHIRLPOOL_COOLDOWN,
            MemoryModuleTypeRegistry.CURSE_COOLDOWN.get(), CURSE_COOLDOWN,
            MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get(), SUMMON_COOLDOWN
    );

    public ShamanEntity(EntityType<? extends ShamanEntity> ignored, Level level) {
        super(EntityRegistry.SHAMAN.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_LAST_STATE, "idle");
        builder.define(DATA_STATE, "idle");
        builder.define(DATA_TICKS_SINCE_LAST_CHANGE, 0);
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        super.customServerAiStep(serverLevel);
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        ShamanAI.updateActivity(this);
        ((Brain<ShamanEntity>) getBrain()).tick(serverLevel, this);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }

    @Override
    protected @NotNull Brain<?> makeBrain(Dynamic<?> dynamic) {
        return ShamanAI.makeBrain(this, dynamic);
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

    public String chooseAttack(LivingEntity target) {
        if (getRandom().nextFloat() < 0.2) {
            return "lyre";
        } else if (getRandom().nextFloat() < 0.2) {
            return "shield";
        } else if (getRandom().nextFloat() < 0.2) {
            return "summon";
        } else {
            return "smite";
        }
    }

    @Override
    public void tick() {
        super.tick();
        cooldowns.forEach((memoryModuleType, cooldown) -> {
            if (!isDeadOrDying() && getBrain().getMemory(memoryModuleType).isPresent()) {
                this.getBrain().setMemory(memoryModuleType, getBrain().getMemory(memoryModuleType).get() - 1);
                if (getBrain().getMemory(memoryModuleType).get() < -10) {
                    getBrain().setMemory(memoryModuleType, cooldown);
                }
            }
        });

        setTicksSinceLastChange(getTicksSinceLastChange() + 1);


//        if (getAttackableFromBrain() != null && isPlaying() && playAnimationState.getTimeInMillis(tickCount) / 50 == 9) {
//            String attack = chooseAttack(getAttackableFromBrain());
//            switch (attack) {
//                case "lyre" -> lyreAttack(getAttackableFromBrain());
//                case "shield" -> shieldAttack();
//                case "summon" -> summonAttack();
//                case "smite" -> smiteAttack(getAttackableFromBrain());
//            }
//        }
    }

    public void lyreAttack(LivingEntity target) {
        if (level() instanceof ServerLevel serverLevel) {
            Vec3 v = target.position().subtract(position()).normalize();
            EtherealWeaponEntity weapon;

            if (getRandom().nextFloat() < 0.333) {
                weapon = new EtherealHammerEntity(null, serverLevel);
            } else if (getRandom().nextFloat() < 0.333) {
                weapon = new EtherealHandsEntity(null, serverLevel);
            } else {
                weapon = new EtherealSwordEntity(null, serverLevel);
            }

            weapon.setPos(position().add(v.scale(3)).add(0, 1.5, 0));
            float[] angles = Util.getXYRot(v);
            weapon.setXRot(angles[0]);
            weapon.setYRot(angles[1]);
            serverLevel.addFreshEntity(weapon);
        }
    }

    public void shieldAttack() {

    }

    public void summonAttack() {
        if (!(level() instanceof ServerLevel serverLevel)) return;
        for (int i = 0; i < 15; i++) {
            Util.spawnMonsterRandomlyAroundEntity(() -> new IcedCreeperEntity(null, serverLevel),
                    serverLevel, this, 5, 10);
            Util.spawnMonsterRandomlyAroundEntity(() -> new IcedSkeletonEntity(null, serverLevel),
                    serverLevel, this, 5, 10);
            Util.spawnMonsterRandomlyAroundEntity(() -> new IcedZombieEntity(null, serverLevel),
                    serverLevel, this, 5, 10);
        }
    }

    public void smiteAttack(LivingEntity target) {
        if (level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 3; i++) {
                LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, serverLevel);
                lightningBolt.setPos(target.position());
                serverLevel.addFreshEntity(lightningBolt);
            }
        }
    }

    @Nullable
    protected LivingEntity getAttackableFromBrain() {
        return this.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE).orElse(null);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_STATE.equals(accessor)) {
            this.lastAnimationState.copyFrom(currentAnimationState);
            this.currentAnimationState.stop();
            this.currentAnimationState.startIfStopped(tickCount);
            setTicksSinceLastChange(0);
            this.refreshDimensions();

            if (isIdle()) {
                if (getBrain().getMemory(MemoryModuleType.LOOK_TARGET).isPresent()) {
                    getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                            new WalkTarget(getBrain().getMemory(MemoryModuleType.LOOK_TARGET).get(), 1, 2));
                }
            }
        }

        super.onSyncedDataUpdated(accessor);
    }

    public String getLastState() {
        return this.entityData.get(DATA_LAST_STATE);
    }

    public void setLastState(String state) {
        this.entityData.set(DATA_LAST_STATE, state);
    }

    public String getState() {
        return this.entityData.get(DATA_STATE);
    }

    public void setState(String state) {
        this.entityData.set(DATA_STATE, state);
    }

    public boolean isIdle() {
        return getState().equals("idle");
    }

    public void setIdle() {
        this.setLastState(getState());
        this.setState("idle");
    }

    public boolean isSummoning() {
        return getState().equals("summoning");
    }

    public void setSummoning() {
        this.setLastState(getState());
        this.setState("summoning");
    }

    public boolean isWhirlpooling() {
        return getState().equals("whirlpooling");
    }

    public void setWhirlpooling() {
        this.setLastState(getState());
        this.setState("whirlpooling");
    }

    public int getTicksSinceLastChange() {
        return this.entityData.get(DATA_TICKS_SINCE_LAST_CHANGE);
    }

    public void setTicksSinceLastChange(int ticks) {
        this.entityData.set(DATA_TICKS_SINCE_LAST_CHANGE, ticks);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
