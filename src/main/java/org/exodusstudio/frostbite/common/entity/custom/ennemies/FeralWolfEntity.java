package org.exodusstudio.frostbite.common.entity.custom.ennemies;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.goals.FeralWolfBiteGoal;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class FeralWolfEntity extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_IS_FROZEN =
            SynchedEntityData.defineId(FeralWolfEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_BITING =
            SynchedEntityData.defineId(FeralWolfEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_TICKS_SINCE_LAST_BITE =
            SynchedEntityData.defineId(FeralWolfEntity.class, EntityDataSerializers.INT);
    public final AnimationState bitingAnimationState = new AnimationState();

    public FeralWolfEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.FERAL_WOLF.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new FeralWolfBiteGoal(this, 1.4));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
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
        builder.define(DATA_IS_FROZEN, false);
        builder.define(DATA_IS_BITING, false);
        builder.define(DATA_TICKS_SINCE_LAST_BITE, 0);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("isFrozen", this.isFrozen());
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setFrozen(input.getBooleanOr("isFrozen", false));
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_IS_BITING.equals(accessor)) {
            this.bitingAnimationState.stop();
            if (this.getEntityData().get(DATA_IS_BITING)) {
                this.bitingAnimationState.startIfStopped(tickCount);
            }
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public void tick() {
        super.tick();
        if (Frostbite.temperatureStorage.getTemperature(uuid.toString(), true) < -30) {
            freeze();
        }

        if (level() instanceof ServerLevel serverLevel &&
                getTarget() instanceof Player player &&
                isBiting() &&
                bitingAnimationState.getTimeInMillis(tickCount) / 50 == 5) {
            player.hurtServer(serverLevel, damageSources().freeze(), isFrozen() ? 5 : 2.5f);
        }

        if (isBiting()) {
            setTicksSinceLastAttack(getTicksSinceLastAttack() + 1);
        }

        if (getTicksSinceLastAttack() > 30) {
            setBiting(false);
            setTicksSinceLastAttack(0);
        }
    }

    public void freeze() {
        if (!this.isFrozen()) {
            this.setFrozen(true);
            setHealth(getMaxHealth());
        }
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    public boolean isFrozen() {
        return this.entityData.get(DATA_IS_FROZEN);
    }

    public void setFrozen(boolean frozen) {
        this.entityData.set(DATA_IS_FROZEN, frozen);
    }

    public boolean isBiting() {
        return this.entityData.get(DATA_IS_BITING);
    }

    public void setBiting(boolean biting) {
        this.entityData.set(DATA_IS_BITING, biting);
    }

    public int getTicksSinceLastAttack() {
        return this.entityData.get(DATA_TICKS_SINCE_LAST_BITE);
    }

    public void setTicksSinceLastAttack(int ticks) {
        this.entityData.set(DATA_TICKS_SINCE_LAST_BITE, ticks);
    }
}
