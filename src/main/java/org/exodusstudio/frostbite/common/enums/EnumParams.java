package org.exodusstudio.frostbite.common.enums;

import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;

public class EnumParams {
    public static Object getResourceLocationDecayingHeart(int idx, Class<?> type) {
        return type.cast(ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
                "textures/overlays/decaying_heart.png"));
    }
}
