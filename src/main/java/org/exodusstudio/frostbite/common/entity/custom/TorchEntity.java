package org.exodusstudio.frostbite.common.entity.custom;

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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.entity.custom.goals.TorchSliceGoal;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class TorchEntity extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_SLICING =
            SynchedEntityData.defineId(TorchEntity.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState slicingAnimationState = new AnimationState();

    public TorchEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.TORCH.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new TorchSliceGoal(this, 0.8));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SLICING, false);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_SLICING.equals(accessor)) {
            this.slicingAnimationState.stop();
            if (this.getEntityData().get(DATA_SLICING)) {
                this.slicingAnimationState.startIfStopped(tickCount);
            }

            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(accessor);
    }

    public void setSlicing(boolean slicing) {
        this.getEntityData().set(DATA_SLICING, slicing);
    }

    public boolean isSlicing() {
        return this.getEntityData().get(DATA_SLICING);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
