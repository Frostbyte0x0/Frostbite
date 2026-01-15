package org.exodusstudio.frostbite.common.entity.custom.elves;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.entity.custom.helper.StateMonsterEntity;
import org.exodusstudio.frostbite.common.entity.goals.ActionStrollGoal;
import org.exodusstudio.frostbite.common.entity.goals.ElfHuddleAroundHealerGoal;
import org.exodusstudio.frostbite.common.item.weapons.elf.ModeWeapon;

import java.util.Optional;

public abstract class ElfEntity extends StateMonsterEntity implements RangedAttackMob {
    private static final EntityDataAccessor<Integer> DATA_COOLDOWN_TICKS =
            SynchedEntityData.defineId(ElfEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Optional<BlockPos>> DATA_HEALER_POSITION =
            SynchedEntityData.defineId(ElfEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
    protected final int cooldownTicks;
    public final AnimationState jumpingAnimationState = new AnimationState();
    public static final int BLEND_TICKS = 10;

    protected ElfEntity(EntityType<? extends Monster> type, Level level, int cooldownTicks) {
        super(type, level, BLEND_TICKS);
        this.cooldownTicks = cooldownTicks;
        setIdle();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ElfHuddleAroundHealerGoal(this, 1.3f));
        this.goalSelector.addGoal(5, new ActionStrollGoal(this, 0.8, ElfEntity::setWalking, ElfEntity::setIdle));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_COOLDOWN_TICKS, 0);
        builder.define(DATA_HEALER_POSITION, Optional.empty());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 50)
                .add(Attributes.FOLLOW_RANGE, 15)
                .add(Attributes.MOVEMENT_SPEED, 0.3);
    }

    @Override
    public void tick() {
        super.tick();

        if (isAttacking()) {
            if (getTicksSinceLastChange() >= 20) {
                performRangedAttack(this, 0);
                level().playSound(null, this.blockPosition(), SoundEvents.EVOKER_PREPARE_WOLOLO, this.getSoundSource(),
                        1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
            }
        }

        setCooldownTicks(Math.max(0, getCooldownTicks() - 1));

        if (getHealth() > 0.8 * getMaxHealth()) {
            setHealerPosition(null);
        }

        if (getTarget() != null && getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ModeWeapon staff) {
            if (getTarget().distanceTo(this) < 5) {
                staff.setFirstMode();
            } else {
                staff.setSecondMode();
            }
        }

        if (isJumping()) {
            jumpingAnimationState.startIfStopped(tickCount);
        } else {
            jumpingAnimationState.stop();
        }
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel serverLevel, DamageSource p_21385_, boolean p_21387_) {
        super.dropCustomDeathLoot(serverLevel, p_21385_, p_21387_);
        if (random.nextFloat() < 0.1f) this.spawnAtLocation(serverLevel, this.equipment.get(EquipmentSlot.MAINHAND));
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        if (getCooldownTicks() > 0) return;

        ItemStack itemInHand = getItemInHand(InteractionHand.MAIN_HAND);
        if (itemInHand.getItem() instanceof ModeWeapon staff) {
            staff.attack(level(), this);
            staff.attack(Minecraft.getInstance().level, (LivingEntity) Minecraft.getInstance().level.getEntity(uuid));
        }

        setIdle();
        setCooldownTicks(cooldownTicks);
    }

    public void tryStartAttacking() {
        if (getCooldownTicks() > 0 || isAttacking()) return;
        setAttacking();
    }

    public boolean isAttacking() {
        return getCurrentState().equals("attacking");
    }

    public void setAttacking() {
        setLastState(getCurrentState());
        setCurrentState("attacking");
    }

    public boolean isWalking() {
        return getCurrentState().equals("walking");
    }

    public void setWalking() {
        setLastState(getCurrentState());
        setCurrentState("walking");
    }

    public boolean isJumping() {
        return getDeltaMovement().y > 0 && !onGround();
    }

    public static void setWalking(PathfinderMob mob) {
        if (mob instanceof ElfEntity elf) elf.setWalking();
    }

    public static void setIdle(PathfinderMob mob) {
        if (mob instanceof ElfEntity elf) elf.setIdle();
    }

    public void setCooldownTicks(int ticks) {
        this.entityData.set(DATA_COOLDOWN_TICKS, ticks);
    }

    public int getCooldownTicks() {
        return this.entityData.get(DATA_COOLDOWN_TICKS);
    }

    public void setHealerPosition(BlockPos pos) {
        if (pos == null) {
            this.entityData.set(DATA_HEALER_POSITION, Optional.empty());
        } else {
            this.entityData.set(DATA_HEALER_POSITION, Optional.of(pos));
        }
    }

    public Optional<BlockPos> getHealerPosition() {
        return this.entityData.get(DATA_HEALER_POSITION);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
