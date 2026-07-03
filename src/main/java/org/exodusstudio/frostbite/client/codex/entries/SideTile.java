package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.exodusstudio.frostbite.common.util.Util;

import java.util.List;

public class SideTile {
    private final ListCodexEntry entry;
    public static final int WIDTH = 75;
    public static final int HEIGHT = 30;
    public List<FormattedCharSequence> titleLines;
    public List<FormattedCharSequence> descriptionLines;
    public List<FormattedCharSequence> tipsLines;

    public SideTile(ListCodexEntry entry) {
        this.entry = entry;
        this.titleLines = Minecraft.getInstance().font.split(entry.title, 65);
        this.descriptionLines = Minecraft.getInstance().font.split(entry.description, 65);
        this.tipsLines = Minecraft.getInstance().font.split(entry.tips, 65);
    }

    public void drawSide(GuiGraphicsExtractor gui, int x, int y) {
        Util.drawTexture(gui, x, y, WIDTH, HEIGHT, entry.image);
        Util.drawMultilineText(gui, this.titleLines, x + 5, y + 5, 0xFFFFFFFF);
    }

    public void drawContent(GuiGraphicsExtractor gui, int x, int y) {
        Util.drawMultilineText(gui, this.descriptionLines, x + 3, y + 5, 0xFFFFFFFF);
        gui.text(Minecraft.getInstance().font, Component.translatable("codex.tips"), x + 3, y + 14 + this.descriptionLines.size() * 9, 0xFFFFFFFF);
        Util.drawMultilineText(gui, this.tipsLines, x + 3, y + 23 + this.descriptionLines.size() * 9, 0xFFFFFFFF);
    }

    public int getContentHeight() {
        return this.descriptionLines.size() * 9 + this.tipsLines.size() * 9 + 28;
    }
}
