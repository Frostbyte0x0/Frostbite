package org.exodusstudio.frostbite.common.util;

import net.minecraft.world.phys.Vec3;

public class ScreenShakeHandler {
    private static final double decay = 0.9;
    private static Vec3 offset = Vec3.ZERO;
    private static double intensity = 0.0;

    public static void triggerShake(double amount) {
        intensity = amount;
        applyRandomOffset();
    }

    public static void tick() {
        offset = offset.scale(decay);
        intensity *= decay;

        // If it's basically gone, reset
        if (intensity < 0.01) {
            offset = Vec3.ZERO;
            intensity = 0.0;
        } else {
            applyRandomOffset();
        }
    }

    public static Vec3 getOffset() {
        return offset;
    }

    private static void applyRandomOffset() {
        offset = new Vec3(
                (Math.random() - 0.5) * intensity,
                (Math.random() - 0.5) * intensity,
                (Math.random() - 0.5) * intensity
        );
    }
}

