package org.exodusstudio.frostbite.client.codex.tabs;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.client.codex.CodexTabType;
import org.exodusstudio.frostbite.client.codex.CodexWidget;
import org.exodusstudio.frostbite.client.gui.CodexScreen;

import java.util.ArrayList;
import java.util.List;

public class CodexTab {
    private CodexScreen screen;
    private final CodexTabType type;
    private final int index;
    private final ItemStack icon;
    private final Component title;
    private final List<CodexWidget> widgets;
    private double scrollX;
    private double scrollY;
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;
    private float fade;
    private boolean centered;

    public CodexTab(String title, CodexTabType type, int index, ItemStack icon) {
        this.widgets = new ArrayList<>();
        this.minX = Integer.MAX_VALUE;
        this.minY = Integer.MAX_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxY = Integer.MIN_VALUE;
        this.type = type;
        this.index = index;
        this.icon = icon;
        this.title = Component.literal(title);
    }

    public CodexTabType getType() {
        return this.type;
    }

    public Component getTitle() {
        return this.title;
    }

    public void drawTab(GuiGraphics p_282671_, int p_282721_, int p_282964_, int p_470594_, int p_470666_, boolean p_283052_) {
        int i = p_282721_ + this.type.getX(this.index);
        int j = p_282964_ + this.type.getY(this.index);
        this.type.draw(p_282671_, i, j, p_283052_, this.index);
        if (!p_283052_ && p_470594_ > i && p_470666_ > j && p_470594_ < i + this.type.getWidth() && p_470666_ < j + this.type.getHeight()) {
            p_282671_.requestCursor(CursorTypes.POINTING_HAND);
        }

    }

    public void drawIcon(GuiGraphics guiGraphics, int offsetX, int offsetY) {
        this.type.drawIcon(guiGraphics, offsetX, offsetY, this.index, this.icon);
    }

    public void drawContents(GuiGraphics guiGraphics, int x, int y) {
        if (!this.centered) {
            this.scrollX = 117 - (this.maxX + this.minX) / 2f;
            this.scrollY = 56 - (this.maxY + this.minY) / 2f;
            this.centered = true;
        }

        guiGraphics.enableScissor(x, y, x + 234, y + 113);
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate((float)x, (float)y);
        //Identifier identifier = this.display.getBackground().map(ClientAsset.ResourceTexture::texturePath).orElse(TextureManager.INTENTIONAL_MISSING_TEXTURE);
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        int k = i % 16;
        int l = j % 16;

        for (int i1 = -1; i1 <= 15; ++i1) {
            for (int j1 = -1; j1 <= 8; ++j1) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TextureManager.INTENTIONAL_MISSING_TEXTURE, k + 16 * i1, l + 16 * j1, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

//        this.root.drawConnectivity(guiGraphics, i, j, true);
//        this.root.drawConnectivity(guiGraphics, i, j, false);
//        this.root.draw(guiGraphics, i, j);
        guiGraphics.pose().popMatrix();
        guiGraphics.disableScissor();
    }

    public void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height) {
        guiGraphics.fill(0, 0, 234, 113, Mth.floor(this.fade * 255.0F) << 24);
        boolean flag = false;
        int i = Mth.floor(this.scrollX);
        int j = Mth.floor(this.scrollY);
        if (mouseX > 0 && mouseX < 234 && mouseY > 0 && mouseY < 113) {
            for (CodexWidget widget : this.widgets) {
                if (widget.isMouseOver(i, j, mouseX, mouseY)) {
                    flag = true;
                    widget.drawHover(guiGraphics, i, j, this.fade, width, height);
                    break;
                }
            }
        }

        if (flag) {
            this.fade = Mth.clamp(this.fade + 0.02F, 0.0F, 0.3F);
        } else {
            this.fade = Mth.clamp(this.fade - 0.04F, 0.0F, 1.0F);
        }

    }

    public boolean isMouseOver(int offsetX, int offsetY, double mouseX, double mouseY) {
        return this.type.isMouseOver(offsetX, offsetY, this.index, mouseX, mouseY);
    }

    public void scroll(double dragX, double dragY) {
        if (this.canScrollHorizontally()) {
            this.scrollX = Mth.clamp(this.scrollX + dragX, -(this.maxX - 234), 0);
        }

        if (this.canScrollVertically()) {
            this.scrollY = Mth.clamp(this.scrollY + dragY, -(this.maxY - 113), 0);
        }
    }

    public boolean canScrollHorizontally() {
        return this.maxX - this.minX > 234;
    }

    public boolean canScrollVertically() {
        return this.maxY - this.minY > 113;
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

    public void setScreen(CodexScreen screen) {
        this.screen = screen;
    }

    public CodexScreen getScreen() {
        return this.screen;
    }
}
