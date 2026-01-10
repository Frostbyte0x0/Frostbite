package org.exodusstudio.frostbite.common.entity.custom.animals;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.TemperatureEntity;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class BoarEntity extends Animal implements NeutralMob, TemperatureEntity {
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME;
    private static final UniformInt PERSISTENT_ANGER_TIME;
    private static final EntityDataAccessor<Long> DATA_ANGER_END_TIME =
            SynchedEntityData.defineId(BoarEntity.class, EntityDataSerializers.LONG);
    @Nullable
    private EntityReference<LivingEntity> persistentAngerTarget;

    public BoarEntity(EntityType<? extends Animal> ignored, Level level) {
        super(EntityRegistry.BOAR.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new ChargeTowardsTargetGoal(this));
        //this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0F, true));
        this.goalSelector.addGoal(7, new BreedGoal(this, 1.0F));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(3, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(8, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6F)
                .add(Attributes.ATTACK_KNOCKBACK, 1.0F)
                .add(Attributes.ATTACK_DAMAGE, 5.0F);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        this.addPersistentAngerSaveData(output);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.readPersistentAngerSaveData(this.level(), input);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.PIG_FOOD);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return EntityRegistry.BOAR.get().create(serverLevel, EntitySpawnReason.BREEDING);
    }

    public int getMaxSpawnClusterSize() {
        return 8;
    }

    public void setRemainingPersistentAngerTime(int time) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, time);
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public long getPersistentAngerEndTime() {
        return this.entityData.get(DATA_ANGER_END_TIME);
    }

    @Override
    public void setPersistentAngerEndTime(long l) {
        this.entityData.set(DATA_ANGER_END_TIME, l);
    }

    public @Nullable EntityReference<LivingEntity> getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable EntityReference<LivingEntity> entityReference) {
        this.persistentAngerTarget = entityReference;
    }

    @Override
    public boolean doHurtTarget(ServerLevel serverLevel, Entity entity) {
        entity.hurtServer(serverLevel, this.damageSources().mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        entity.push(entity.getX() - this.getX(), (entity.getY() - this.getY()) * 0.2f, entity.getZ() - this.getZ());
        return true;
    }

    @Override
    public int getBaseOuterTempIncrease() {
        return 2;
    }

    @Override
    public LivingEntity getInstance() {
        return this;
    }

    static {
        DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(BoarEntity.class, EntityDataSerializers.INT);
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    }

    static class ChargeTowardsTargetGoal extends Goal {
        private final BoarEntity boar;
        private Entity target;
        private int chargeCooldown;
        private boolean isCharging = false;
        private Vec3 chargeOrigin;

        public ChargeTowardsTargetGoal(BoarEntity boar) {
            this.boar = boar;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            target = boar.getTarget();
            return target != null;
        }

        @Override
        public void start() {
            chargeCooldown = 0;
        }

        @Override
        public void tick() {
            if (target != null) {
                boar.getNavigation().moveTo(target, 1.0D);
                chargeCooldown--;

                if (boar.distanceToSqr(target) < 6.0D
                        && boar.getSensing().hasLineOfSight(target) && chargeCooldown <= 0) {
                    isCharging = true;
                    chargeCooldown = 20;
                    chargeOrigin = boar.position();
                    boar.push((target.getX() - boar.getX()) * 0.8f, (target.getY() - boar.getY()) * 0.2f, (target.getZ() - boar.getZ()) * 0.8f);
                }



//                if (isCharging) {
//                    if (boar.isWithinMeleeAttackRange((LivingEntity) target)) {
//                        boar.doHurtTarget((ServerLevel) boar.level(), target);
//                        isCharging = false;
//                    } else {
//                        if (boar.tickCount % 20 == 0) {
//                            boar.getNavigation().moveTo(target, 2D);
//                        }
//                    }
//                }

                if (isCharging) {
                    if (boar.isWithinMeleeAttackRange((LivingEntity) target)) {
                        boar.doHurtTarget((ServerLevel) boar.level(), target);
                        isCharging = false;
                    }

                    if (boar.position().distanceTo(chargeOrigin) > 4.0D) {
                        isCharging = false;
                    }
                }
            }
        }
    }
}
