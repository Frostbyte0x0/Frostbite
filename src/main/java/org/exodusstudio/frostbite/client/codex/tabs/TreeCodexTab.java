package org.exodusstudio.frostbite.client.codex.tabs;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.codex.entries.CodexWidget;
import org.exodusstudio.frostbite.client.codex.entries.TargetCodexEntry;
import org.exodusstudio.frostbite.client.codex.formations.CodexFormation;
import org.exodusstudio.frostbite.common.util.Util;

import java.util.*;

public class TreeCodexTab extends CodexTab {
    private final Map<TargetCodexEntry, CodexWidget> widgets;
    public final Set<CodexFormation> formations = new HashSet<>();
    private float zoom = 1;
    private static final int initialMinX = -700;
    private static final int initialMinY = -300;
    private static final int initialMaxX = 700;
    private static final int initialMaxY = 300;
    private boolean isInitialized = false;
    private TargetCodexEntry[] targetEntries;

    public TreeCodexTab(String id, TabType type, int index, String icon, TargetCodexEntry... entries) {
        super(id, type, index, icon, entries);
        this.widgets = new HashMap<>();
        this.minX = initialMinX;
        this.minY = initialMinY;
        this.maxX = initialMaxX;
        this.maxY = initialMaxY;
        this.targetEntries = entries;
    }

    public void tryInit() {
        if (isInitialized) return;

        for (TargetCodexEntry entry : targetEntries) {
            CodexWidget widget = new CodexWidget(entry);
            widget.setTab(this);
            widget.parent = this.widgets.get(entry.parent);
            widget.codexFormation = entry.formation;
            widget.codexFormation.addWidget(widget);
            formations.add(widget.codexFormation);
            this.widgets.put(entry, widget);
        }

        for (CodexFormation formation : formations) {
            formation.computePlacements();
        }
        for (CodexWidget widget : this.widgets.values()) {
            this.findMinMaxXY(widget);
        }
        isInitialized = true;
    }

    @Override
    public void drawTooltips(GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, int width, int height) {
        super.drawTooltips(GuiGraphicsExtractor, mouseX, mouseY, width, height);
        tryInit();

        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        if (mouseX > 0 && mouseX < 234 && mouseY > 0 && mouseY < 113) {
            for (CodexWidget widget : this.widgets.values()) {
                if (widget.isMouseOver(i, j, mouseX, mouseY, zoom)) {
                    increaseFade = true;
                    widget.drawHover(GuiGraphicsExtractor, i, j, width, zoom);
                    return;
                }
            }
            increaseFade = false;
        }
    }

    private void findMinMaxXY(CodexWidget widget) {
        int i = widget.getX();
        int j = i + 28;
        int k = widget.getY();
        int l = k + 27;
        this.minX = Math.min(this.minX, i);
        this.maxX = Math.max(this.maxX, j);
        this.minY = Math.min(this.minY, k);
        this.maxY = Math.max(this.maxY, l);
    }

    @Override
    public void drawContents(GuiGraphicsExtractor GuiGraphicsExtractor, int scrollX, int scrollY) {
        super.drawContents(GuiGraphicsExtractor, scrollX, scrollY);
        tryInit();

        GuiGraphicsExtractor.enableScissor(scrollX, scrollY, scrollX + 234, scrollY + 113);
        GuiGraphicsExtractor.pose().pushMatrix();
        GuiGraphicsExtractor.pose().translate(scrollX, scrollY);
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);

        int s = (int) (16 * zoom);
        for (int x = -s; x < 234 + s; x += s) {
            for (int y = -s; y < 113 + s; y += s) {
                Util.drawTexture(GuiGraphicsExtractor, x + (i % s), y + (j % s), s, s,
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/block/misty_log.png"));
            }
        }

        for (CodexWidget widget : this.widgets.values()) {
            widget.drawConnectivity(GuiGraphicsExtractor, i, j, zoom);
        }
        for (CodexWidget widget : this.widgets.values()) {
            widget.draw(GuiGraphicsExtractor, i, j, zoom);
        }
        GuiGraphicsExtractor.pose().popMatrix();
        GuiGraphicsExtractor.disableScissor();
    }

    public void zoom(double amount) {
        zoom = (float) Mth.clamp(zoom + amount * 0.1f, 0.5f, 1f);
        this.minX = (int) (initialMinX * zoom);
        this.minY = (int) (initialMinY * zoom);
        this.maxX = (int) (initialMaxX * zoom);
        this.maxY = (int) (initialMaxY * zoom);
    }
}
