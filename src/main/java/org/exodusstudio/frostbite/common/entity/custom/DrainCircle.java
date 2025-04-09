package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.particle.DrainParticleOptions;

public class DrainCircle extends AreaEffectCloud {
    public DrainCircle(EntityType<? extends AreaEffectCloud> p_19704_, Level p_19705_) {
        super(p_19704_, p_19705_);
    }

    @Override
    public void tick() {
        if (this.level().isClientSide && this.tickCount % 20 == 0) {
            this.level().addParticle(new DrainParticleOptions(0), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }
        //super.tick();
    }
}
