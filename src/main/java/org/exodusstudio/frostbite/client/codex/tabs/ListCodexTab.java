package org.exodusstudio.frostbite.client.codex.tabs;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.codex.entries.CodexWidget;
import org.exodusstudio.frostbite.client.codex.entries.ListCodexEntry;
import org.exodusstudio.frostbite.client.codex.entries.SideTile;
import org.exodusstudio.frostbite.common.util.Util;

import java.util.Arrays;

public class ListCodexTab extends CodexTab {
    private final SideTile[] tiles;

    public ListCodexTab(String title, CodexTabType type, int index, String icon, ListCodexEntry... entries) {
        super(title, type, index, icon);
        this.tiles = Arrays.stream(entries).map(SideTile::new).toArray(SideTile[]::new);
    }

    @Override
    public void drawContents(GuiGraphicsExtractor GuiGraphicsExtractor, int scrollX, int scrollY) {
        super.drawContents(GuiGraphicsExtractor, scrollX, scrollY);

        GuiGraphicsExtractor.enableScissor(scrollX, scrollY, scrollX + 234, scrollY + 113);
        GuiGraphicsExtractor.pose().pushMatrix();
        GuiGraphicsExtractor.pose().translate(scrollX, scrollY);
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);

        for (SideTile tile : this.tiles) {
            tile.draw(GuiGraphicsExtractor, i, j);
        }
        GuiGraphicsExtractor.pose().popMatrix();
        GuiGraphicsExtractor.disableScissor();
    }
}
