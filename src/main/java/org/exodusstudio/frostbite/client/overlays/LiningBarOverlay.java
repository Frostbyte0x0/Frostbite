package org.exodusstudio.frostbite.client.overlays;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;

public class LiningBarOverlay {
    private static final ResourceLocation ARMOR_HALF_SPRITE =
            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/overlays/lining_bar/lining_bar_half.png");
    private static final ResourceLocation ARMOR_FULL_SPRITE =
            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/overlays/lining_bar/lining_bar_full.png");


    public static void render(GuiGraphics graphics, DeltaTracker ignored) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            assert Minecraft.getInstance().gameMode != null;
            if (Minecraft.getInstance().gameMode.canHurtPlayer()) {
                int l = graphics.guiWidth() / 2 - 91;
                renderLining(graphics, player, graphics.guiHeight() - 34, 1, 0, l);
            }
        }
    }

    private static void renderLining(GuiGraphics guiGraphics, Player player, int y, int heartRows, int height, int x) {
        int i = Frostbite.liningStorage.getLiningLevelForPlayer(player.getStringUUID());
        if (i > 0) {
            int j = y - (heartRows - 1) * height - 15;

            for (int k = 0; k < 10; ++k) {
                int l = x + k * 8;
                if (k * 2 + 1 < i) {
                    drawTexture(guiGraphics, l, j, 9, 9, ARMOR_FULL_SPRITE, i);
                }

                if (k * 2 + 1 == i) {
                    drawTexture(guiGraphics, l, j, 9, 9, ARMOR_HALF_SPRITE, i);
                }
            }
        }

    }

    public static void drawTexture(GuiGraphics graphics, int leftPos, int topPos, int width, int height, ResourceLocation texture, int level) {
        int red = (int) Mth.lerp(Math.max(0, level - 20) / 4f, 100, 255);
        int colour = ARGB.color(red, 255, 255);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, leftPos, topPos, 0f, 0f, width, height, width, height, colour);
    }
}
