package org.exodusstudio.frostbite.client.overlays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class FlashbangOverlay {
    private static int ticks = 0;
    private static int maxTicks = 60;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (ticks > 0) {
            ticks--;
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.Post event) {
        if (ticks > 0) {
            renderFlash(event.getGuiGraphics(), event.getPartialTick().getGameTimeDeltaTicks());
        }
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