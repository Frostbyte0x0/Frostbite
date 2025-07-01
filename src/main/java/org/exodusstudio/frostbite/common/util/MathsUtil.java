package org.exodusstudio.frostbite.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

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

    public static AABB squareAABB(AABB aabb, double inflation) {
        double sizeX = aabb.maxX - aabb.minX;
        double sizeY = aabb.maxY - aabb.minY;
        double sizeZ = aabb.maxZ - aabb.minZ;

        double minSize = Math.min(sizeX, Math.min(sizeY, sizeZ));
        Vec3 start = new Vec3(aabb.minX + sizeX / 2, aabb.minY + sizeY / 2, aabb.minZ + sizeZ / 2);
        Vec3 end = start.add(minSize);
        return new AABB(start, end).inflate(inflation);
    }

    public static AABB squareAABB(Vec3 center, double size) {
        Vec3 start = new Vec3(center.x - size / 2, center.y - size / 2, center.z - size / 2);
        Vec3 end = start.add(size);
        return new AABB(start, end);
    }

    public static List<BlockPos> getBlockPositionsInAABB(AABB aabb) {
        List<BlockPos> positions = new ArrayList<>();

        int sizeX = Math.toIntExact(Math.round(aabb.maxX) - Math.round(aabb.minX));
        int sizeY = Math.toIntExact(Math.round(aabb.maxY) - Math.round(aabb.minY));
        int sizeZ = Math.toIntExact(Math.round(aabb.maxZ) - Math.round(aabb.minZ));

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                for (int k = 0; k < sizeZ; k++) {
                    positions.add(new BlockPos(Math.round((float) aabb.minX) + i,
                                               Math.round((float) aabb.minY) + j,
                                               Math.round((float) aabb.minZ) + k));
                }
            }
        }

        return positions;
    }
}
