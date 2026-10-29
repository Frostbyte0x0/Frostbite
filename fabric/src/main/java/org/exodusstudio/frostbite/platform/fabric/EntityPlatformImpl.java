package org.exodusstudio.frostbite.platform.fabric;

import net.minecraft.world.entity.LightningBolt;
import org.exodusstudio.frostbite.fabric.mixinterfaces.LightningBoltDamage;

public class EntityPlatformImpl {
    public static void setLightningDamage(LightningBolt bolt, float damage) {
        ((LightningBoltDamage) bolt).frostbite$setDamage(damage);
    }
}
