package org.exodusstudio.frostbite.common.util.helpers;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;

@SuppressWarnings("unused")
public class ParticleHelper {
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

    public static void spawnParticlesFromAABB(Level level, AABB aabb, int count) {
        int[] xyz = new int[]{1, 0, 0};

        for (int j = 0; j < 3; j++) {
            for (float i = 0; i <= count; i++) {
                double x = Mth.lerp(xyz[0] * i / count, aabb.minX, aabb.maxX);
                double y = Mth.lerp(xyz[1] * i / count, aabb.minY, aabb.maxY);
                double z = Mth.lerp(xyz[2] * i / count, aabb.minZ, aabb.maxZ);

                level.addAlwaysVisibleParticle(
                        ParticleRegistry.DEBUG_PARTICLE.get(),
                        x,
                        y,
                        z,
                        0,
                        0,
                        0);
            }
            rotateArrayByOneRight(xyz);
        }

        for (int j = 0; j < 3; j++) {
            for (float i = 0; i <= count; i++) {
                double x = Mth.lerp(xyz[0] * i / count, aabb.maxX, aabb.minX);
                double y = Mth.lerp(xyz[1] * i / count, aabb.maxY, aabb.minY);
                double z = Mth.lerp(xyz[2] * i / count, aabb.maxZ, aabb.minZ);

                level.addAlwaysVisibleParticle(
                        ParticleRegistry.DEBUG_PARTICLE.get(),
                        x,
                        y,
                        z,
                        0,
                        0,
                        0);
            }
            rotateArrayByOneRight(xyz);
        }
    }

    public static void spawnParticlesFromVector(Level level, SimpleParticleType type, Vec3 origin, Vec3 vector, int count) {
        Vec3 pos;
        for (int i = 0; i <= count; i++) {
            pos = origin.add(vector.scale((double) i / count));
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        type,
                        pos.x,
                        pos.y,
                        pos.z,
                        1,
                        0, 0, 0, 0);
            } else {
                level.addAlwaysVisibleParticle(
                        type,
                        pos.x,
                        pos.y,
                        pos.z,
                        0,
                        0,
                        0);
            }
        }
    }

    public static void spawnParticlesFromVector(Level level, Vec3 origin, Vec3 vector, int count) {
        spawnParticlesFromVector(level, ParticleRegistry.DEBUG_PARTICLE.get(), origin, vector, count);
    }

    public static void rotateArrayByOneRight(int[] arr) {
        if (arr == null || arr.length <= 1) return;

        int lastElement = arr[arr.length - 1];
        for (int i = arr.length - 1; i > 0; i--) {
            arr[i] = arr[i - 1];
        }

        arr[0] = lastElement;
    }

