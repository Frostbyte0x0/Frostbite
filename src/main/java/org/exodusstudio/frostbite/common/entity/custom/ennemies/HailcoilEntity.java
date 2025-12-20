package org.exodusstudio.frostbite.common.entity.custom.ennemies;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.exodusstudio.frostbite.common.entity.goals.*;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.Ownable;

import javax.annotation.Nullable;

import static org.exodusstudio.frostbite.common.util.Util.spawnParticleRandomly;

public class HailcoilEntity extends Monster implements Ownable {
    private boolean hasLimitedLife;
    private int limitedLifeTicks;
    @Nullable
    private EntityReference<LivingEntity> owner;

    public HailcoilEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.HAILCOIL.get(), level);
        this.moveControl = new FlyingMoveControl(this);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new FlyingChargeAttackGoal<>(this, 0.2f));
        this.goalSelector.addGoal(8, new FlyingRandomMoveGoal(this, 0.15f));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5.0F)
                .add(Attributes.FLYING_SPEED, 1F)
                .add(Attributes.MOVEMENT_SPEED, 0.1F)
                .add(Attributes.ATTACK_DAMAGE, 2.0F);
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel p_376894_) {
        return 0;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getInt("life_ticks").ifPresentOrElse(this::setLimitedLife, () -> this.hasLimitedLife = false);
        this.owner = EntityReference.read(input, "owner");
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        if (this.hasLimitedLife) {
            output.putInt("life_ticks", this.limitedLifeTicks);
        }

        EntityReference.store(this.owner, output, "owner");
    }

    public void setLimitedLife(int limitedLifeTicks) {
        this.hasLimitedLife = true;
        this.limitedLifeTicks = limitedLifeTicks;
    }

    @Nullable
    public LivingEntity getOwner() {
        return EntityReference.get(this.owner, this.level(), LivingEntity.class);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = EntityReference.of(owner);
    }

    @Override
    public void restoreFrom(Entity entity) {
        super.restoreFrom(entity);
        if (entity instanceof HailcoilEntity hailcoil) {
            this.owner = hailcoil.owner;
        }
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);

        if (this.hasLimitedLife && --this.limitedLifeTicks <= 0 && level() instanceof ServerLevel serverLevel) {
            this.limitedLifeTicks = 20;
            this.hurtServer(serverLevel, this.damageSources().starve(), 1.0F);
        }

        if (!this.isDeadOrDying()) {
            for (int i = 0; i < 2; ++i) {
                double d0 = this.getX() + (0.5D - this.getRandom().nextDouble()) * 0.2;
                double d1 = this.getY() + (0.5D - this.getRandom().nextDouble()) * 0.2;
                double d2 = this.getZ() + (0.5D - this.getRandom().nextDouble()) * 0.2;

                this.level().addAlwaysVisibleParticle(
                        ParticleTypes.SOUL_FIRE_FLAME,
                        d0, d1, d2,
                        (0.5 - this.getRandom().nextDouble()) * 0.055 + this.getDeltaMovement().x,
                        (0.5 - this.getRandom().nextDouble()) * 0.055 + this.getDeltaMovement().y,
                        (0.5 - this.getRandom().nextDouble()) * 0.055 + this.getDeltaMovement().z);
            }
        } else {
            for (int i = 0; i < 5; ++i) {
                double d0 = this.getX() + (0.5D - this.getRandom().nextDouble()) * 0.2;
                double d1 = this.getY() + (0.5D - this.getRandom().nextDouble()) * 0.2;
                double d2 = this.getZ() + (0.5D - this.getRandom().nextDouble()) * 0.2;

                this.level().addAlwaysVisibleParticle(
                        ParticleTypes.FLAME,
                        d0, d1, d2,
                        (0.5 - this.getRandom().nextDouble()) * 0.055,
                        (0.5 - this.getRandom().nextDouble()) * 0.055,
                        (0.5 - this.getRandom().nextDouble()) * 0.055);
            }
        }

        if (this.getRandom().nextBoolean()) {
            spawnParticleRandomly(this, ParticleTypes.SOUL, 0.2, 0.055);
        } else {
            double d0 = this.getX() + (0.5D - this.getRandom().nextDouble()) * 0.2;
            double d1 = this.getY() + (0.5D - this.getRandom().nextDouble()) * 0.2;
            double d2 = this.getZ() + (0.5D - this.getRandom().nextDouble()) * 0.2;

            this.level().addAlwaysVisibleParticle(
                    ParticleTypes.SOUL,
                    d0, d1, d2,
                    (0.5 - this.getRandom().nextDouble()) * 0.03,
                    (this.getRandom().nextDouble()) * 0.1,
                    (0.5 - this.getRandom().nextDouble()) * 0.03);
        }

        if (this.tickCount % 10 == 0) {
            spawnParticleRandomly(this, ParticleTypes.FALLING_HONEY, 0.5, 0);
        }
    }
}
