package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.jetbrains.annotations.NotNull;

public class LastStandEntity extends Entity {
    private float damageAccumulated;
    private boolean isReleasing;
    private int releaseTimes;
    private final int maxReleaseTimes = 20;
    private final int frequency = 20;
    private final int strength = 5;
    private static final EntityDataAccessor<Float> DATA_DAMAGE_ACCUMULATED = SynchedEntityData.defineId(LastStandEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_RELEASE_TIMES = SynchedEntityData.defineId(LastStandEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_RELEASING = SynchedEntityData.defineId(LastStandEntity.class, EntityDataSerializers.BOOLEAN);

    public LastStandEntity(EntityType<?> entityType, Level level) {
        super(EntityRegistry.LAST_STAND.get(), level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_DAMAGE_ACCUMULATED, 0f);
        builder.define(DATA_RELEASE_TIMES, 0);
        builder.define(DATA_IS_RELEASING, false);
    }

    @Override
    public boolean hurtServer(@NotNull ServerLevel serverLevel, @NotNull DamageSource damageSource, float v) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("damageAccumulated")) {
            this.setDamageAccumulated(compoundTag.getFloat("damageAccumulated"));
        }
        if (compoundTag.contains("releaseTimes")) {
            this.setReleaseTimes(compoundTag.getInt("releaseTimes"));
        }
        if (compoundTag.contains("isReleasing")) {
            this.setReleasing(compoundTag.getBoolean("isReleasing"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putFloat("damageAccumulated", damageAccumulated);
        compoundTag.putInt("releaseTimes", Math.min(releaseTimes, maxReleaseTimes));
        compoundTag.putBoolean("isReleasing", isReleasing);
    }

    @Override
    public void tick() {
        if (!isReleasing() || this.tickCount % frequency != 0) {
            return;
        }

        for (int i = 0; i < 200; i++) {
            Vec3 speeds = new Vec3(random.nextDouble(), random.nextDouble(), random.nextDouble()).normalize();
            this.level().addAlwaysVisibleParticle(ParticleTypes.CRIT,
                    this.getX() + this.random.nextFloat() * plusOrMinus(),
                    this.getY() + this.random.nextFloat() * plusOrMinus(),
                    this.getZ() + this.random.nextFloat() * plusOrMinus(),
                    plusOrMinus() * speeds.x * strength,
                    strength * speeds.y,
                    plusOrMinus() * speeds.z * strength);
        }

        setReleaseTimes(getReleaseTimes() - 1);
        if (getReleaseTimes() < 0) {
            this.discard();
        }
    }

    public void setDamageAccumulated(float damageAccumulated) {
        entityData.set(DATA_DAMAGE_ACCUMULATED, damageAccumulated);
        setReleaseTimes((int) (damageAccumulated / 2));
    }

    public void setReleasing(boolean releasing) {
        entityData.set(DATA_IS_RELEASING, releasing);
    }

    public void setReleaseTimes(int releaseTimes) {
        entityData.set(DATA_RELEASE_TIMES, Math.min(maxReleaseTimes, releaseTimes));
    }

    public boolean isReleasing() {
        return entityData.get(DATA_IS_RELEASING);
    }

    public float getDamageAccumulated() {
        return entityData.get(DATA_DAMAGE_ACCUMULATED);
    }

    public int getReleaseTimes() {
        return Math.min(maxReleaseTimes, entityData.get(DATA_RELEASE_TIMES));
    }

    public int plusOrMinus() {
        return this.random.nextBoolean() ? 1 : -1;
    }
}
