package org.exodusstudio.frostbite.common.util;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class MathsUtil {
    static RandomSource random = RandomSource.create();

    public static Vec3 calculateDir(Entity e1, Entity e2, Vec3 multiplier) {
        double theta = 0;
        double alpha = 0;
        double sign = 1;

        double x = e1.position().x;
        double y = e1.position().y;
        double z = e1.position().z;

        double u = e2.position().x;
        double v = e2.position().y;
        double w = e2.position().z;

        if (!(u - x == 0)) {
            sign = (u - x) / Math.abs(u - x);
            theta = Math.atan((v - y) / (u - x));
            alpha = Math.atan((w - z) / (u - x));
        }

        return new Vec3(Math.cos(theta) * multiplier.x * sign,
                Math.sin(theta) * multiplier.y * sign,
                Math.sin(alpha) * multiplier.z * sign);
    }

    public static int plusOrMinus() {
        return random.nextBoolean() ? 1 : -1;
    }

    public static void spawnParticleRandomly(Entity entity, SimpleParticleType particleType, double positionVariation, double speedVariation) {
        double d0 = entity.getX() + (0.5D - entity.getRandom().nextDouble()) * positionVariation;
        double d1 = entity.getY() + (0.5D - entity.getRandom().nextDouble()) * positionVariation;
        double d2 = entity.getZ() + (0.5D - entity.getRandom().nextDouble()) * positionVariation;

        entity.level().addAlwaysVisibleParticle(
                particleType,
                d0, d1, d2,
                (0.5 - entity.getRandom().nextDouble()) * speedVariation,
                (0.5 - entity.getRandom().nextDouble()) * speedVariation,
                (0.5 - entity.getRandom().nextDouble()) * speedVariation);
    }
}
