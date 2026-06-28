package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.client.gui.GuiGraphicsExtractor;

public record SideTile(ListCodexEntry entry) {
    public static final int WIDTH = 100;
    public static final int HEIGHT = 40;

    public void draw(GuiGraphicsExtractor gui, int x, int y) {
        //gui.drawTexture(entry.image, x, y, WIDTH, HEIGHT);
    }
}
