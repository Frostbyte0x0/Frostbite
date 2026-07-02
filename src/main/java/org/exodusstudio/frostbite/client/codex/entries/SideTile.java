package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import org.exodusstudio.frostbite.common.util.Util;

public record SideTile(ListCodexEntry entry) {
    public static final int WIDTH = 75;
    public static final int HEIGHT = 30;

    public void drawSide(GuiGraphicsExtractor gui, int x, int y) {
        Util.drawTexture(gui, x, y, WIDTH, HEIGHT, entry.image);
        Util.drawMultilineText(gui, entry.titleLines, x + 5, y + 5, 0xFFFFFFFF);
    }

    public void drawContent(GuiGraphicsExtractor gui, int x, int y) {
        Util.drawMultilineText(gui, entry.descriptionLines, x + 3, y + 5, 0xFFFFFFFF);
        gui.text(Minecraft.getInstance().font, Component.translatable("codex.tips"), x + 3, y + 14 + entry.descriptionLines.size() * 9, 0xFFFFFFFF);
        Util.drawMultilineText(gui, entry.tipsLines, x + 3, y + 23 + entry.descriptionLines.size() * 9, 0xFFFFFFFF);
    }

    public int getContentHeight() {
        return entry.descriptionLines.size() * 9 + entry.tipsLines.size() * 9 + 28;
    }
}
