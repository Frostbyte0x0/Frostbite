package org.exodusstudio.frostbite.common.entity.custom.guards;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class GuardEntity extends Monster {
    private static final EntityDataAccessor<Float> DATA_AWAKE_TIME =
            SynchedEntityData.defineId(GuardEntity.class, EntityDataSerializers.FLOAT);
    public AnimationState wakingUpAnimationState = new AnimationState();
    public static final int WAKE_UP_ANIMATION_TICKS = 15;

    public GuardEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.GUARD.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.22);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putFloat("awakeTime", getAwakeTime());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setAwakeTime(input.getFloatOr("awakeTime", 0f));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_AWAKE_TIME, 0f);
    }

    public boolean isAwake() {
        return this.entityData.get(DATA_AWAKE_TIME) == 0;
    }

    public float getAwakeTime() {
        return this.entityData.get(DATA_AWAKE_TIME);
    }

    public void setAwakeTime(float f) {
        this.entityData.set(DATA_AWAKE_TIME, f);
        if (f > 0) {
            wakingUpAnimationState.startIfStopped(tickCount);
        }
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
