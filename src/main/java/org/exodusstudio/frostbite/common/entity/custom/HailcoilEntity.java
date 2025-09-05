package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

import static org.exodusstudio.frostbite.common.util.Util.spawnParticleRandomly;

public class HailcoilEntity extends Monster {
    public HailcoilEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new HailcoilEntityMoveControl(this);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(4, new HailcoilChargeAttackGoal());
        this.goalSelector.addGoal(8, new HailcoilRandomMoveGoal());
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
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
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);

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

    class HailcoilEntityMoveControl extends MoveControl {
        public HailcoilEntityMoveControl(HailcoilEntity hailcoilEntity) {
            super(hailcoilEntity);
        }

        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 vec3 = new Vec3(this.wantedX - HailcoilEntity.this.getX(), this.wantedY - HailcoilEntity.this.getY(), this.wantedZ - HailcoilEntity.this.getZ());
                double d0 = vec3.length();
                if (d0 < HailcoilEntity.this.getBoundingBox().getSize()) {
                    this.operation = Operation.WAIT;
                    HailcoilEntity.this.setDeltaMovement(HailcoilEntity.this.getDeltaMovement().scale(0.5F));
                } else {
                    HailcoilEntity.this.setDeltaMovement(HailcoilEntity.this.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.1 / d0)));
                    if (HailcoilEntity.this.getTarget() == null) {
                        Vec3 vec31 = HailcoilEntity.this.getDeltaMovement();
                        HailcoilEntity.this.setYRot(-((float) Mth.atan2(vec31.x, vec31.z)) * (180F / (float)Math.PI));
                    } else {
                        double d2 = HailcoilEntity.this.getTarget().getX() - HailcoilEntity.this.getX();
                        double d1 = HailcoilEntity.this.getTarget().getZ() - HailcoilEntity.this.getZ();
                        HailcoilEntity.this.setYRot(-((float)Mth.atan2(d2, d1)) * (180F / (float)Math.PI));
                    }
                    HailcoilEntity.this.yBodyRot = HailcoilEntity.this.getYRot();
                }
            }

        }
    }

    class HailcoilChargeAttackGoal extends Goal {
        public HailcoilChargeAttackGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            LivingEntity livingentity = HailcoilEntity.this.getTarget();
            return livingentity != null && livingentity.isAlive() && !HailcoilEntity.this.getMoveControl().hasWanted() && HailcoilEntity.this.random.nextInt(reducedTickDelay(7)) == 0 && HailcoilEntity.this.distanceToSqr(livingentity) > (double) 4.0F;
        }

        public boolean canContinueToUse() {
            return HailcoilEntity.this.getMoveControl().hasWanted() && HailcoilEntity.this.getTarget() != null && HailcoilEntity.this.getTarget().isAlive();
        }

        public void start() {
            LivingEntity livingentity = HailcoilEntity.this.getTarget();
            if (livingentity != null) {
                Vec3 vec3 = livingentity.getEyePosition();
                HailcoilEntity.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0F);
            }
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingentity = HailcoilEntity.this.getTarget();
            if (livingentity != null) {
                if (HailcoilEntity.this.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                    HailcoilEntity.this.doHurtTarget(getServerLevel(HailcoilEntity.this.level()), livingentity);
                } else {
                    double d0 = HailcoilEntity.this.distanceToSqr(livingentity);
                    if (d0 < (double)9.0F) {
                        Vec3 vec3 = livingentity.getEyePosition();
                        HailcoilEntity.this.moveControl.setWantedPosition(vec3.x, vec3.y, vec3.z, 0.3F);
                    }
                }
            }

        }
    }

    class HailcoilRandomMoveGoal extends Goal {
        public HailcoilRandomMoveGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return !HailcoilEntity.this.getMoveControl().hasWanted() && HailcoilEntity.this.random.nextInt(reducedTickDelay(7)) == 0;
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void tick() {
            BlockPos blockpos = HailcoilEntity.this.blockPosition();

            for (int i = 0; i < 3; ++i) {
                BlockPos blockpos1 = blockpos.offset(HailcoilEntity.this.random.nextInt(20) - 10, HailcoilEntity.this.random.nextInt(15) - 7, HailcoilEntity.this.random.nextInt(20) - 10);
                if (HailcoilEntity.this.level().isEmptyBlock(blockpos1)) {
                    HailcoilEntity.this.moveControl.setWantedPosition((double)blockpos1.getX() + (double)0.5F, (double)blockpos1.getY() + (double)0.5F, (double)blockpos1.getZ() + (double)0.5F, (double)0.25F);
                    if (HailcoilEntity.this.getTarget() == null) {
                        HailcoilEntity.this.getLookControl().setLookAt((double)blockpos1.getX() + (double)0.5F, (double)blockpos1.getY() + (double)0.5F, (double)blockpos1.getZ() + (double)0.5F, 180.0F, 20.0F);
                    }
                    break;
                }
            }

        }
    }
}
