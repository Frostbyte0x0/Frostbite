package org.exodusstudio.frostbite.common.entity.custom.animals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;
import org.jetbrains.annotations.Nullable;

public class LevitatingJellyfishEntity extends AgeableWaterCreature {
    private static final int MAX_DISTANCE_ABOVE_GROUND = 6;
    private static final EntityDataAccessor<Integer> DATA_MOVE_COOLDOWN;
    public final AnimationState swimmingAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public float rotateSpeed;
    public Vec3 movementVector;
    public float xBodyRot;
    public float xBodyRotO;
    public float zBodyRot;
    public float zBodyRotO;

    public LevitatingJellyfishEntity(EntityType<? extends LevitatingJellyfishEntity> type, Level level) {
        super(type, level);
        this.setPathfindingMalus(PathType.WATER, 10.0F);
        this.movementVector = Vec3.ZERO;
        this.rotateSpeed = 0;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder p_326308_) {
        super.defineSynchedData(p_326308_);
        p_326308_.define(DATA_MOVE_COOLDOWN, 0);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("MoveCooldown", this.getMoveCooldown());
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        if (input.getInt("MoveCooldown").isPresent()) {
            this.setMoveCooldown(input.getInt("MoveCooldown").get());
        }
    }

    public int getMoveCooldown() {
        return this.entityData.get(DATA_MOVE_COOLDOWN);
    }

