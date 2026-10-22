package org.exodusstudio.frostbite.common.util.helpers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.util.Util;
import org.joml.Quaternionf;

public class Vec3Helper {
    public static Quaternionf getRotationQuaternionAroundLookVector(int j, int count, Entity owner, Vec3 vec32) {
        float angle = (float) (j * 2 * Math.PI / count);
        float playerYAngle = (float) ((90 - owner.getYRot()) * Math.PI / 180);
        float playerXAngle = (float) (owner.getXRot() * Math.PI / 180);
        Quaternionf quaternion;
        if (Math.abs(vec32.x) < 0.5f) {
            quaternion = new Quaternionf()
                    .rotateLocalX(angle)
                    .rotateLocalZ(playerXAngle)
                    .rotateLocalY(playerYAngle);
        } else {
            quaternion = new Quaternionf()
                    .rotateLocalZ(angle)
                    .rotateLocalX(-playerXAngle)
                    .rotateLocalY((float) (playerYAngle + Math.PI / 2));
        }

        return quaternion;
    }

    public static Vec3 rotateFirstAroundSecond(Vec3 target, Vec3 around, double angle) {
        around = around.normalize();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double dot = around.dot(target);
        Vec3 cross = around.cross(target);
        return target.scale(cos).add(cross.scale(sin)).add(around.scale(dot).scale(1 - cos));
    }

    public static Quaternionf getRotationQuaternionAroundVector(float angle, Vec3 vec32) {
        float playerYAngle = (float) (Math.atan2(vec32.z, vec32.x) + Math.PI / 4);
        float playerXAngle = (float) -Math.atan2(vec32.y, vec32.x);
        Quaternionf quaternion;
        if (Math.abs(vec32.x) < 0.5f) {
            quaternion = new Quaternionf()
                    .rotateLocalX(angle)
                    .rotateLocalZ(playerXAngle)
                    .rotateLocalY(playerYAngle);
        } else {
            quaternion = new Quaternionf()
                    .rotateLocalZ(angle)
                    .rotateLocalX(-playerXAngle)
                    .rotateLocalY((float) (playerYAngle + Math.PI / 2));
        }

        return quaternion;
    }

    public static Vec3 perpendicularY(Vec3 d) {
        if (d.x == 0 && d.z == 0) return Vec3.X_AXIS;
        double lengthXZ = Util.distanceBetween(d.x, d.z);
        return new Vec3(-d.x * d.y / lengthXZ, lengthXZ, -d.z * d.y / lengthXZ);
    }

    public static float[] getXYRot(Vec3 dir) {
        float xRot = (float) Math.toDegrees(Math.asin(-dir.y));
        float yRot = (float) -Math.toDegrees(Math.atan2(dir.x, dir.z));
        return new float[]{xRot, yRot};
    }
}
