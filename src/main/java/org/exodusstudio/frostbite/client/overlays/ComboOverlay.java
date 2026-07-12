package org.exodusstudio.frostbite.client.overlays;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
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

        float lastHit = ComboWeapon.getTimeSinceLastHit(player);
        float comboLength = ComboWeapon.getComboLength(player);
        float progress = lastHit / comboLength;

        gui.horizontalLine(x1 + halfSquareSide, x0, y1, 1 / 8f > progress ? 0xFFFFFFFF : 0xFF000000);
        gui.verticalLine(x0, y0, y1, 3 / 8f > progress ? 0xFFFFFFFF : 0xFF000000);
        gui.horizontalLine(x1, x0, y0, 5 / 8f > progress ? 0xFFFFFFFF : 0xFF000000);
        gui.verticalLine(x1, y0, y1, 7 / 8f > progress ? 0xFFFFFFFF : 0xFF000000);
        gui.horizontalLine(x1, x0 - halfSquareSide, y1, 1 > progress ? 0xFFFFFFFF : 0xFF000000);

        if (1 / 8f > progress) {
            gui.horizontalLine(x1 + halfSquareSide, (int) Mth.lerp(progress * 8f, x1 + halfSquareSide, x0),
                    y1, 0xFF000000);
        }
        if (3 / 8f > progress && progress > 1 / 8f) {
            gui.verticalLine(x0, (int) Mth.lerp((progress - 1 / 8f) * 4f, y1, y0),
                    y1, 0xFF000000);
        }
        if (5 / 8f > progress && progress > 3 / 8f) {
            gui.horizontalLine(x0, (int) Mth.lerp((progress - 3 / 8f) * 4f, x0, x1),
                    y0, 0xFF000000);
        }
        if (7 / 8f > progress && progress > 5 / 8f) {
            gui.verticalLine(x1, (int) Mth.lerp((progress - 5 / 8f) * 4f, y0, y1),
                    y0, 0xFF000000);
        }
        if (1 > progress && progress > 7 / 8f) {
            gui.horizontalLine(x1, (int) Mth.lerp((progress - 7 / 8f) * 8f, x1, x0 - halfSquareSide),
                    y1, 0xFF000000);
        }


        gui.verticalLine((x0 + x1) / 2, y0 - halfSquareSide / 2, y0 + halfSquareSide / 2, 0xFFFF0000);
    }
}
