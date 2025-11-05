package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.jetbrains.annotations.Nullable;

public class TanukiEntity extends Animal {
    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS =
            SynchedEntityData.defineId(TanukiEntity.class, EntityDataSerializers.BYTE);
    private float sitAmount;
    private float sitAmountO;

    public TanukiEntity(EntityType<? extends Animal> ignored, Level level) {
        super(EntityRegistry.TANUKI.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_FLAGS, (byte)0);
    }

    @Override
    public void tick() {
        super.tick();
        this.updateSitAmount();
    }

    private void updateSitAmount() {
        this.sitAmountO = this.sitAmount;
        if (isSitting()) {
            this.sitAmount = Math.min(1.0F, this.sitAmount + 0.15F);
        } else {
            this.sitAmount = Math.max(0.0F, this.sitAmount - 0.19F);
        }

    }

    public void tryToSit() {
        if (!this.isInWater()) {
            this.setZza(0.0F);
            this.getNavigation().stop();
            this.sit(true);
        }

    }

    public float getSitAmount(float partialTick) {
        return Mth.lerp(partialTick, this.sitAmountO, this.sitAmount);
    }

    public boolean isSitting() {
        return this.getFlag(8);
    }

    public void sit(boolean sitting) {
        this.setFlag(8, sitting);
    }

    private boolean getFlag(int flag) {
        return (this.entityData.get(DATA_ID_FLAGS) & flag) != 0;
    }

    private void setFlag(int flagId, boolean value) {
        byte b0 = this.entityData.get(DATA_ID_FLAGS);
        if (value) {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 | flagId));
        } else {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b0 & ~flagId));
        }

    }

    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.FOX_FOOD);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
