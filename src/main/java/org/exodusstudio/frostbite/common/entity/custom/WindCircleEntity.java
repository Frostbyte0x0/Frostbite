package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.particle.DrainParticleOptions;
import org.exodusstudio.frostbite.common.particle.WindCircleParticleOptions;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

import java.util.List;

public class WindCircleEntity extends AreaEffectCloud {
    public WindCircleEntity(EntityType<? extends WindCircleEntity> entityType, Level level) {
        super(EntityRegistry.WIND_CIRCLE.get(), level);
    }

    @Override
    public void tick() {
        if (this.getRadius() == 0) {
            this.setRadius(2.5f);
        }

        if (this.tickCount == 1) {
            if (this.level().isClientSide) {
                this.level().addParticle(new WindCircleParticleOptions(0), this.getX(), this.getY() + 0.2f, this.getZ(), 0, 0, 0);
            }
        }

        if (this.tickCount == 26) {
            List<LivingEntity> list1 = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0, 3, 0));
            if (!list1.isEmpty()) {
                for (LivingEntity livingentity : list1) {
                    livingentity.push(new Vec3(0, 2, 0));
                }
            }
        }
        if (this.tickCount == 27) this.discard();
    }
}
