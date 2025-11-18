package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.entity.custom.goals.BanditFleeGoal;
import org.exodusstudio.frostbite.common.entity.custom.goals.BanditStealGoal;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.CustomTemperatureEntity;
import org.jetbrains.annotations.Nullable;

public class BanditEntity extends Animal implements CustomTemperatureEntity {
    private static final EntityDataAccessor<String> DATA_STATE =
            SynchedEntityData.defineId(BanditEntity.class, EntityDataSerializers.STRING);
    public final AnimationState stealingAnimationState = new AnimationState();
    public final AnimationState walkingAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState fleeingAnimationState = new AnimationState();

    public BanditEntity(EntityType<? extends Animal> ignored, Level level) {
        super(EntityRegistry.BANDIT.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new BanditStealGoal(this, 0.8));
        this.goalSelector.addGoal(4,
                new BanditFleeGoal(this, 10, 1.1, 1.4));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 15)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_STATE, "idle");
    }

    public void resetAnimationStates() {
        this.stealingAnimationState.stop();
        this.walkingAnimationState.stop();
        this.idleAnimationState.stop();
        this.fleeingAnimationState.stop();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_STATE.equals(accessor)) {
            resetAnimationStates();
            String state = this.getEntityData().get(DATA_STATE);
            switch (state) {
                case "stealing" -> this.stealingAnimationState.startIfStopped(tickCount);
                case "walking" -> this.walkingAnimationState.startIfStopped(tickCount);
                case "idle" -> this.idleAnimationState.startIfStopped(tickCount);
                case "fleeing" -> this.fleeingAnimationState.startIfStopped(tickCount);
            }

            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public void tick() {
        super.tick();

        if (getTarget() instanceof Player player && isStealing() && stealingAnimationState.getTimeInMillis(tickCount) / 50 == 10) {
            stealItemFromPlayer(player);
        }
    }

    public void stealItemFromPlayer(Player player) {
        for (int i = random.nextIntBetweenInclusive(0, player.getInventory().getContainerSize() - 1); i < player.getInventory().getContainerSize(); i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (!itemStack.isEmpty()) {
                ItemStack stolenItem = itemStack.split(1);
                this.setItemInHand(InteractionHand.MAIN_HAND, stolenItem);
                break;
            }
        }
    }

    @Override
    public void die(DamageSource damageSource) {
        if (!level().isClientSide) {
            ItemStack heldItem = this.getItemInHand(InteractionHand.MAIN_HAND);
            if (!heldItem.isEmpty()) {
                drop(heldItem, false, true);
            }
        }
        super.die(damageSource);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.FOX_FOOD);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return EntityRegistry.BANDIT.get().create(serverLevel, EntitySpawnReason.BREEDING);
    }

    public boolean isStealing() {
        return this.getEntityData().get(DATA_STATE).equals("stealing");
    }

    public void setStealing() {
        this.getEntityData().set(DATA_STATE, "stealing");
    }

    public boolean isFleeing() {
        return this.getEntityData().get(DATA_STATE).equals("fleeing");
    }

    public void setFleeing() {
        this.getEntityData().set(DATA_STATE, "fleeing");
    }

    public boolean isIdle() {
        return this.getEntityData().get(DATA_STATE).equals("idle");
    }

    public void setIdle() {
        this.getEntityData().set(DATA_STATE, "idle");
    }

    public String getCurrentState() {
        return this.getEntityData().get(DATA_STATE);
    }

    @Override
    public int getBaseOuterTempIncrease() {
        return 2;
    }
}
