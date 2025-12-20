package org.exodusstudio.frostbite.client.overlays;

import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;

import static org.exodusstudio.frostbite.common.util.TemperatureStorage.MAX_TEMP;
import static org.exodusstudio.frostbite.common.util.TemperatureStorage.MIN_TEMP;
import static org.exodusstudio.frostbite.common.util.Util.isFrostbite;

public class ThermometerOverlay {
    private static final Identifier THERMOMETER0 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer0.png");
    private static final Identifier THERMOMETER1 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer1.png");
    private static final Identifier THERMOMETER2 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer2.png");
    private static final Identifier THERMOMETER3 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer3.png");
    private static final Identifier THERMOMETER4 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer4.png");
    private static final Identifier THERMOMETER5 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer5.png");
    private static final Identifier THERMOMETER6 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer6.png");
    private static final Identifier THERMOMETER7 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/cold_bar/thermometer7.png");

    public static final Identifier[] THERMOMETERS = {THERMOMETER0, THERMOMETER1, THERMOMETER2, THERMOMETER3,
            THERMOMETER4, THERMOMETER5, THERMOMETER6, THERMOMETER7};


    public static void drawTexture(GuiGraphics graphics, int leftPos, int topPos, int width, int height, Identifier texture) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos, topPos, 0f, 0f, width, height, width, height);
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker ignored) {
        Player player = Minecraft.getInstance().player;
        assert player != null;

        float outer_temp = (float) Math.round(Frostbite.temperatureStorage.getTemperature(player, false) * 10f) / 10f;

        if (!isFrostbite(player.level()) && outer_temp == MAX_TEMP || Minecraft.getInstance().options.hideGui) {
            return;
        }

        int thermometerToShow = (int) Math.floor(Math.clamp(THERMOMETERS.length * (outer_temp - MIN_TEMP) / (MAX_TEMP - MIN_TEMP), 0, THERMOMETERS.length - 1));

        int textureWidth = 24;
        int textureHeight = 24;

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        int x = width / 2 + 130;
        int y = height - textureHeight - 2;

        drawTexture(guiGraphics, x, y, textureWidth, textureHeight, THERMOMETERS[thermometerToShow]);

        Font font = Minecraft.getInstance().font;
        Component text = Component.literal("").append("§7" + outer_temp + "C").withStyle(ChatFormatting.BLUE);
        guiGraphics.drawString(font, text, (x + (textureWidth - font.width(text)) / 2), y - textureHeight + 11, ARGB.color(255, 255, 255, 255));
    }
}
