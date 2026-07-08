package org.exodusstudio.frostbite.client.overlays;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;

public class ComboOverlay {
    public static void render(GuiGraphicsExtractor gui, DeltaTracker ignored) {
        Player player = Minecraft.getInstance().player;
        assert player != null;

        if (!ComboWeapon.shouldShowComboOverlay(player)) return;

        int squareSide = 24;
        int halfSquareSide = squareSide / 2;

        int width = gui.guiWidth();
        int height = gui.guiHeight();
        int x0 = width / 2 + halfSquareSide;
        int y0 = height / 2 + halfSquareSide;
        int x1 = width / 2 - halfSquareSide;
        int y1 = height / 2 - halfSquareSide;


        gui.verticalLine(x0, y0, y1, 0xFFFFFFFF);
        gui.verticalLine(x1, y0, y1, 0xFFFFFFFF);
        gui.horizontalLine(x1, x0, y0, 0xFFFFFFFF);
        gui.horizontalLine(x1, x0, y1, 0xFFFFFFFF);

        gui.verticalLine((x0 + x1) / 2, y0 - halfSquareSide / 2, y0 + halfSquareSide / 2, 0xFFFF0000);
    }
}
