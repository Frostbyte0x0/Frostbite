package org.exodusstudio.frostbite.client.overlays;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;

public class ComboOverlay {
    public static void render(GuiGraphicsExtractor gui, DeltaTracker ignored) {
        Player player = Minecraft.getInstance().player;
        assert player != null;

        if (!ComboWeapon.shouldShowComboOverlay(player)) return;
        if (!(player.getItemInHand(player.getUsedItemHand()).getItem() instanceof ComboWeapon comboWeapon)) return;

        int squareSide = 25;
        int halfSquareSide = squareSide / 2;

        int width = gui.guiWidth();
        int height = gui.guiHeight();
        int x0 = (width + squareSide) / 2;
        int y0 = (height + squareSide) / 2;
        int x1 = (width - squareSide) / 2;
        int y1 = (height - squareSide) / 2;

        float progress = ComboWeapon.getStepProgress(player);

        float tolerance = comboWeapon.getComboStep(player).tolerance();
        float critTolerance = comboWeapon.getComboStep(player).critTolerance();
        int w = (int) (tolerance * 4 * halfSquareSide);
        int cw = (int) (critTolerance * 4 * halfSquareSide);
        gui.fill(
                width / 2 + w,
                y0 + 2,
                width / 2 - w,
                y0 - 1,
                ARGB.color(255, 245, 215, 66));
        gui.fill(
                width / 2 + cw,
                y0 + 2,
                width / 2 - cw,
                y0 - 1,
                ARGB.color(255, 255, 55, 41));

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

        int index = ComboWeapon.getComboIndex(player) + 1;
        int stepCount = comboWeapon.getComboStepCount();
        int r = (int) Mth.lerp((float) index / stepCount, 128, 219);
        int g = (int) Mth.lerp((float) index / stepCount, 114, 24);
        int b = (int) Mth.lerp((float) index / stepCount, 25, 24);

        gui.text(
                Minecraft.getInstance().font,
                "x" + index,
                (width - Minecraft.getInstance().font.width("x" + index)) / 2 + squareSide,
                height / 2 - Minecraft.getInstance().font.lineHeight / 2,
                ARGB.color(r, g, b));
    }
}
