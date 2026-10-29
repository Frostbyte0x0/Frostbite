package org.exodusstudio.frostbite.client.overlays;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class FlashbangOverlay {
    private static int ticks = 0;
    private static int maxTicks = 60;

    public static void init() {
        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            if (ticks > 0) {
                ticks--;
            }
        });

        ClientGuiEvent.RENDER_HUD.register((graphics, deltaTracker) -> {
            if (ticks > 0) {
                renderFlash(graphics, deltaTracker.getGameTimeDeltaTicks());
            }
        });
    }

    public static void trigger() {
        trigger(200);
    }

    public static void trigger(int duration) {
        ticks = duration;
        maxTicks = duration;
    }

    private static void renderFlash(GuiGraphicsExtractor graphics, float partialTicks) {
        Minecraft client = Minecraft.getInstance();
        if (client != null && client.getWindow() != null) {
            int width = client.getWindow().getWidth();
            int height = client.getWindow().getHeight();

            float progress = 1.0f - ((float) ticks / maxTicks);
            float alpha = 1.0f / (1.0f + (float) Math.exp(8 * (progress - 0.7f)));

            int color = ((int) (alpha * 255) << 24) | 0xFFFFFF;
            graphics.fill(0, 0, width, height, color);
        }
    }

}