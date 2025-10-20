package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

public class RevenantEntity extends Monster {
    private static final EntityDataAccessor<Boolean> DATA_IS_RECOVERING =
            SynchedEntityData.defineId(RevenantEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_RECOVERING_TICKS =
            SynchedEntityData.defineId(RevenantEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_RISING =
            SynchedEntityData.defineId(RevenantEntity.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState risingAnimationState = new AnimationState();

    public RevenantEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.REVENANT.get(), level);
    }

    @Override
    protected void registerGoals() {
        //this.goalSelector.addGoal(0, new RevenantRecoverGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
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
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 35)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
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
    public void die(DamageSource damageSource) {
        if (isAttackedWithSilverStake(damageSource)) {
            super.die(damageSource);
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (this.isRecovering() && !isAttackedWithSilverStake(source)) {
            return false;
        }
        return super.hurtServer(level, source, amount);
    }

    @Override
    public boolean hurtClient(DamageSource damageSource) {
        if (this.isRecovering() && !isAttackedWithSilverStake(damageSource)) {
            return false;
        }
        return super.hurtClient(damageSource);
    }

    @Override
    public void tick() {
        super.tick();

        if (getHealth() <= 1) {
            setRecovering(true);
        } else if (getHealth() >= 20 && isRecovering()) {
            setRecovering(false);
        }

        if (isRecovering()) {
            setRecoveringTicks(getRecoveringTicks() + 1);
            if (getRecoveringTicks() > 80 && getRecoveringTicks() % 10 == 0 && getHealth() < 20) {
                setHealth(getHealth() + 1);
            }
        } else {
            setRecoveringTicks(0);
        }
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
        }
        this.entityData.set(DATA_IS_RISING, rising);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    private boolean isAttackedWithSilverStake(DamageSource source) {
        Entity entity = source.getEntity();
        return entity != null && entity.getWeaponItem() != null && entity.getWeaponItem().is(ItemRegistry.SILVER_STAKE);
    }
}
