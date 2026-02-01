package org.exodusstudio.frostbite.client.codex.tabs;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.codex.entries.CodexWidget;
import org.exodusstudio.frostbite.client.codex.entries.TargetCodexEntry;
import org.exodusstudio.frostbite.common.util.Util;

import java.util.HashMap;
import java.util.Map;

public class TreeCodexTab extends CodexTab {
    private final Map<TargetCodexEntry, CodexWidget> widgets;
    private float zoom = 1;
    private static final int initialMinX = -700;
    private static final int initialMinY = -300;
    private static final int initialMaxX = 700;
    private static final int initialMaxY = 300;

    public TreeCodexTab(String title, CodexTabType type, int index, String icon, TargetCodexEntry... entries) {
        super(title, type, index, icon);
        this.widgets = new HashMap<>();
        this.minX = initialMinX;
        this.minY = initialMinY;
        this.maxX = initialMaxX;
        this.maxY = initialMaxY;

        for (TargetCodexEntry entry : entries) {
            CodexWidget widget = new CodexWidget(entry);
            widget.setTab(this);
            widget.parent = this.widgets.get(entry.parent);
            widget.codexFormation = entry.formation;
            widget.codexFormation.addWidget(widget);
            this.widgets.put(entry, widget);
            this.addWidget(widget);
        }
    }

    @Override
    public void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height) {
        super.drawTooltips(guiGraphics, mouseX, mouseY, width, height);
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        if (mouseX > 0 && mouseX < 234 && mouseY > 0 && mouseY < 113) {
            for (CodexWidget widget : this.widgets.values()) {
                if (widget.isMouseOver(i, j, mouseX, mouseY, zoom)) {
                    increaseFade = true;
                    widget.drawHover(guiGraphics, i, j, width);
                    return;
                }
            }
            increaseFade = false;
        }
    }

    private void addWidget(CodexWidget widget) {
        int i = widget.getX();
        int j = i + 28;
        int k = widget.getY();
        int l = k + 27;
        this.minX = Math.min(this.minX, i);
        this.maxX = Math.max(this.maxX, j);
        this.minY = Math.min(this.minY, k);
        this.maxY = Math.max(this.maxY, l);
    }

    public void drawContents(GuiGraphics guiGraphics, int x, int y) {
        super.drawContents(guiGraphics, x, y);

        guiGraphics.enableScissor(x, y, x + 234, y + 113);
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        int k = i % 16;
        int l = j % 16;

        for (int i1 = -1; i1 <= 15 / zoom; ++i1) {
            for (int j1 = -1; j1 <= 8 / zoom; ++j1) {
                int s = (int) (16 * zoom);
                Util.drawTexture(guiGraphics, k + s * i1, l + s * j1, s, s,
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/block/misty_log.png"));
                //guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/block/misty_log.png"),
                //        k + 16 * i1, l + 16 * j1, 0, 0, 16, 16, 16, 16);
            }
        }

        for (CodexWidget widget : this.widgets.values()) {
            widget.drawConnectivity(guiGraphics, i, j, zoom);
        }
        for (CodexWidget widget : this.widgets.values()) {
            widget.draw(guiGraphics, i, j, zoom);
        }
        guiGraphics.pose().popMatrix();
        guiGraphics.disableScissor();
    }

    public void zoom(double amount) {
        zoom = (float) Mth.clamp(zoom + amount / 6f, 0.5, 1.5);
        this.minX = (int) (initialMinX * zoom);
        this.minY = (int) (initialMinY * zoom);
        this.maxX = (int) (initialMaxX * zoom);
        this.maxY = (int) (initialMaxY * zoom);
    }
}
