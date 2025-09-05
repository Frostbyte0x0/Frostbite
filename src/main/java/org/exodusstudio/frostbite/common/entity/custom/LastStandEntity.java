package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static org.exodusstudio.frostbite.common.util.Util.calculateDir;
import static org.exodusstudio.frostbite.common.util.Util.plusOrMinus;

public class LastStandEntity extends Entity {
    private final int maxReleaseTicks = 200;
    private final int shockwaveFrequency = 20;
    private final int explosionFrequency = 20;
    private final int hailcoilReleaseFrequency = 10;
    private final int strength = 2;
    private static final EntityDataAccessor<Integer> DATA_RELEASE_TICKS = SynchedEntityData.defineId(LastStandEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_RELEASING = SynchedEntityData.defineId(LastStandEntity.class, EntityDataSerializers.BOOLEAN);
    private List<HailcoilEntity> hailcoils = new ArrayList<>();

    public LastStandEntity(EntityType<?> entityType, Level level) {
        super(EntityRegistry.LAST_STAND.get(), level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_RELEASE_TICKS, 0);
        builder.define(DATA_IS_RELEASING, false);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float v) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        if (input.getInt("releaseTicks").isPresent()) {
            this.setReleaseTicks(input.getInt("releaseTicks").get());
        }
        this.setReleasing(input.getBooleanOr("isReleasing", false));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        valueOutput.putInt("releaseTicks", Math.min(getReleaseTicks(), maxReleaseTicks));
        valueOutput.putBoolean("isReleasing", isReleasing());
    }

    @Override
    public void tick() {
        if (isReleasing()) {
            if (this.tickCount % shockwaveFrequency == 0) {
                for (int i = 0; i < 200; i++) {
                    Vec3 speeds = new Vec3(random.nextDouble(), random.nextDouble(), random.nextDouble()).normalize();
                    this.level().addAlwaysVisibleParticle(ParticleRegistry.SHOCKWAVE_PARTICLE.get(),
                            this.getX() + this.random.nextFloat() * plusOrMinus(),
                            this.getY() + this.random.nextFloat() * plusOrMinus(),
                            this.getZ() + this.random.nextFloat() * plusOrMinus(),
                            plusOrMinus() * speeds.x * strength,
                            plusOrMinus() * speeds.y * strength,
                            plusOrMinus() * speeds.z * strength);
                }

                if (this.level() instanceof ServerLevel serverLevel) {
                    for (LivingEntity livingEntity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(25))) {
                        if (this.distanceTo(livingEntity) < 25) {
                            livingEntity.hurtServer(serverLevel, this.damageSources().generic(), 4f);
                            livingEntity.addDeltaMovement(calculateDir(this, livingEntity, new Vec3(1, 1, 1)));
                        }
                    }
                }
            }
            
            if (this.tickCount % explosionFrequency == 0) {
                this.level().explode(this,
                        this.getX() + this.getRandom().nextFloat() * plusOrMinus() * 10,
                        this.getY() + this.getRandom().nextFloat() * plusOrMinus() * 10,
                        this.getZ() + this.getRandom().nextFloat() * plusOrMinus() * 10,
                        5,
                        Level.ExplosionInteraction.MOB);
            }

            if (this.tickCount % hailcoilReleaseFrequency == 0) {
                if (this.level() instanceof ServerLevel serverLevel) {
                    HailcoilEntity hailcoil = new HailcoilEntity(EntityRegistry.HAILCOIL.get(), serverLevel);
                    hailcoil.move(MoverType.SELF, this.blockPosition().getCenter());
                    hailcoil.setInvulnerable(true);
                    hailcoils.add(hailcoil);
                    hailcoil.finalizeSpawn(serverLevel, this.level().getCurrentDifficultyAt(this.blockPosition()),
                            EntitySpawnReason.TRIGGERED, null);

                    hailcoil.setDeltaMovement(new Vec3(random.nextDouble(), random.nextDouble(), random.nextDouble()));
                    serverLevel.addFreshEntityWithPassengers(hailcoil);
                    serverLevel.gameEvent(GameEvent.ENTITY_PLACE, this.blockPosition(), GameEvent.Context.of(this));
                }

            }

            for (int j = 0; j < 10; ++j) {
                double d0 = this.getX() + (0.35D - this.getRandom().nextDouble() * 0.7);
                double d1 = this.getY() + (1.5 - this.getRandom().nextDouble() * 2);
                double d2 = this.getZ() + (0.35D - this.getRandom().nextDouble() * 0.7);

                this.level().addAlwaysVisibleParticle(
                        ParticleTypes.SOUL,
                        d0, d1, d2,
                        (0.5D - this.getRandom().nextDouble()) * 0.15,
                        0.5f * this.getRandom().nextDouble() + 0.2D,
                        (0.5D - this.getRandom().nextDouble()) * 0.15);
            }

            setReleaseTicks(getReleaseTicks() - 1);
            if (getReleaseTicks() < 0) {
                this.discard();
                for (HailcoilEntity hailcoil : hailcoils) {
                    hailcoil.setInvulnerable(false);
                }
            }

        } else {
            for (int i = 0; i < Mth.lerp((float) this.tickCount / 200, 0, 10); ++i) {
                double d0 = this.getX() + (0.35D - this.getRandom().nextDouble() * 0.7);
                double d1 = this.getY() + (1.5 - this.getRandom().nextDouble() * 2);
                double d2 = this.getZ() + (0.35D - this.getRandom().nextDouble() * 0.7);

                this.level().addAlwaysVisibleParticle(
                        ParticleTypes.SOUL_FIRE_FLAME,
                        d0, d1, d2,
                        (0.5D - this.getRandom().nextDouble()) * 0.2,
                        ((double) this.tickCount / 200) * this.getRandom().nextDouble() + 0.2D,
                        (0.5D - this.getRandom().nextDouble()) * 0.2);
            }
        }
    }

    public void setReleasing(boolean releasing) {
        entityData.set(DATA_IS_RELEASING, releasing);
    }

    public void setReleaseTicks(int releaseTicks) {
        entityData.set(DATA_RELEASE_TICKS, Math.min(maxReleaseTicks, releaseTicks));
    }

    public boolean isReleasing() {
        return entityData.get(DATA_IS_RELEASING);
    }

    public int getReleaseTicks() {
        return Math.min(maxReleaseTicks, entityData.get(DATA_RELEASE_TICKS));
    }
}
