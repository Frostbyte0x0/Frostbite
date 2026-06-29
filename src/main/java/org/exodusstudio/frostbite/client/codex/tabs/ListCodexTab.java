package org.exodusstudio.frostbite.client.codex.tabs;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.exodusstudio.frostbite.client.codex.entries.ListCodexEntry;
import org.exodusstudio.frostbite.client.codex.entries.SideTile;

import java.util.Arrays;

public class ListCodexTab extends CodexTab {
    private static final int SCROLL_BAR_WIDTH = 8;
    private final SideTile[] tiles;
    private SideTile selectedTile;

    public ListCodexTab(String id, CodexTabType type, int index, String icon, ListCodexEntry... entries) {
        super(id, type, index, icon);
        this.tiles = Arrays.stream(entries).map(SideTile::new).toArray(SideTile[]::new);
    }

    @Override
    public void drawContents(GuiGraphicsExtractor gui, int scrollX, int scrollY) {
        super.drawContents(gui, scrollX, scrollY);

        gui.enableScissor(scrollX, scrollY, scrollX + 234, scrollY + 113);

        gui.pose().pushMatrix();
        gui.pose().translate(scrollX, scrollY);
        int j = 0;

        for (SideTile tile : this.tiles) {
            tile.drawSide(gui, 0, j);
            j += SideTile.HEIGHT;
        }
        gui.pose().popMatrix();
        gui.disableScissor();
    }
}
