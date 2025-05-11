package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import static org.exodusstudio.frostbite.common.util.MathsUtil.spawnParticleRandomly;

public class HailcoilEntity extends Entity {
    public HailcoilEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = false;
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
        //this.moveTo(this.position().add(new Vec3(0, 0, 0.1)));

        for (int i = 0; i < 2; ++i) {
            spawnParticleRandomly(this, ParticleTypes.SOUL_FIRE_FLAME, 0.2, 0.055);
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
