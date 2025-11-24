package org.exodusstudio.frostbite.common.entity.custom.ennemies;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.exodusstudio.frostbite.common.entity.goals.FlyingMoveControl;
import org.exodusstudio.frostbite.common.entity.goals.FlyingRandomMoveGoal;
import org.exodusstudio.frostbite.common.entity.goals.SpecterDashAttackGoal;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class SpecterEntity extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_IS_TRANSPARENT =
            SynchedEntityData.defineId(SpecterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_ATTACKING =
            SynchedEntityData.defineId(SpecterEntity.class, EntityDataSerializers.BOOLEAN);

    public SpecterEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.SPECTER.get(), level);
        this.moveControl = new FlyingMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new SpecterDashAttackGoal(this, 0.1f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new FlyingRandomMoveGoal(this, 0.15f));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.15);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_TRANSPARENT, false);
        builder.define(DATA_IS_ATTACKING, false);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setTransparent(input.getBooleanOr("isTransparent", false));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("isTransparent", isTransparent());
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.move(MoverType.SELF, getDeltaMovement());
        this.noPhysics = false;
        this.setNoGravity(true);
        if (!level().isEmptyBlock(blockPosition()) && Math.abs(getDeltaMovement().length()) < 0.03) {
            setDeltaMovement(getDeltaMovement().x, getDeltaMovement().y + 0.03, getDeltaMovement().z);
        }

        setTransparent(!isAttacking());

        //this.setPos(this.getX() + getDeltaMovement().x, this.getY() + getDeltaMovement().y, this.getZ() + getDeltaMovement().z);
//
//        if (tickCount % 20 == 0) {
//            setTransparent(!isTransparent());
//        }
    }

    @Override
    public boolean hurtServer(ServerLevel p_376221_, DamageSource p_376460_, float p_376610_) {
        if (isTransparent()) {
            return false;
        }
        return super.hurtServer(p_376221_, p_376460_, p_376610_);
    }

    public boolean isTransparent() {
        return this.entityData.get(DATA_IS_TRANSPARENT);
    }

    public void setTransparent(boolean transparent) {
        this.entityData.set(DATA_IS_TRANSPARENT, transparent);
    }

    public boolean isAttacking() {
        return this.entityData.get(DATA_IS_ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(DATA_IS_ATTACKING, attacking);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected void pushEntities() {}

    @Override
    protected void doPush(Entity entity) {}

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }
}
