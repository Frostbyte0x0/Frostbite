package org.exodusstudio.frostbite.common.network;

import dev.architectury.networking.NetworkManager;

public class NetworkRegistry {
    public static void init() {
        NetworkManager.registerReceiver(NetworkManager.c2s(), StaffPayload.TYPE, StaffPayload.STREAM_CODEC, ServerPayloadHandler::handleDataOnMain);
        NetworkManager.registerReceiver(NetworkManager.c2s(), ParryPayload.TYPE, ParryPayload.STREAM_CODEC, ServerPayloadHandler::handleParry);
    }
}
