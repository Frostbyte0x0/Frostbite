package org.exodusstudio.frostbite.common.network;

import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;

public class NetworkManager {
    public static ResourceLocation PLAYER_HEART_DATA_PACKET_ID = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "player_heart_data");
}
