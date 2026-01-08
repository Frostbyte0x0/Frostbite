package org.exodusstudio.frostbite.common.entity.custom.guards;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.helper.StateMonsterEntity;
import org.exodusstudio.frostbite.common.entity.goals.GuardAndApproachGoal;
import org.exodusstudio.frostbite.common.entity.goals.GuardMeleeAttackGoal;
import org.exodusstudio.frostbite.common.util.Util;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class GuardEntity extends StateMonsterEntity {
    private static final EntityDataAccessor<Integer> DATA_ATTACK_COOLDOWN =
            SynchedEntityData.defineId(GuardEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_GUARD_COOLDOWN =
            SynchedEntityData.defineId(GuardEntity.class, EntityDataSerializers.INT);
    public static final int WAKE_UP_ANIMATION_TICKS = 15;
    public static final int ATTACK_ANIMATION_TICKS = 15;
    public static final int BLEND_TICKS = 10;
    public static final int ATTACK_COOLDOWN = 30;
    public static final int GUARD_COOLDOWN = 30;

    public GuardEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level, BLEND_TICKS);
        currentAnimationState.startIfStopped(tickCount - 2 * BLEND_TICKS);
        setCurrentState("asleep");
        setLastState("asleep");
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new GuardAndApproachGoal(this, 0.75, false));
        this.goalSelector.addGoal(2, new GuardMeleeAttackGoal(this, 1.2, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("isAwake", isAwake());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        boolean isAwake = input.getBooleanOr("isAwake", false);
        if (isAwake) {
            this.entityData.set(DATA_STATE, "idle");
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ATTACK_COOLDOWN, ATTACK_COOLDOWN);
        builder.define(DATA_GUARD_COOLDOWN, GUARD_COOLDOWN);
    }

    public void serverAiStep() {
        if (isAwake()) {
            super.serverAiStep();
        }
    }

    @Override
    public void tick() {
        super.tick();

        setGuardCooldown(getGuardCooldown() - 1);
        setAttackCooldown(getAttackCooldown() - 1);

        if (level() instanceof ServerLevel serverLevel) {
            float t = currentAnimationState.getTimeInMillis(tickCount) / 50f;

            if (isAttacking()) {
                if (t == 6) {
                    doAttack(serverLevel);
                } else if (t == ATTACK_ANIMATION_TICKS) {
                    setIdle();
                }
            }
        }

        if (isGuarding() || isAttacking()) {
            this.yBodyRot = yHeadRot;
        }

        getNavigation().setSpeedModifier(isGuarding() ? 0.75 : 1);
    }

    public AABB getAttackAABB() {
        return Util.squareAABB(position(), 2)
                .move(getViewVector(0).normalize().scale(2).add(0, 1.5, 0));
    }

    public void doAttack(ServerLevel serverLevel) {
        serverLevel.getEntitiesOfClass(LivingEntity.class, getAttackAABB())
                .forEach(entity -> {
                    if (entity != this) entity.hurtServer(serverLevel, damageSources().mobAttack(this), 7.5f);
                });
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (isGuarding() && source.getDirectEntity() instanceof LivingEntity) {
            Vec3 dir = source.getDirectEntity().position().subtract(position());
            double angle = Math.atan2(dir.z, dir.x) * (180 / Math.PI);
            double diff = (((angle + 360) % 360) - ((yBodyRot + 360) % 360) + 360) % 360;
            if (diff <= 180) {
                return false;
            } else {
                amount *= 2;
            }
        }

        if (!isAwake()) {
            if (source.getDirectEntity() instanceof LivingEntity e && canTargetEntity(e)) {
                setTarget(e);
            }
            setWakingUp();
            amount = 0;
        }

        return super.hurtServer(level, source, amount);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
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
    public boolean isWithinMeleeAttackRange(LivingEntity entity) {
        return entity.distanceToSqr(this) < 9;
    }

    @Override
    public boolean isPushable() {
        return isAwake();
    }

    public boolean isAwake() {
        if (getCurrentState().equals("wakingUp")) {
            return this.entityData.get(DATA_TICKS_SINCE_LAST_CHANGE) >= WAKE_UP_ANIMATION_TICKS;
        }
        return !getCurrentState().equals("asleep");
    }

    public void setWakingUp() {
        if (isAwake()) {
            return;
        }
        this.entityData.set(DATA_LAST_STATE, getCurrentState());
        this.entityData.set(DATA_STATE, "wakingUp");
    }

    public boolean isAttacking() {
        return this.entityData.get(DATA_STATE).equals("attacking");
    }

    public void setAttacking() {
        this.entityData.set(DATA_LAST_STATE, getCurrentState());
        this.entityData.set(DATA_STATE, "attacking");
    }

    public boolean isGuarding() {
        return this.entityData.get(DATA_STATE).equals("guarding");
    }

    public void setGuarding() {
        this.entityData.set(DATA_LAST_STATE, getCurrentState());
        this.entityData.set(DATA_STATE, "guarding");
    }

    public int getAttackCooldown() {
        return this.entityData.get(DATA_ATTACK_COOLDOWN);
    }

    public void setAttackCooldown(int cooldown) {
        this.entityData.set(DATA_ATTACK_COOLDOWN, cooldown);
    }

    public int getGuardCooldown() {
        return this.entityData.get(DATA_GUARD_COOLDOWN);
    }

    public void setGuardCooldown(int cooldown) {
        this.entityData.set(DATA_GUARD_COOLDOWN, cooldown);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
