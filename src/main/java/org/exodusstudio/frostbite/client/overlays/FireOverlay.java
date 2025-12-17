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

public class FireOverlay {
    private static final Identifier FIRE0 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire0.png");
    private static final Identifier FIRE1 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire1.png");
    private static final Identifier FIRE2 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire2.png");
    private static final Identifier FIRE3 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire3.png");
    private static final Identifier FIRE4 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire4.png");
    private static final Identifier FIRE5 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire5.png");
    private static final Identifier FIRE6 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire6.png");
    private static final Identifier FIRE7 = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/overlays/fire/fire7.png");

    public static final Identifier[] FIRES = {FIRE0, FIRE1, FIRE2, FIRE3,
            FIRE4, FIRE5, FIRE6, FIRE7};


    public static void drawTexture(GuiGraphics graphics, int leftPos, int topPos, int width, int height, Identifier texture) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos, topPos, 0f, 0f, width, height, width, height);
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker ignored) {
        Player player = Minecraft.getInstance().player;
        assert player != null;


        float innerTemp = (float) Math.round(Frostbite.temperatureStorage.getTemperature(player, true) * 10f) / 10f;

        if (!isFrostbite(player.level()) && innerTemp == MAX_TEMP || Minecraft.getInstance().options.hideGui) {
            return;
        }

        int fireToShow = (int) Math.floor(Math.clamp(FIRES.length * (innerTemp - MIN_TEMP) / (MAX_TEMP - MIN_TEMP), 0, FIRES.length - 1));

        int textureWidth = 24;
        int textureHeight = 24;

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();
        int x = width / 2 + 95;
        int y = height - textureHeight - 2;


        drawTexture(guiGraphics, x, y, textureWidth, textureHeight, FIRES[fireToShow]);

        Font font = Minecraft.getInstance().font;
        Component text = Component.literal("§7" + innerTemp + "C").withStyle(ChatFormatting.RED);
        guiGraphics.drawString(font, text, (x + (textureWidth - font.width(text)) / 2), y - textureHeight + 11, ARGB.color(255, 255, 255, 255));
    }
}
