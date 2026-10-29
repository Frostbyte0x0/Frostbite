package org.exodusstudio.frostbite.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.LightningBolt;

public class EntityPlatform {
    @ExpectPlatform
    public static void setLightningDamage(LightningBolt bolt, float damage) {
        throw new AssertionError();
    }
}
