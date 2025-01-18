package com.frostbyte.frostbite.client;

import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class FrostbiteClient {
    public static void initClient() {
        registerOverlays();
    }

    public static void registerOverlays() {
        RenderGuiEvent.register(ColdBarOverlay::render);
    }
}
