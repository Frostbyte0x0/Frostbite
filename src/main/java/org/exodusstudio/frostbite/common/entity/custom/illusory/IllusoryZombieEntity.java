package org.exodusstudio.frostbite.common.entity.custom.illusory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

import javax.annotation.Nullable;
import java.time.LocalDate;

public class IllusoryZombieEntity extends Zombie {
    private static final EntityDimensions BABY_DIMENSIONS;
    protected int conversionTime;

    public IllusoryZombieEntity(EntityType<? extends Entity> entityType, Level level) {
        super(EntityRegistry.ILLUSORY_ZOMBIE.get(), level);
        this.setCanBreakDoors(false);
    }

    public IllusoryZombieEntity(Level level) {
        this(EntityRegistry.ILLUSORY_ZOMBIE.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0F, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 120.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, 0.0F)
                .add(Attributes.ARMOR, 2.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder p_326435_) {
        super.defineSynchedData(p_326435_);
    }

    @Override
    public boolean canBreakDoors() {
        return false;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel p_376355_) {
        return 0;
    }

    @Override
    protected boolean convertsInWater() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void aiStep() {
        if (this.isAlive()) {
            boolean flag = this.isSunSensitive() && this.isSunBurnTick();
            if (flag) {
                ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        Item item = itemstack.getItem();
                        itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            this.onEquippedItemBroken(item, EquipmentSlot.HEAD);
                            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    this.igniteForSeconds(8.0F);
                }
            }
        }

        super.aiStep();
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float damage) {
        if (this.getTarget() != null && damageSource.getEntity() != null && (damageSource.getEntity().is(this.getTarget()) || damage >= this.getHealth())) {
            IllusoryEntity.revealIllusion(this);
        }
        return true;
    }

    @Override
    public boolean doHurtTarget(ServerLevel p_376343_, Entity p_34276_) {
        IllusoryEntity.revealIllusion(this);
        return true;
    }


    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.EMPTY;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.EMPTY;
    }

    @Override
    protected boolean canSpawnInLiquids() {
        return false;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource p_219165_, DifficultyInstance p_219166_) {
        super.populateDefaultEquipmentSlots(p_219165_, p_219166_);
        if (p_219165_.nextFloat() < (this.level().getDifficulty() == Difficulty.HARD ? 0.05F : 0.01F)) {
            int i = p_219165_.nextInt(3);
            if (i == 0) {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }

    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsBaby", this.isBaby());
        compound.putBoolean("CanBreakDoors", this.canBreakDoors());
        compound.putInt("DrownedConversionTime", this.isUnderWaterConverting() ? this.conversionTime : -1);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBaby(compound.getBoolean("IsBaby"));
        this.setCanBreakDoors(false);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose p_316771_) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(p_316771_);
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        return false;
    }

    @Override
    public boolean wantsToPickUp(ServerLevel p_376535_, ItemStack p_182400_) {
        return false;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34297_, DifficultyInstance p_34298_, EntitySpawnReason p_362602_, @Nullable SpawnGroupData p_34300_) {
        RandomSource randomsource = p_34297_.getRandom();
        p_34300_ = super.finalizeSpawn(p_34297_, p_34298_, p_362602_, p_34300_);
        this.setCanPickUpLoot(false);

        if (p_34300_ == null) {
            p_34300_ = new net.minecraft.world.entity.monster.Zombie.ZombieGroupData(getSpawnAsBabyOdds(randomsource), false);
        }

        if (p_34300_ instanceof net.minecraft.world.entity.monster.Zombie.ZombieGroupData zombie$zombiegroupdata) {
            this.setCanBreakDoors(false);
            if (p_362602_ != EntitySpawnReason.CONVERSION) {
                this.populateDefaultEquipmentSlots(randomsource, p_34298_);
                this.populateDefaultEquipmentEnchantments(p_34297_, randomsource, p_34298_);
            }
        }

        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
            LocalDate localdate = LocalDate.now();
            int i = localdate.getDayOfMonth();
            int j = localdate.getMonth().getValue();
            if (j == 10 && i == 31 && randomsource.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(randomsource.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        return p_34300_;
    }

    @Override
    protected void handleAttributes(float difficulty) {
    }

    static {
        BABY_DIMENSIONS = EntityType.ZOMBIE.getDimensions().scale(0.5F).withEyeHeight(0.93F);
    }
}

