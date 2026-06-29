package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.exodusstudio.frostbite.common.util.Util;

public record SideTile(ListCodexEntry entry) {
    public static final int WIDTH = 75;
    public static final int HEIGHT = 30;

    public void drawSide(GuiGraphicsExtractor gui, int x, int y) {
        Util.drawTexture(gui, x, y, WIDTH, HEIGHT, entry.image);
        Util.drawMultilineText(gui, entry.titleLines, x + 5, y + 5, 0xFFFFFF);
    }
}