//    public static void ring(Level level, SimpleParticleType type, Vec3 origin, Vec3 normal, int count, double radiusX, double radiusZ) {
//        normal = normal.normalize();
//        Vec3 perpNormal = Vec3Helper.perpendicularY(normal);
//        for (int i = 0; i < count; i++) {
//            Vec3 pos = Vec3Helper.rotateFirstAroundSecond(perpNormal, normal, Math.PI * 2 * i / count).multiply(radiusX, 1, radiusZ);
//            if (level instanceof ServerLevel serverLevel) {
//                serverLevel.sendParticles(
//                        type,
//                        origin.x + pos.x,
//                        origin.y + pos.y,
//                        origin.z + pos.z,
//                        1,
//                        0, 0, 0, 0);
//            } else {
//                level.addAlwaysVisibleParticle(
//                        type,
//                        origin.x + pos.x,
//                        origin.y + pos.y,
//                        origin.z + pos.z,
//                        0,
//                        0,
//                        0);
//            }
//        }
//    }

    public static void ring(Level level, SimpleParticleType type, Vec3 origin, Vec3 normal, int count, double radius, Vec3 expandingCenter, double expandingSpeed) {
        normal = normal.normalize();
        Vec3 perpNormal = Vec3Helper.perpendicularY(normal);
        for (int i = 0; i < count; i++) {
            Vec3 pos = Vec3Helper.rotateFirstAroundSecond(perpNormal, normal, Math.PI * 2 * i / count).scale(radius);
            Vec3 d = origin.add(pos);
            Vec3 s = d.subtract(expandingCenter).normalize().scale(expandingSpeed);
            if (level instanceof ServerLevel serverLevel && expandingSpeed == 0) {
                serverLevel.sendParticles(
                        type,
                        d.x,
                        d.y,
                        d.z,
                        1,
                        0, 0, 0, 0);
            } else {
                level.addAlwaysVisibleParticle(
                        type,
                        d.x,
                        d.y,
                        d.z,
                        s.x,
                        s.y,
                        s.z);
            }
        }
    }

    public static void ring(Level level, SimpleParticleType type, Vec3 origin, Vec3 normal, int count, double radius, double expandingSpeed) {
        normal = normal.normalize();
        Vec3 perpNormal = Vec3Helper.perpendicularY(normal);
        for (int i = 0; i < count; i++) {
            Vec3 pos = Vec3Helper.rotateFirstAroundSecond(perpNormal, normal, Math.PI * 2 * i / count).scale(radius);
            if (level instanceof ServerLevel serverLevel && expandingSpeed == 0) {
                serverLevel.sendParticles(
                        type,
                        origin.x + pos.x,
                        origin.y + pos.y,
                        origin.z + pos.z,
                        1,
                        0, 0, 0, 0);
            } else {
                level.addAlwaysVisibleParticle(
                        type,
                        origin.x + pos.x,
                        origin.y + pos.y,
                        origin.z + pos.z,
                        pos.x * expandingSpeed,
                        pos.y * expandingSpeed,
                        pos.z * expandingSpeed);
            }
        }
    }

    public static void ring(Level level, SimpleParticleType type, Vec3 origin, Vec3 normal, int count, double radius) {
        ring(level, type, origin, normal, count, radius, 0);
    }

    public static void sphere(Level level, SimpleParticleType type, Vec3 origin, Vec3 normal, int countPerRing, int ringCount, double radius, double expandingSpeed) {
        normal = normal.normalize();
        for (int i = 0; i <= ringCount; i++) {
            double a = (2f * i / ringCount - 1);
            Vec3 pos = origin.add(normal.scale(a * radius));

            if (i == 0 || i == ringCount)
                ring(level, type, pos, normal, 1, 0, origin, expandingSpeed);
            else {
                ring(level, type, pos, normal, countPerRing, radius * Math.sqrt(1 - a*a), origin, expandingSpeed);
            }
        }
    }

    public static void sphere(Level level, SimpleParticleType type, Vec3 origin, Vec3 normal, int countPerRing, int ringCount, double radius) {
        sphere(level, type, origin, normal, countPerRing, ringCount, radius, 0);
    }

    public static void completeSphere(Level level, SimpleParticleType type, Vec3 origin, Vec3 normal, int countPerRing, int ringCount, double radius, double expandingSpeed) {
        Vec3 normal2 = Vec3Helper.perpendicularY(normal);
        Vec3 normal3 = Vec3Helper.rotateFirstAroundSecond(normal2, normal, Math.PI / 2);
        sphere(level, type, origin, normal, countPerRing, ringCount, radius, expandingSpeed);
        sphere(level, type, origin, normal2, countPerRing, ringCount, radius, expandingSpeed);
        sphere(level, type, origin, normal3, countPerRing, ringCount, radius, expandingSpeed);
    }

    public static void completeSphere(Level level, SimpleParticleType type, Vec3 origin, Vec3 normal, int countPerRing, int ringCount, double radius) {
        completeSphere(level, type, origin, normal, countPerRing, ringCount, radius, 0);
    }

//    public static void sphere(Level level, SimpleParticleType type, Vec3 origin, Vec3 normal, int countPerRing, int ringCount, int radiusX, int radiusY, int radiusZ) {
//        normal = normal.normalize();
//        for (int i = 0; i < ringCount; i++) {
//            Vec3 pos = origin.add(normal.scale((i - ringCount / 2f) * radiusY));
//            double r = Math.cos((i - ringCount / 2f) / ringCount * Math.PI * 2);
//            ring(level, type, pos, normal, countPerRing, radiusX * r, radiusZ * r);
//        }
//    }
}
