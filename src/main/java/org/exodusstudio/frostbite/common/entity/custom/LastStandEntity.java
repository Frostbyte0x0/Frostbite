package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class LastStandEntity extends Entity {
    private float damageAccumulated;
    private boolean isReleasing;
    private int releaseTicks;

    public LastStandEntity(EntityType<?> entityType, Level level) {
        super(EntityRegistry.LAST_STAND.get(), level);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    public void tick() {
        if (isReleasing) {
            ParticleOptions particleoptions = ParticleTypes.ENTITY_EFFECT;
            int f = 3;
            int i = Mth.ceil((float) Math.PI * f * f);
            for (int j = 0; j < i; j++) {
                float f2 = this.random.nextFloat() * (float) (Math.PI * 2);
                float f3 = Mth.sqrt(this.random.nextFloat()) * f;
                double d0 = this.getX() + (double)(Mth.cos(f2) * f3);
                double d1 = this.getY();
                double d2 = this.getZ() + (double)(Mth.sin(f2) * f3);
                if (particleoptions.getType() == ParticleTypes.ENTITY_EFFECT) {
                    if (flag && this.random.nextBoolean()) {
                        this.level().addAlwaysVisibleParticle(ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, -1), d0, d1, d2, 0.0, 0.0, 0.0);
                    } else {
                        this.level().addAlwaysVisibleParticle(particleoptions, d0, d1, d2, 0.0, 0.0, 0.0);
                    }
                } else if (flag) {
                    this.level().addAlwaysVisibleParticle(particleoptions, d0, d1, d2, 0.0, 0.0, 0.0);
                } else {
                    this.level()
                            .addAlwaysVisibleParticle(
                                    particleoptions, d0, d1, d2, (0.5 - this.random.nextDouble()) * 0.15, 0.01F, (0.5 - this.random.nextDouble()) * 0.15
                            );
                }
            }
            releaseTicks--;
        }
    }

    public void setDamageAccumulated(float damageAccumulated) {
        this.damageAccumulated = damageAccumulated;
        releaseTicks = (int) (damageAccumulated * 10);
    }

    public void setReleasing(boolean releasing) {
        isReleasing = releasing;
    }
}
