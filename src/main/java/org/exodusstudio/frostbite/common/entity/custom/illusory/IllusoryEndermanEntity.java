package org.exodusstudio.frostbite.common.entity.custom.illusory;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class IllusoryEndermanEntity extends EnderMan {
    public IllusoryEndermanEntity(EntityType<? extends EnderMan> p_32485_, Level p_32486_) {
        super(p_32485_, p_32486_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0F, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0F, 0.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0F)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_DAMAGE, 0.0F)
                .add(Attributes.FOLLOW_RANGE, 120.0F)
                .add(Attributes.STEP_HEIGHT, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isCreepy() ? SoundEvents.ENDERMAN_SCREAM : SoundEvents.EMPTY;
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
    protected void dropCustomDeathLoot(ServerLevel p_348556_, DamageSource p_32497_, boolean p_32499_) {    }

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
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel p_376355_) {
        return 0;
    }
}
