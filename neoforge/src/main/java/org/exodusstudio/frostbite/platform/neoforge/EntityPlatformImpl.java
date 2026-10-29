package org.exodusstudio.frostbite.platform.neoforge;

import net.minecraft.world.entity.LightningBolt;

public class EntityPlatformImpl {
    public static void setLightningDamage(LightningBolt bolt, float damage) {
        bolt.setDamage(damage);
    }
}