    public void setMoveCooldown(int cooldown) {
        this.entityData.set(DATA_MOVE_COOLDOWN, cooldown);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LevitatingJellyfishRandomMovementGoal(this));
        this.goalSelector.addGoal(1, new LevitatingJellyfishFleeGoal());
    }

    @Override
    public void tick() {
        super.tick();
        //this.move(MoverType.SELF, this.getDeltaMovement());
        if (this.getMoveCooldown() == 50 && this.level().isClientSide()) {
            //this.swimmingAnimationState.start(this.tickCount);
        }
        this.setMoveCooldown(this.getMoveCooldown() + 1);
        idleAnimationState.startIfStopped(tickCount);
    }

    @Override
    public void playerTouch(Player player) {
        super.playerTouch(player);
        player.addEffect(new MobEffectInstance(EffectRegistry.TWITCHING, 9600));
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SQUID_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SQUID_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SQUID_DEATH;
    }

    protected SoundEvent getSquirtSound() {
        return SoundEvents.SQUID_SQUIRT;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        this.xBodyRotO = this.xBodyRot;
        this.zBodyRotO = this.zBodyRot;

        if (this.getMoveCooldown() <= 3) {
            if (this.canControlVehicle()) {
                this.setDeltaMovement(this.movementVector.scale(this.getMoveCooldown() / 3f));
            }

            this.rotateSpeed = 1.0F;
        } else {
            this.rotateSpeed *= 0.8F;
            if (this.canControlVehicle()) {
                this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            }
        }

        Vec3 vec3 = this.getDeltaMovement();
        double d0 = vec3.horizontalDistance();
        this.yBodyRot += (-((float)Mth.atan2(vec3.x, vec3.z)) * (180F / (float)Math.PI) - this.yBodyRot) * 0.1F;
        this.setYRot(this.yBodyRot);
        this.zBodyRot += (float)Math.PI * this.rotateSpeed * 1.5F;
        this.xBodyRot += (-((float)Mth.atan2(d0, vec3.y)) * (180F / (float)Math.PI) - this.xBodyRot) * 0.1F;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (source.getEntity() instanceof Player && random.nextFloat() < 0.25f) {
            ((Player) source.getEntity()).addEffect(new MobEffectInstance(EffectRegistry.TWITCHING, 2400));
        }

        if (super.hurtServer(level, source, damage) && this.getLastHurtByMob() != null) {
            this.spawnInk();
            return true;
        } else {
            return false;
        }
    }

    private Vec3 rotateVector(Vec3 vector) {
        Vec3 vec3 = vector.xRot(this.xBodyRotO * ((float)Math.PI / 180F));
        return vec3.yRot(-this.yBodyRotO * ((float)Math.PI / 180F));
    }

    private void spawnInk() {
        this.makeSound(this.getSquirtSound());
        Vec3 vec3 = this.rotateVector(new Vec3(0.0F, -1.0F, 0.0F)).add(this.getX(), this.getY(), this.getZ());

        for(int i = 0; i < 30; ++i) {
            Vec3 vec31 = this.rotateVector(new Vec3((double)this.random.nextFloat() * 0.6 - 0.3, -1.0F, (double)this.random.nextFloat() * 0.6 - 0.3));
            float f = this.isBaby() ? 0.1F : 0.3F;
            Vec3 vec32 = vec31.scale((f + this.random.nextFloat() * 2.0F));
            ((ServerLevel)this.level()).sendParticles(this.getInkParticle(), vec3.x, vec3.y + (double)0.5F, vec3.z, 0, vec32.x, vec32.y, vec32.z, 0.1F);
        }

    }

    protected ParticleOptions getInkParticle() {
        return ParticleTypes.SQUID_INK;
    }

    @Override
    protected double getDefaultGravity() {
        return 0;
    }

    @Override
    protected void handleAirSupply(int airSupply) {}

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    static {
        DATA_MOVE_COOLDOWN = SynchedEntityData.defineId(LevitatingJellyfishEntity.class, EntityDataSerializers.INT);
    }

    class LevitatingJellyfishFleeGoal extends Goal {
        private int fleeTicks;

        public boolean canUse() {
            LivingEntity livingentity = LevitatingJellyfishEntity.this.getLastHurtByMob();
            return livingentity != null && LevitatingJellyfishEntity.this.distanceToSqr(livingentity) < (double) 100.0F;
        }

        public void start() {
            this.fleeTicks = 0;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            ++this.fleeTicks;
            LivingEntity livingentity = LevitatingJellyfishEntity.this.getLastHurtByMob();
            if (livingentity != null) {
                Vec3 vec3 = new Vec3(LevitatingJellyfishEntity.this.getX() - livingentity.getX(), LevitatingJellyfishEntity.this.getY() - livingentity.getY(), LevitatingJellyfishEntity.this.getZ() - livingentity.getZ());
                FluidState fluidstate = LevitatingJellyfishEntity.this.level().getFluidState(BlockPos.containing(LevitatingJellyfishEntity.this.getX() + vec3.x, LevitatingJellyfishEntity.this.getY() + vec3.y, LevitatingJellyfishEntity.this.getZ() + vec3.z));

                double d0 = vec3.length();
                if (d0 > (double)0.0F) {
                    vec3.normalize();
                    double d1 = 2.0F;
                    if (d0 > (double)5.0F) {
                        d1 -= (d0 - (double)5.0F) / (double)5.0F;
                    }

                    if (d1 > (double)0.0F) {
                        vec3 = vec3.scale(d1);
                    }
                }

                if (fluidstate.is(FluidTags.WATER)) {
                    vec3 = vec3.subtract(0.0F, vec3.y, 0.0F);
                }

                LevitatingJellyfishEntity.this.movementVector = new Vec3(vec3.x / (double)20.0F, vec3.y / (double)20.0F, vec3.z / (double)20.0F);

                if (this.fleeTicks % 10 == 5) {
                    LevitatingJellyfishEntity.this.level().addParticle(ParticleTypes.BUBBLE, LevitatingJellyfishEntity.this.getX(), LevitatingJellyfishEntity.this.getY(), LevitatingJellyfishEntity.this.getZ(), 0.0F, 0.0F, 0.0F);
                }
            }

        }
    }

    static class LevitatingJellyfishRandomMovementGoal extends Goal {
        private final LevitatingJellyfishEntity jellyfish;

        public LevitatingJellyfishRandomMovementGoal(LevitatingJellyfishEntity p_30003_) {
            this.jellyfish = p_30003_;
        }

        public boolean canUse() {
            return true;
        }

        public void tick() {
            if (this.jellyfish.getMoveCooldown() >= 60) {
                this.jellyfish.setMoveCooldown(0);
                float f = this.jellyfish.getRandom().nextFloat() * ((float)Math.PI * 2F);
                boolean canMoveUp = false;
                Vec3 vec3 = new Vec3((Mth.cos(f) * 0.2F), (-0.1F + this.jellyfish.getRandom().nextFloat() * 0.2F), (Mth.sin(f) * 0.2F));

                for (int j = 1; j < MAX_DISTANCE_ABOVE_GROUND; j++) {
                    BlockState blockstate = jellyfish.level().getBlockState(BlockPos.containing(jellyfish.getX(), jellyfish.getY() - j, jellyfish.getZ()));
                    if (!blockstate.isAir()) {
                        canMoveUp = true;
                        break;
                    }
                }

                if (!canMoveUp) {
                    vec3 = vec3.multiply(1, -Math.abs(vec3.y) / vec3.y, 1);
                }

                FluidState fluidstate = jellyfish.level().getFluidState(BlockPos.containing(jellyfish.getX() + vec3.x, jellyfish.getY() + vec3.y, jellyfish.getZ() + vec3.z));
                if (fluidstate.is(FluidTags.WATER)) {
                    vec3 = vec3.subtract(0.0F, vec3.y, 0.0F);
                }

                this.jellyfish.movementVector = vec3;
            }

        }
    }
}
