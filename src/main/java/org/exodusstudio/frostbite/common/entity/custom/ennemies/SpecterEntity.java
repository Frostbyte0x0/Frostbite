package org.exodusstudio.frostbite.common.entity.custom.ennemies;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
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
import org.exodusstudio.frostbite.common.entity.goals.FlyingMoveControl;
import org.exodusstudio.frostbite.common.entity.goals.FlyingRandomMoveGoal;
import org.exodusstudio.frostbite.common.entity.goals.SpecterBackAwayAndDashAttackGoal;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.jetbrains.annotations.Nullable;

public class SpecterEntity extends Monster {
    private static final EntityDataAccessor<String> DATA_STATE =
            SynchedEntityData.defineId(SpecterEntity.class, EntityDataSerializers.STRING);

    public SpecterEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.SPECTER.get(), level);
        this.moveControl = new FlyingMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new SpecterBackAwayAndDashAttackGoal(this, 0.1f));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new FlyingRandomMoveGoal(this, 0.15f));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(Attributes.MOVEMENT_SPEED, 0.15);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_STATE, "idle");
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

        setDeltaMovement(getDeltaMovement().x * 0.92, getDeltaMovement().y * 0.92, getDeltaMovement().z * 0.92);

//        if (getTarget() != null && distanceTo(getTarget()) < 3 && isTransparent() && !isBackingAway()) {
//            setBackingAway(true);
//        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float damage) {
        if (!getState().equals("attacking") && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return false;
        }
        setState("backing");
        return super.hurtServer(level, damageSource, damage);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.BOGGED_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.BOGGED_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BOGGED_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return random.nextFloat() * 0.3f + 0.25f;
    }

    public String getState() {
        return this.entityData.get(DATA_STATE);
    }

    public void setState(String state) {
        this.entityData.set(DATA_STATE, state);
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
