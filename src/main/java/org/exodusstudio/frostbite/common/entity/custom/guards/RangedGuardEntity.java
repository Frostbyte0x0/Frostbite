package org.exodusstudio.frostbite.common.entity.custom.guards;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class RangedGuardEntity extends GuardEntity implements CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW =
            SynchedEntityData.defineId(RangedGuardEntity.class, EntityDataSerializers.BOOLEAN);

    public RangedGuardEntity(EntityType<? extends GuardEntity> ignored, Level level) {
        super(EntityRegistry.RANGED_GUARD.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
//        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 3, 1.3, 1.5));
        this.goalSelector.addGoal(3, new RangedCrossbowAttackGoal<>(this, 1, 20));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, GuardEntity.class, RangedGuardEntity.class, HeavyGuardEntity.class, ChiefGuardEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_CHARGING_CROSSBOW, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount == 1) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
        }
        equipment.get(EquipmentSlot.MAINHAND);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public void setChargingCrossbow(boolean isCharging) {
        setCharging(true);
    }

    public PiglinArmPose getArmPose() {
        if (isCharging()) {
            return PiglinArmPose.CROSSBOW_CHARGE;
        } else {
            return this.isHolding((is) -> is.getItem() instanceof CrossbowItem) && CrossbowItem.isCharged(this.getWeaponItem()) ? PiglinArmPose.CROSSBOW_HOLD : PiglinArmPose.DEFAULT;
        }
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float crossbowPower) {
        ItemStack usedItem = getMainHandItem();
        ItemStack projectile = getProjectile(usedItem);
        Item var6 = usedItem.getItem();
        if (var6 instanceof CrossbowItem && level() instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 10; i++) {
                Arrow arrow = new Arrow(level(), this, ItemStack.EMPTY, usedItem) {
                    @Override
                    public void tick() {
                        super.tick();
                        if (isInGround()) discard();
                    }
                };
                Projectile.spawnProjectileUsingShoot(arrow, serverLevel, projectile,
                        livingEntity.getX() - arrow.getX(),
                        livingEntity.getY() - arrow.getY() + livingEntity.distanceTo(arrow) * 0.2,
                        livingEntity.getZ() - arrow.getZ(), 1.6F,
                        30 - serverLevel.getDifficulty().getId() * 4);
            }
        }

        this.onCrossbowAttackPerformed();
    }

    public void setCharging(boolean charging) {
        entityData.set(IS_CHARGING_CROSSBOW, charging);
    }

    public boolean isCharging() {
        return entityData.get(IS_CHARGING_CROSSBOW);
    }
}
