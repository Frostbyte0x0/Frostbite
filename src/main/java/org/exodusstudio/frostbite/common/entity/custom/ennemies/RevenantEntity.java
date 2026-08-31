package org.exodusstudio.frostbite.common.entity.custom.ennemies;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.jetbrains.annotations.Nullable;

public class RevenantEntity extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_IS_RECOVERING =
            SynchedEntityData.defineId(RevenantEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_RECOVERING_TICKS =
            SynchedEntityData.defineId(RevenantEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_RISING =
            SynchedEntityData.defineId(RevenantEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_RISING_TICKS =
            SynchedEntityData.defineId(RevenantEntity.class, EntityDataSerializers.INT);
    public final AnimationState risingAnimationState = new AnimationState();

    public RevenantEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.REVENANT.get(), level);
        risingAnimationState.startIfStopped(tickCount);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.2, true) {
            public boolean canUse() {return super.canUse() && mob instanceof RevenantEntity r && !r.isRecovering();}
        });
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_RECOVERING, false);
        builder.define(DATA_RECOVERING_TICKS, 0);
        builder.define(DATA_IS_RISING, false);
        builder.define(DATA_RISING_TICKS, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("recoveringTicks", getRecoveringTicks());
        output.putBoolean("isRecovering", isRecovering());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        setRecoveringTicks(input.getIntOr("recoveringTicks", 0));
        setRecovering(input.getBooleanOr("isRecovering", false));
    }

    @Override
    public void aiStep() {
//        if (!isRecovering() && !isRising()) {
            super.aiStep();
//        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (amount < 3.4E38 && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (this.isRecovering() && !isDamageableAttack(source)) {
                return false;
            }
            if (amount > getHealth() && !isDamageableAttack(source)) {
                setHealth(1);
                return false;
            }
        }
        return super.hurtServer(level, source, amount);
    }

    @Override
    public boolean hurtClient(DamageSource damageSource) {
        if (this.isRecovering() && !isDamageableAttack(damageSource)) {
            return false;
        }
        return super.hurtClient(damageSource);
    }

    @Override
    public void tick() {
        super.tick();

        if (!isRecovering() && !isRising()) {
            setDeltaMovement(0, getDeltaMovement().y, 0);
        }

        if (getHealth() <= 1) {
            setRecovering(true);
        } else if (getHealth() >= 20 && isRecovering()) {
            setRecovering(false);
        }

        if (isRising()) {
            if (getRisingTicks() >= risingAnimationState.getTimeInMillis(tickCount) * 20 * 1000) {
                setRising(false);
            } else {
                setRisingTicks(getRisingTicks() + 1);
            }
        } else {
            setRisingTicks(0);
        }

        if (isRecovering()) {
            setRecoveringTicks(getRecoveringTicks() + 1);
            if (getRecoveringTicks() > 80 && getRecoveringTicks() % 10 == 0 && getHealth() < 20) {
                setHealth(getHealth() + 1);
            }
        } else {
            if (getRecoveringTicks() > 0 && level() instanceof ServerLevel serverLevel) {
                RevenantEntity revenant = new RevenantEntity(null, level());
                revenant.setPos(getX(), getY(), getZ());
                revenant.setRising(true);

                serverLevel.addFreshEntityWithPassengers(revenant);
                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, getOnPos(), GameEvent.Context.of(revenant));
            }
            setRecoveringTicks(0);
        }
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return random.nextFloat() * 0.4f + 0.6f;
    }

    public boolean isRecovering() {
        return this.entityData.get(DATA_IS_RECOVERING);
    }

    public void setRecovering(boolean recovering) {
        this.entityData.set(DATA_IS_RECOVERING, recovering);
    }

    public int getRecoveringTicks() {
        return this.entityData.get(DATA_RECOVERING_TICKS);
    }

    public void setRecoveringTicks(int ticks) {
        this.entityData.set(DATA_RECOVERING_TICKS, ticks);
    }

    public boolean isRising() {
        return this.entityData.get(DATA_IS_RISING);
    }

    public void setRising(boolean rising) {
        if (rising) {
            this.risingAnimationState.start(this.tickCount);
        } else {
            this.risingAnimationState.stop();
        }
        this.entityData.set(DATA_IS_RISING, rising);
    }

    public int getRisingTicks() {
        return this.entityData.get(DATA_RISING_TICKS);
    }

    public void setRisingTicks(int ticks) {
        this.entityData.set(DATA_RISING_TICKS, ticks);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    private boolean isDamageableAttack(DamageSource source) {
        return source.is(DamageTypeTags.IS_FIRE);
    }
}
