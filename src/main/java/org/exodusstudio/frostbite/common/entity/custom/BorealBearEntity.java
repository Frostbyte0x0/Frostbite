package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.entity.custom.goals.BorealBearAttackGoal;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.CustomTemperatureEntity;

public class BorealBearEntity extends Monster implements CustomTemperatureEntity {
    private static final EntityDataAccessor<Boolean> DATA_IS_ATTACKING =
            SynchedEntityData.defineId(BorealBearEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_TICKS_SINCE_LAST_ATTACK =
            SynchedEntityData.defineId(BorealBearEntity.class, EntityDataSerializers.INT);
    public final AnimationState attackingAnimationState = new AnimationState();

    public BorealBearEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.BOREAL_BEAR.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new BorealBearAttackGoal(this, 1.4));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.FOLLOW_RANGE, 15)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_ATTACKING, false);
        builder.define(DATA_TICKS_SINCE_LAST_ATTACK, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_IS_ATTACKING.equals(accessor)) {
            this.attackingAnimationState.stop();
            if (this.getEntityData().get(DATA_IS_ATTACKING)) {
                this.attackingAnimationState.startIfStopped(tickCount);
            }
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public void tick() {
        super.tick();
        if (level() instanceof ServerLevel serverLevel &&
                getTarget() instanceof Player player &&
                isAttacking() &&
                attackingAnimationState.getTimeInMillis(tickCount) / 50 == 5) {
            player.hurtServer(serverLevel, damageSources().freeze(), 5);
        }

        if (isAttacking()) {
            setTicksSinceLastAttack(getTicksSinceLastAttack() + 1);
        }

        if (getTicksSinceLastAttack() > 30) {
            setAttacking(false);
            setTicksSinceLastAttack(0);
        }
    }

    @Override
    public void die(DamageSource damageSource) {
        super.die(damageSource);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public int getBaseOuterTempIncrease() {
        return 4;
    }

    public boolean isAttacking() {
        return this.entityData.get(DATA_IS_ATTACKING);
    }

    public void setAttacking(boolean biting) {
        this.entityData.set(DATA_IS_ATTACKING, biting);
    }

    public int getTicksSinceLastAttack() {
        return this.entityData.get(DATA_TICKS_SINCE_LAST_ATTACK);
    }

    public void setTicksSinceLastAttack(int ticks) {
        this.entityData.set(DATA_TICKS_SINCE_LAST_ATTACK, ticks);
    }
}
