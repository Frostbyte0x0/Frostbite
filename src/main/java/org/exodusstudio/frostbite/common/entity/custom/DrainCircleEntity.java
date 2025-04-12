package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.particle.DrainParticleOptions;

import java.util.List;

public class DrainCircleEntity extends AreaEffectCloud {
    private int charge;
    private final int chargePerHealth = 5;
    private final float dps = 1f;

    public DrainCircleEntity(EntityType<? extends AreaEffectCloud> p_19704_, Level p_19705_) {
        super(p_19704_, p_19705_);
    }

    @Override
    public void tick() {
        if (this.getRadius() == 0) {
            this.setRadius(2.5f);
        }

        if (this.tickCount % 20 == 0) {
            if (this.level().isClientSide) {
                this.level().addParticle(new DrainParticleOptions(0), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }

            if (this.level() instanceof ServerLevel serverLevel) {
                List<LivingEntity> list1 = serverLevel.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
                if (!list1.isEmpty()) {
                    for (LivingEntity livingentity : list1) {
                        livingentity.hurtServer(serverLevel, this.damageSources().generic(), dps);
                        this.charge += (int) (dps * chargePerHealth);
                    }
                }
            }
        }
        //super.tick();
    }

    public int absorbCharge() {
        int tempCharge = charge;
        charge = 0;
        return tempCharge;
    }
}
