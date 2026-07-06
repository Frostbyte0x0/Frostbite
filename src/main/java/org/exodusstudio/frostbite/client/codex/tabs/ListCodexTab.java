package org.exodusstudio.frostbite.client.codex.tabs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.exodusstudio.frostbite.client.codex.entries.CodexEntry;
import org.exodusstudio.frostbite.client.codex.entries.ListCodexEntry;
import org.exodusstudio.frostbite.client.codex.entries.SideTile;

import java.util.Arrays;

public class ListCodexTab extends CodexTab {
    private static final int SCROLL_BAR_WIDTH = 5;
    private SideTile[] tiles;
    private SideTile selectedTile;
    private float sideScrollAmount = 0;
    private float contentScrollAmount = 0;
    private final ListCodexEntry[] listEntries;

    public ListCodexTab(String id, CodexTabType type, int index, String icon, ListCodexEntry... entries) {
        super(id, type, index, icon, entries);
        this.listEntries = entries;
    }

    @Override
    public void drawContents(GuiGraphicsExtractor gui, int scrollX, int scrollY) {
        super.drawContents(gui, scrollX, scrollY);

        if (tiles == null) {
            reloadTiles();
        }

        gui.enableScissor(scrollX, scrollY, scrollX + 234, scrollY + 113);

        gui.pose().pushMatrix();
        gui.pose().translate(scrollX, scrollY);
        gui.fill(0, 0, 234, 113, 0xFFC6C6C6);

        if (selectedTile == null) {
            gui.pose().popMatrix();
            gui.disableScissor();
            return;
        }

        int j = 0;

        for (SideTile tile : this.tiles) {
            int sideAddY = tiles.length * SideTile.HEIGHT > 113 ? (int) (sideScrollAmount * (113 - tiles.length * SideTile.HEIGHT)) : 0;
            tile.drawSide(gui, 0, j + sideAddY);
            j += SideTile.HEIGHT;
        }

        gui.fill(SideTile.WIDTH, 0, SideTile.WIDTH + SCROLL_BAR_WIDTH, 113, 0xFF777777);
        int sideScrollBarHeight = tiles.length * SideTile.HEIGHT > 113 ? 113 * 113 / (tiles.length * SideTile.HEIGHT) : 113;
        int sideScrollBarStart = (int) (sideScrollAmount * (113 - sideScrollBarHeight));
        gui.fill(SideTile.WIDTH, sideScrollBarStart, SideTile.WIDTH + SCROLL_BAR_WIDTH, sideScrollBarStart + sideScrollBarHeight, 0xFFAAAAAA);

        int contentAddY = selectedTile.getContentHeight() > 113 ? (int) (contentScrollAmount * (113 - selectedTile.getContentHeight())) : 0;
        selectedTile.drawContent(gui, SideTile.WIDTH + SCROLL_BAR_WIDTH, contentAddY);

        gui.fill(234 - SCROLL_BAR_WIDTH, 0, 234, 113, 0xFF777777);
        int contentScrollBarHeight = selectedTile.getContentHeight() > 113 ? 113 * 113 / (selectedTile.getContentHeight()) : 113;
        int contentScrollBarStart = (int) (contentScrollAmount * (113 - contentScrollBarHeight));
        gui.fill(234 - SCROLL_BAR_WIDTH, contentScrollBarStart, 234, contentScrollBarStart + contentScrollBarHeight, 0xFFAAAAAA);

        gui.pose().popMatrix();
        gui.disableScissor();
    }

    public void reloadTiles() {
        this.tiles = Arrays.stream(listEntries)
                .filter(e -> CodexEntry.playerHasEntry(Minecraft.getInstance().player, e))
                .map(SideTile::new)
                .toArray(SideTile[]::new);
        this.selectedTile = tiles.length > 0 ? tiles[0] : null;
    }

    public void selectTile(int mouseX, int mouseY) {
        int fromX = (screen.width - 234) / 2;
        int fromY = (screen.height - 113) / 2;

        if (!isMouseInside(mouseX, mouseY)) return;

        if ((mouseX - fromX) < SideTile.WIDTH) {
            int addY = tiles.length * SideTile.HEIGHT > 113 ? (int) (sideScrollAmount * (tiles.length * SideTile.HEIGHT - 113)) : 0;
            double j = (mouseY - fromY) + addY - 5;
            int index = (int) Math.floor(j / SideTile.HEIGHT);
            if (index < tiles.length) selectedTile = tiles[index];
        }
    }

    @Override
    public void scroll(double mouseX, double mouseY, double dragX, double dragY) {
        if (selectedTile == null) return;

        int fromX = (screen.width - 234) / 2;

        if (!isMouseInside((int) mouseX, (int) mouseY)) return;

        if ((mouseX - fromX) > SideTile.WIDTH + SCROLL_BAR_WIDTH) {
            if (selectedTile.getContentHeight() > 113) {
                contentScrollAmount -= (float) (dragY / (selectedTile.getContentHeight() - 113));
                contentScrollAmount = Math.clamp(contentScrollAmount, 0, 1);
            }
        } else {
            if (tiles.length * SideTile.HEIGHT > 113) {
                sideScrollAmount -= (float) (dragY / (tiles.length * SideTile.HEIGHT - 113));
                sideScrollAmount = Math.clamp(sideScrollAmount, 0, 1);
            }
        }
    }
}
