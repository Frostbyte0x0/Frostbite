package org.exodusstudio.frostbite.client.codex.tabs;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.codex.entries.CodexWidget;
import org.exodusstudio.frostbite.client.codex.entries.TargetCodexEntry;

import java.util.ArrayList;
import java.util.List;

public class TreeCodexTab extends CodexTab {
    private final List<CodexWidget> widgets;

    public TreeCodexTab(String title, CodexTabType type, int index, String icon, TargetCodexEntry... entries) {
        super(title, type, index, icon);
        this.widgets = new ArrayList<>();
        this.minX = -500;
        this.minY = -200;
        this.maxX = 500;
        this.maxY = 200;

        for (TargetCodexEntry entry : entries) {
            CodexWidget widget = new CodexWidget(entry);
            widget.setTab(this);
            widget.codexFormation = entry.formation;
            widget.codexFormation.addWidget(widget);
            this.addWidget(widget);
        }
    }

    @Override
    public void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height) {
        super.drawTooltips(guiGraphics, mouseX, mouseY, width, height);
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        if (mouseX > 0 && mouseX < 234 && mouseY > 0 && mouseY < 113) {
            for (CodexWidget widget : this.widgets) {
                if (widget.isMouseOver(i, j, mouseX, mouseY)) {
                    increaseFade = true;
                    widget.drawHover(guiGraphics, i, j, width);
                    return;
                }
            }
            increaseFade = false;
        }
    }

    private void addWidget(CodexWidget widget) {
        this.widgets.add(widget);
        int i = widget.getX();
        int j = i + 28;
        int k = widget.getY();
        int l = k + 27;
        this.minX = Math.min(this.minX, i);
        this.maxX = Math.max(this.maxX, j);
        this.minY = Math.min(this.minY, k);
        this.maxY = Math.max(this.maxY, l);

        for (CodexWidget codexWidget : this.widgets) {
            codexWidget.attachToParent();
        }
    }

    public void drawContents(GuiGraphics guiGraphics, int x, int y) {
        super.drawContents(guiGraphics, x, y);

        guiGraphics.enableScissor(x, y, x + 234, y + 113);
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate((float)x, (float)y);
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        int k = i % 16;
        int l = j % 16;

        for (int i1 = -1; i1 <= 15; ++i1) {
            for (int j1 = -1; j1 <= 8; ++j1) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/block/misty_log.png"), k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

//        this.root.drawConnectivity(guiGraphics, i, j, true);
//        this.root.drawConnectivity(guiGraphics, i, j, false);
        for (CodexWidget widget : this.widgets) {
            widget.draw(guiGraphics, i, j);
        }
        guiGraphics.pose().popMatrix();
        guiGraphics.disableScissor();
    }
}
