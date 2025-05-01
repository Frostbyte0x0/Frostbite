package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class LastStandEntity extends Entity {
    private float damageAccumulated;
    private boolean isReleasing;
    private int releaseTicks;
    private static final EntityDataAccessor<Float> DATA_DAMAGE_ACCUMULATED = SynchedEntityData.defineId(LastStandEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_RELEASE_TICKS = SynchedEntityData.defineId(LastStandEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_IS_RELEASING = SynchedEntityData.defineId(LastStandEntity.class, EntityDataSerializers.BOOLEAN);

    public LastStandEntity(EntityType<?> entityType, Level level) {
        super(EntityRegistry.LAST_STAND.get(), level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_DAMAGE_ACCUMULATED, 0f);
        builder.define(DATA_RELEASE_TICKS, 0);
        builder.define(DATA_IS_RELEASING, false);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("damageAccumulated")) {
            this.setDamageAccumulated(compoundTag.getFloat("damageAccumulated"));
        }
        if (compoundTag.contains("releaseTicks")) {
            this.setReleaseTicks(compoundTag.getInt("releaseTicks"));
        }
        if (compoundTag.contains("isReleasing")) {
            this.setReleasing(compoundTag.getBoolean("isReleasing"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putFloat("damageAccumulated", damageAccumulated);
        compoundTag.putInt("releaseTicks", releaseTicks);
        compoundTag.putBoolean("isReleasing", isReleasing);
    }

    @Override
    public void tick() {
        if (isReleasing()) {
            ParticleOptions particleoptions = ParticleTypes.ANGRY_VILLAGER;
            int f = 3;
            int i = Mth.ceil((float) Math.PI * f * f);
            for (int j = 0; j < i; j++) {
                float f2 = this.random.nextFloat() * (float) (Math.PI * 2);
                float f3 = Mth.sqrt(this.random.nextFloat()) * f;
                double d0 = this.getX() + (double)(Mth.cos(f2) * f3);
                double d1 = this.getY();
                double d2 = this.getZ() + (double)(Mth.sin(f2) * f3);
                this.level().addAlwaysVisibleParticle(particleoptions, d0, d1, d2,
                        (0.5 - this.random.nextDouble()) * 0.15,
                        0.01F,
                        (0.5 - this.random.nextDouble()) * 0.1);
            }

            setReleaseTicks(getReleaseTicks() - 1);
            if (getReleaseTicks() < 0) {
                this.discard();
            }
        }
    }

    public void setDamageAccumulated(float damageAccumulated) {
        entityData.set(DATA_DAMAGE_ACCUMULATED, damageAccumulated);
        setReleaseTicks((int) (damageAccumulated * 10));
    }

    public void setReleasing(boolean releasing) {
        entityData.set(DATA_IS_RELEASING, releasing);
    }

    public void setReleaseTicks(int releaseTicks) {
        entityData.set(DATA_RELEASE_TICKS, releaseTicks);
    }

    public boolean isReleasing() {
        return entityData.get(DATA_IS_RELEASING);
    }

    public float getDamageAccumulated() {
        return entityData.get(DATA_DAMAGE_ACCUMULATED);
    }

    public int getReleaseTicks() {
        return entityData.get(DATA_RELEASE_TICKS);
    }
}
