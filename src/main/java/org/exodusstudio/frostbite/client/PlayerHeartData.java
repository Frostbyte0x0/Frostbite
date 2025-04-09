package org.exodusstudio.frostbite.client;

import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;

public class PlayerHeartData {
    public static ResourceLocation sprite = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "decaying_heart");
    public static boolean shouldShow = false;
}
