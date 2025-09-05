package org.exodusstudio.frostbite.client.overlays;

import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.exodusstudio.frostbite.Frostbite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import static org.exodusstudio.frostbite.common.util.Util.isFrostbite;

public class ThermometerOverlay {
    private static final ResourceLocation THERMOMETER0 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer0.png");
    private static final ResourceLocation THERMOMETER1 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer1.png");
    private static final ResourceLocation THERMOMETER2 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer2.png");
    private static final ResourceLocation THERMOMETER3 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer3.png");
    private static final ResourceLocation THERMOMETER4 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer4.png");
    private static final ResourceLocation THERMOMETER5 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer5.png");
    private static final ResourceLocation THERMOMETER6 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer6.png");
    private static final ResourceLocation THERMOMETER7 = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer7.png");

    public static final ResourceLocation[] THERMOMETERS = {THERMOMETER0, THERMOMETER1, THERMOMETER2, THERMOMETER3,
            THERMOMETER4, THERMOMETER5, THERMOMETER6, THERMOMETER7};


    public static void drawTexture(GuiGraphics graphics, int leftPos, int topPos, int width, int height, ResourceLocation texture, boolean blend) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos, topPos, 0f, 0f, width, height, width, height);
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Player player = Minecraft.getInstance().player;
        assert player != null;

        float maxTemp = 20f;
        float minTemp = -60f;

        float outer_temp = (float) Math.round(Frostbite.savedTemperatures.getTemperature(player, false) * 10f) / 10f;

        if (!isFrostbite(player.level()) && outer_temp == maxTemp) {
            return;
        }

        int thermometerToShow = (int) Math.floor(Math.clamp(THERMOMETERS.length * (outer_temp - minTemp) / (maxTemp - minTemp), 0, THERMOMETERS.length - 1));

        int x = 610;
        int y = 470;

        int textureWidth = 24;
        int textureHeight = 24;

        drawTexture(guiGraphics, x, y, textureWidth, textureHeight, THERMOMETERS[thermometerToShow], false);

        Font font = Minecraft.getInstance().font;
        Component text = Component.literal("").append("§7" + outer_temp + "C").withStyle(ChatFormatting.BLUE);
        guiGraphics.drawString(font, text, (x + (textureWidth - font.width(text)) / 2), y - textureHeight + 11, ARGB.color(255, 255, 255, 255));
    }
}
