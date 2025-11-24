package org.exodusstudio.frostbite.common.entity.custom.ennemies;

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
import org.exodusstudio.frostbite.common.entity.goals.BanditFleeGoal;
import org.exodusstudio.frostbite.common.entity.goals.BanditStealGoal;
import org.exodusstudio.frostbite.common.entity.goals.BanditStrollGoal;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.CustomTemperatureEntity;
import org.jetbrains.annotations.Nullable;

public class BanditEntity extends Animal implements CustomTemperatureEntity {
    private static final EntityDataAccessor<String> DATA_LAST_STATE =
            SynchedEntityData.defineId(BanditEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_STATE =
            SynchedEntityData.defineId(BanditEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_TICKS_SINCE_LAST_CHANGE =
            SynchedEntityData.defineId(BanditEntity.class, EntityDataSerializers.INT);
    public final AnimationState currentAnimationState = new AnimationState();
    public final AnimationState lastAnimationState = new AnimationState();
    public static final int BLEND_TICKS = 10;

    public BanditEntity(EntityType<? extends Animal> ignored, Level level) {
        super(EntityRegistry.BANDIT.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new BanditStealGoal(this, 0.8));
        this.goalSelector.addGoal(4,
                new BanditFleeGoal(this, 10, 1.1, 1.4));
        this.goalSelector.addGoal(5, new BanditStrollGoal(this, 0.8));
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
        builder.define(DATA_LAST_STATE, "idle");
        builder.define(DATA_TICKS_SINCE_LAST_CHANGE, BLEND_TICKS);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_STATE.equals(accessor)) {
            this.lastAnimationState.copyFrom(currentAnimationState);
            this.currentAnimationState.stop();
            this.currentAnimationState.startIfStopped(tickCount);
            setTicksSinceLastChange(0);
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public void tick() {
        super.tick();

        if (getTarget() instanceof Player player && isStealing() && currentAnimationState.getTimeInMillis(tickCount) / 50 == 10) {
            stealItemFromPlayer(player);
        }

        setTicksSinceLastChange(getTicksSinceLastChange() + 1);

        if (getTicksSinceLastChange() > BLEND_TICKS) {
            lastAnimationState.stop();
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
        this.getEntityData().set(DATA_LAST_STATE, getCurrentState());
        this.getEntityData().set(DATA_STATE, "stealing");
    }

    public boolean isFleeing() {
        return this.getEntityData().get(DATA_STATE).equals("fleeing");
    }

    public void setFleeing() {
        this.getEntityData().set(DATA_LAST_STATE, getCurrentState());
        this.getEntityData().set(DATA_STATE, "fleeing");
    }

    public boolean isIdle() {
        return this.getEntityData().get(DATA_STATE).equals("idle");
    }

    public void setIdle() {
        this.getEntityData().set(DATA_LAST_STATE, getCurrentState());
        this.getEntityData().set(DATA_STATE, "idle");
    }

    public boolean isWalking() {
        return this.getEntityData().get(DATA_STATE).equals("walking");
    }

    public void setWalking() {
        this.getEntityData().set(DATA_LAST_STATE, getCurrentState());
        this.getEntityData().set(DATA_STATE, "walking");
    }

    public String getCurrentState() {
        return this.getEntityData().get(DATA_STATE);
    }

    public String getLastState() {
        return this.getEntityData().get(DATA_LAST_STATE);
    }

    public int getTicksSinceLastChange() {
        return this.getEntityData().get(DATA_TICKS_SINCE_LAST_CHANGE);
    }

    public void setTicksSinceLastChange(int ticks) {
        this.getEntityData().set(DATA_TICKS_SINCE_LAST_CHANGE, ticks);
    }

    @Override
    public int getBaseOuterTempIncrease() {
        return 2;
    }
}
