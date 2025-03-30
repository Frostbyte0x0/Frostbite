package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class IllusoryZombieEntity extends Zombie {
    private static final ResourceLocation LEADER_ZOMBIE_BONUS_ID;
    private static final ResourceLocation ZOMBIE_RANDOM_SPAWN_BONUS_ID;
    private static final EntityDimensions BABY_DIMENSIONS;
    private int inWaterTime;
    protected int conversionTime;

    public IllusoryZombieEntity(EntityType<? extends Entity> entityType, Level level) {
        super(EntityRegistry.ILLUSORY_ZOMBIE.get(), level);
    }

    public IllusoryZombieEntity(Level level) {
        this(EntityRegistry.ILLUSORY_ZOMBIE.get(), level);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, (double)1.0F, false));
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, (double)1.0F, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, (double)1.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, new Class[0])).setAlertOthers(new Class[]{ZombifiedPiglin.class}));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, (double)35.0F).add(Attributes.MOVEMENT_SPEED, (double)0.23F).add(Attributes.ATTACK_DAMAGE, (double)3.0F).add(Attributes.ARMOR, (double)2.0F).add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    protected void defineSynchedData(SynchedEntityData.Builder p_326435_) {
        super.defineSynchedData(p_326435_);
    }

    public boolean canBreakDoors() {
        return false;
    }

    protected int getBaseExperienceReward(ServerLevel p_376355_) {
        return 0;
    }

    protected boolean convertsInWater() {
        return false;
    }


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

    public boolean hurtServer(ServerLevel p_376886_, DamageSource p_376124_, float p_376398_) {
        if (!super.hurtServer(p_376886_, p_376124_, p_376398_)) {
            return false;
        } else {
            LivingEntity livingentity = this.getTarget();
            if (livingentity == null && p_376124_.getEntity() instanceof LivingEntity) {
                livingentity = (LivingEntity)p_376124_.getEntity();
            }

            if (livingentity != null && p_376886_.getDifficulty() == Difficulty.HARD && (double)this.random.nextFloat() < this.getAttributeValue(Attributes.SPAWN_REINFORCEMENTS_CHANCE) && p_376886_.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                int i = Mth.floor(this.getX());
                int j = Mth.floor(this.getY());
                int k = Mth.floor(this.getZ());
                EntityType<? extends net.minecraft.world.entity.monster.Zombie> entitytype = this.getType();
                net.minecraft.world.entity.monster.Zombie zombie = (net.minecraft.world.entity.monster.Zombie)entitytype.create(p_376886_, EntitySpawnReason.REINFORCEMENT);
                if (zombie == null) {
                    return true;
                }

                for(int l = 0; l < 50; ++l) {
                    int i1 = i + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
                    int j1 = j + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
                    int k1 = k + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
                    BlockPos blockpos = new BlockPos(i1, j1, k1);
                    if (SpawnPlacements.isSpawnPositionOk(entitytype, p_376886_, blockpos) && SpawnPlacements.checkSpawnRules(entitytype, p_376886_, EntitySpawnReason.REINFORCEMENT, blockpos, p_376886_.random)) {
                        zombie.setPos((double)i1, (double)j1, (double)k1);
                        if (!p_376886_.hasNearbyAlivePlayer((double)i1, (double)j1, (double)k1, (double)7.0F) && p_376886_.isUnobstructed(zombie) && p_376886_.noCollision(zombie)) {
                            zombie.setTarget(livingentity);
                            zombie.finalizeSpawn(p_376886_, p_376886_.getCurrentDifficultyAt(zombie.blockPosition()), EntitySpawnReason.REINFORCEMENT, (SpawnGroupData)null);
                            p_376886_.addFreshEntityWithPassengers(zombie);
                            break;
                        }
                    }
                }
            }

            return true;
        }
    }

    public boolean doHurtTarget(ServerLevel p_376343_, Entity p_34276_) {
        boolean flag = super.doHurtTarget(p_376343_, p_34276_);
        if (flag) {
            float f = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
                p_34276_.igniteForSeconds((float)(2 * (int)f));
            }
        }

        return flag;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ZOMBIE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_STEP;
    }

    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    public EntityType<? extends net.minecraft.world.entity.monster.Zombie> getType() {
        return super.getType();
    }

    protected boolean canSpawnInLiquids() {
        return false;
    }

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

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsBaby", this.isBaby());
        compound.putBoolean("CanBreakDoors", this.canBreakDoors());
        compound.putInt("InWaterTime", this.isInWater() ? this.inWaterTime : -1);
        compound.putInt("DrownedConversionTime", this.isUnderWaterConverting() ? this.conversionTime : -1);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBaby(compound.getBoolean("IsBaby"));
        this.setCanBreakDoors(false);
        this.inWaterTime = compound.getInt("InWaterTime");
    }

    public EntityDimensions getDefaultDimensions(Pose p_316771_) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(p_316771_);
    }

    public boolean canHoldItem(ItemStack stack) {
        return false;
    }

    public boolean wantsToPickUp(ServerLevel p_376535_, ItemStack p_182400_) {
        return false;
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_34297_, DifficultyInstance p_34298_, EntitySpawnReason p_362602_, @Nullable SpawnGroupData p_34300_) {
        RandomSource randomsource = p_34297_.getRandom();
        p_34300_ = super.finalizeSpawn(p_34297_, p_34298_, p_362602_, p_34300_);
        float f = p_34298_.getSpecialMultiplier();
        this.setCanPickUpLoot(false);

        if (p_34300_ == null) {
            p_34300_ = new net.minecraft.world.entity.monster.Zombie.ZombieGroupData(getSpawnAsBabyOdds(randomsource), true);
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
            int i = localdate.get(ChronoField.DAY_OF_MONTH);
            int j = localdate.get(ChronoField.MONTH_OF_YEAR);
            if (j == 10 && i == 31 && randomsource.nextFloat() < 0.25F) {
                this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(randomsource.nextFloat() < 0.1F ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
                this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.0F;
            }
        }

        this.handleAttributes(f);
        return p_34300_;
    }

    protected void handleAttributes(float difficulty) {
        this.getAttribute(Attributes.FOLLOW_RANGE).addOrReplacePermanentModifier(new AttributeModifier(ZOMBIE_RANDOM_SPAWN_BONUS_ID, 5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addOrReplacePermanentModifier(new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, 0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    protected ItemStack getSkull() {
        return new ItemStack(Items.ZOMBIE_HEAD);
    }

    static {
        LEADER_ZOMBIE_BONUS_ID = ResourceLocation.withDefaultNamespace("leader_zombie_bonus");
        ZOMBIE_RANDOM_SPAWN_BONUS_ID = ResourceLocation.withDefaultNamespace("zombie_random_spawn_bonus");
        BABY_DIMENSIONS = EntityType.ZOMBIE.getDimensions().scale(0.5F).withEyeHeight(0.93F);
    }
}

