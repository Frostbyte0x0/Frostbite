package org.exodusstudio.frostbite.common.entity.custom.animals;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WoollySheepEntity extends Animal {
    private static final int EAT_ANIMATION_TICKS = 40;
    private static final EntityDataAccessor<Byte> DATA_WOOL_ID;
    private static final Map<DyeColor, Integer> COLOR_BY_DYE;
    private int eatAnimationTick;
    private EatBlockGoal eatBlockGoal;

    public WoollySheepEntity(EntityType<? extends Animal> p_29806_, Level p_29807_) {
        super(p_29806_, p_29807_);
    }

    private static int createSheepColor(DyeColor dyeColor) {
        if (dyeColor == DyeColor.WHITE) {
            return -1644826;
        } else {
            int i = dyeColor.getTextureDiffuseColor();
            return ARGB.color(255, Mth.floor((float)ARGB.red(i) * 0.75F), Mth.floor((float)ARGB.green(i) * 0.75F), Mth.floor((float)ARGB.blue(i) * 0.75F));
        }
    }

    public static int getColor(DyeColor dyeColor) {
        return COLOR_BY_DYE.get(dyeColor);
    }

    protected void registerGoals() {
        this.eatBlockGoal = new EatBlockGoal(this);
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25F));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0F));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.1, (p_335259_) -> p_335259_.is(ItemTags.SHEEP_FOOD), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, this.eatBlockGoal);
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    public boolean isFood(ItemStack p_335372_) {
        return p_335372_.is(ItemTags.SHEEP_FOOD);
    }

    protected void customServerAiStep(ServerLevel p_376791_) {
        this.eatAnimationTick = this.eatBlockGoal.getEatAnimationTick();
        super.customServerAiStep(p_376791_);
    }

    public void aiStep() {
        if (this.level().isClientSide) {
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        }

        super.aiStep();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 12.0F).add(Attributes.MOVEMENT_SPEED, 0.23F);
    }

    protected void defineSynchedData(SynchedEntityData.Builder p_325952_) {
        super.defineSynchedData(p_325952_);
        p_325952_.define(DATA_WOOL_ID, (byte)0);
    }

    public void handleEntityEvent(byte p_29814_) {
        if (p_29814_ == 10) {
            this.eatAnimationTick = 40;
        } else {
            super.handleEntityEvent(p_29814_);
        }

    }

    public float getHeadEatPositionScale(float partialTick) {
        if (this.eatAnimationTick <= 0) {
            return 0.0F;
        } else if (this.eatAnimationTick >= 4 && this.eatAnimationTick <= 36) {
            return 1.0F;
        } else {
            return this.eatAnimationTick < 4 ? ((float)this.eatAnimationTick - partialTick) / 4.0F : -((float)(this.eatAnimationTick - 40) - partialTick) / 4.0F;
        }
    }

    public float getHeadEatAngleScale(float partialTick) {
        if (this.eatAnimationTick > 4 && this.eatAnimationTick <= 36) {
            float f = ((float)(this.eatAnimationTick - 4) - partialTick) / 32.0F;
            return ((float)Math.PI / 5F) + 0.21991149F * Mth.sin(f * 28.7F);
        } else {
            return this.eatAnimationTick > 0 ? ((float)Math.PI / 5F) : this.getXRot() * ((float)Math.PI / 180F);
        }
    }

    public void shear(ServerLevel p_376571_, SoundSource p_29819_, ItemStack p_372915_) {
        p_376571_.playSound(null, this, SoundEvents.SHEEP_SHEAR, p_29819_, 1.0F, 1.0F);
        this.dropFromShearingLootTable(p_376571_, BuiltInLootTables.SHEAR_SHEEP, p_372915_, (p_375819_, p_375820_) -> {
            for(int i = 0; i < p_375820_.getCount(); ++i) {
                ItemEntity itementity = this.spawnAtLocation(p_375819_, p_375820_.copyWithCount(1), 1.0F);
                if (itementity != null) {
                    itementity.setDeltaMovement(itementity.getDeltaMovement().add(((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (this.random.nextFloat() * 0.05F), ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
                }
            }

        });
        this.setSheared(true);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(Items.SHEARS)) { // Neo: Shear logic is handled by IShearable
            if (this.level() instanceof ServerLevel serverlevel && this.readyForShearing()) {
                this.shear(serverlevel, SoundSource.PLAYERS, itemstack);
                this.gameEvent(GameEvent.SHEAR, player);
                itemstack.hurtAndBreak(1, player, getSlotForHand(hand));
                return InteractionResult.SUCCESS_SERVER;
            }

            return InteractionResult.CONSUME;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    public boolean readyForShearing() {
        return this.isAlive() && !this.isSheared() && !this.isBaby();
    }

    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Sheared", this.isSheared());
        output.putByte("Color", (byte)this.getColor().getId());
    }

    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setSheared(input.getBooleanOr("Sheared", false));
        this.setColor(DyeColor.byId(input.getByteOr("Color", (byte) 0)));
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SHEEP_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SHEEP_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SHEEP_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(SoundEvents.SHEEP_STEP, 0.15F, 1.0F);
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(DATA_WOOL_ID) & 15);
    }

    public void setColor(DyeColor dyeColor) {
        byte b0 = this.entityData.get(DATA_WOOL_ID);
        this.entityData.set(DATA_WOOL_ID, (byte)(b0 & 240 | dyeColor.getId() & 15));
    }

    public boolean isSheared() {
        return (this.entityData.get(DATA_WOOL_ID) & 16) != 0;
    }

    public void setSheared(boolean sheared) {
        byte b0 = this.entityData.get(DATA_WOOL_ID);
        if (sheared) {
            this.entityData.set(DATA_WOOL_ID, (byte)(b0 | 16));
        } else {
            this.entityData.set(DATA_WOOL_ID, (byte)(b0 & -17));
        }

    }

    public static DyeColor getRandomSheepColor(RandomSource random) {
        int i = random.nextInt(100);
        if (i < 5) {
            return DyeColor.BLACK;
        } else if (i < 10) {
            return DyeColor.GRAY;
        } else if (i < 15) {
            return DyeColor.LIGHT_GRAY;
        } else if (i < 18) {
            return DyeColor.BROWN;
        } else {
            return random.nextInt(500) == 0 ? DyeColor.PINK : DyeColor.WHITE;
        }
    }

    @javax.annotation.Nullable
    public WoollySheepEntity getBreedOffspring(ServerLevel p_149044_, AgeableMob p_149045_) {
        WoollySheepEntity sheep = EntityRegistry.WOOLLY_SHEEP.get().create(p_149044_, EntitySpawnReason.BREEDING);
        if (sheep != null) {
            DyeColor dyecolor = this.getColor();
            DyeColor dyecolor1 = ((WoollySheepEntity)p_149045_).getColor();
            sheep.setColor(DyeColor.getMixedColor(p_149044_, dyecolor, dyecolor1));
        }

        return sheep;
    }

    public void ate() {
        super.ate();
        this.setSheared(false);
        if (this.isBaby()) {
            this.ageUp(60);
        }

    }

    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_29835_, DifficultyInstance p_29836_, EntitySpawnReason p_360585_, @javax.annotation.Nullable SpawnGroupData p_29838_) {
        this.setColor(getRandomSheepColor(p_29835_.getRandom()));
        return super.finalizeSpawn(p_29835_, p_29836_, p_360585_, p_29838_);
    }

    static {
        DATA_WOOL_ID = SynchedEntityData.defineId(WoollySheepEntity.class, EntityDataSerializers.BYTE);
        COLOR_BY_DYE = Maps.newEnumMap((Map) Arrays.stream(DyeColor.values()).collect(Collectors.toMap(Function.identity(), WoollySheepEntity::createSheepColor)));
    }
}
