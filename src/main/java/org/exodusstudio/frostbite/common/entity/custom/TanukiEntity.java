package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.goals.TanukiSitGoal;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.jetbrains.annotations.Nullable;

public class TanukiEntity extends Animal {
    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS =
            SynchedEntityData.defineId(TanukiEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> EAT_COUNTER =
            SynchedEntityData.defineId(TanukiEntity.class, EntityDataSerializers.INT);
    private float sitAmount;
    private float sitAmountO;

    public TanukiEntity(EntityType<? extends Animal> ignored, Level level) {
        super(EntityRegistry.TANUKI.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new TanukiSitGoal(this));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1, (stack) -> stack.is(ItemTags.FOX_FOOD), false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.TEMPT_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_ID_FLAGS, (byte)0);
        builder.define(EAT_COUNTER, 0);;
    }

    @Override
    public void tick() {
        super.tick();
        this.updateSitAmount();
        this.handleEating();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (this.isFood(itemstack)) {
            if (this.isBaby()) {
                this.usePlayerItem(player, hand, itemstack);
                this.ageUp((int)(-this.getAge() / 20 * 0.1F), true);
            } else if (!this.level().isClientSide && this.getAge() == 0 && this.canFallInLove()) {
                this.usePlayerItem(player, hand, itemstack);
                this.setInLove(player);
            } else {
                if (!(this.level() instanceof ServerLevel serverlevel) || this.isSitting() || this.isInWater()) {
                    return InteractionResult.PASS;
                }

                this.tryToSit();
                this.eat(true);
                ItemStack itemstack1 = this.getItemBySlot(EquipmentSlot.MAINHAND);
                if (!itemstack1.isEmpty() && !player.hasInfiniteMaterials()) {
                    this.spawnAtLocation(serverlevel, itemstack1);
                }

                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(itemstack.getItem(), 1));
                this.usePlayerItem(player, hand, itemstack);
            }

            return InteractionResult.SUCCESS_SERVER;
        } else {
            return InteractionResult.PASS;
        }
    }

    private void updateSitAmount() {
        this.sitAmountO = this.sitAmount;
        if (isSitting()) {
            this.sitAmount = Math.min(1.0F, this.sitAmount + 0.15F);
        } else {
            this.sitAmount = Math.max(0.0F, this.sitAmount - 0.19F);
        }

    }

    private void handleEating() {
        if (!this.isEating() && this.isSitting() && !this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && this.random.nextInt(80) == 1) {
            this.eat(true);
        } else if (this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() || !this.isSitting()) {
            this.eat(false);
        }

        if (this.isEating()) {
            this.addEatingParticles();
            if (!this.level().isClientSide && this.getEatCounter() > 80 && this.random.nextInt(20) == 1) {
                if (this.getEatCounter() > 100 && this.getItemBySlot(EquipmentSlot.MAINHAND).is(ItemTags.PANDA_EATS_FROM_GROUND)) {
                    if (!this.level().isClientSide) {
                        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        this.gameEvent(GameEvent.EAT);
                    }

                    this.sit(false);
                }

                this.eat(false);
                return;
            }

            this.setEatCounter(this.getEatCounter() + 1);
        }
    }

    private void addEatingParticles() {
        if (this.getEatCounter() % 5 == 0) {
            this.playSound(SoundEvents.PANDA_EAT, 0.5F + 0.5F * this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

            for (int i = 0; i < 6; i++) {
                Vec3 vec3 = new Vec3((this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, (this.random.nextFloat() - 0.5) * 0.1);
                vec3 = vec3.xRot(-this.getXRot() * (float) (Math.PI / 180.0));
                vec3 = vec3.yRot(-this.getYRot() * (float) (Math.PI / 180.0));
                double d0 = -this.random.nextFloat() * 0.6 - 0.3;
                Vec3 vec31 = new Vec3((this.random.nextFloat() - 0.5) * 0.8, d0, 1.0 + (this.random.nextFloat() - 0.5) * 0.4);
                vec31 = vec31.yRot(-this.yBodyRot * (float) (Math.PI / 180.0));
                vec31 = vec31.add(this.getX(), this.getEyeY() + 1.0, this.getZ());
                this.level()
                        .addParticle(
                                new ItemParticleOption(ParticleTypes.ITEM, this.getItemBySlot(EquipmentSlot.MAINHAND)),
                                vec31.x,
                                vec31.y,
                                vec31.z,
                                vec3.x,
                                vec3.y + 0.05,
                                vec3.z
                        );
            }
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

    public boolean isEating() {
        return this.entityData.get(EAT_COUNTER) > 0;
    }

    public void eat(boolean eating) {
        this.entityData.set(EAT_COUNTER, eating ? 1 : 0);
    }

    private int getEatCounter() {
        return this.entityData.get(EAT_COUNTER);
    }

    private void setEatCounter(int eatCounter) {
        this.entityData.set(EAT_COUNTER, eatCounter);
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
