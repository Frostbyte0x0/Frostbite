package org.exodusstudio.frostbite.common.entity.custom.guards;

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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.MemoryModuleTypeRegistry;
import org.exodusstudio.frostbite.common.util.TargetingEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChiefGuardEntity extends Monster implements TargetingEntity {
    private static final EntityDataAccessor<Boolean> DATA_ATTACKING =
            SynchedEntityData.defineId(ChiefGuardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DATA_LAST_STATE =
            SynchedEntityData.defineId(ChiefGuardEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_STATE =
            SynchedEntityData.defineId(ChiefGuardEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_TICKS_SINCE_LAST_CHANGE =
            SynchedEntityData.defineId(ChiefGuardEntity.class, EntityDataSerializers.INT);
    private static final Component NAME_COMPONENT = Component.translatable("entity.chief_guard.boss_bar");
    private final ServerBossEvent bossEvent = (ServerBossEvent)
            new ServerBossEvent(NAME_COMPONENT, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);
    public final AnimationState lastAnimationState = new AnimationState();
    public final AnimationState currentAnimationState = new AnimationState();
    public static final int ATTACK_COOLDOWN = 60;
    public static final int SUMMON_COOLDOWN = 300;
    public static final int BLEND_TICKS = 10;
    public static final int ATTACK_ANIM_TIME = 40;
    public static final int SUMMON_ANIM_TIME = 40;

    public ChiefGuardEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.CHIEF_GUARD.get(), level);
        setIdle();
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
        builder.define(DATA_ATTACKING, false);
        builder.define(DATA_LAST_STATE, "idle");
        builder.define(DATA_STATE, "idle");
        builder.define(DATA_TICKS_SINCE_LAST_CHANGE,  10);
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        super.customServerAiStep(serverLevel);
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        ChiefGuardAI.updateActivity(this);
        ((Brain<ChiefGuardEntity>) getBrain()).tick(serverLevel, this);
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
        return ChiefGuardAI.makeBrain(this, dynamic);
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
        }

        if (!isDeadOrDying() && getBrain().getMemory(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get()).isPresent()) {
            this.getBrain().setMemory(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get(), getBrain().getMemory(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get()).get() - 1);
            if (getBrain().getMemory(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get()).get() < -10) {
                getBrain().setMemory(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get(), SUMMON_COOLDOWN);
            }
        }

        if (isDashing() && onGround()) {
            setIdle();
        }

        setTicksSinceLastChange(getTicksSinceLastChange() + 1);

        if (getTicksSinceLastChange() > BLEND_TICKS) {
            lastAnimationState.stop();
        }
    }

    public void dash() {
        LivingEntity target = getAttackableFromBrain();
        if (target != null) {
            Vec3 dir = target.position().subtract(position());
            this.addDeltaMovement(dir.normalize().add(0, 0.25, 0).scale(1.5));
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
        }

        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public void setAttacking() {
        this.entityData.set(DATA_LAST_STATE, getCurrentState());
        this.entityData.set(DATA_STATE, "attacking");
        getBrain().setMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), ATTACK_COOLDOWN);
    }

    public void setIdle() {
        this.entityData.set(DATA_LAST_STATE, getCurrentState());
        this.entityData.set(DATA_STATE, "idle");
    }

    public boolean isIdle() {
        return getCurrentState().equals("idle");
    }

    public boolean isAttacking() {
        return getCurrentState().equals("attacking");
    }

    public void setGuarding() {
        this.entityData.set(DATA_LAST_STATE, getCurrentState());
        this.entityData.set(DATA_STATE, "guarding");
    }

    public boolean isGuarding() {
        return getCurrentState().equals("guarding");
    }

    public void setDashing() {
        this.entityData.set(DATA_LAST_STATE, getCurrentState());
        this.entityData.set(DATA_STATE, "dashing");
    }

    public boolean isDashing() {
        return getCurrentState().equals("dashing");
    }

    public void setSummoning() {
        this.entityData.set(DATA_LAST_STATE, getCurrentState());
        this.entityData.set(DATA_STATE, "summoning");
    }

    public boolean isSummoning() {
        return getCurrentState().equals("summoning");
    }

    public String getCurrentState() {
        return this.getEntityData().get(DATA_STATE);
    }

    public String getLastState() {
        return this.getEntityData().get(DATA_LAST_STATE);
    }

    public int getTicksSinceLastChange() {
        return this.getEntityData().get(DATA_TICKS_SINCE_LAST_CHANGE);
    }

    public void setTicksSinceLastChange(int ticks) {
        this.getEntityData().set(DATA_TICKS_SINCE_LAST_CHANGE, ticks);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
