package org.exodusstudio.frostbite.client.codex.tabs;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.gui.CodexScreen;

public class CodexTab {
    protected CodexScreen screen;
    protected final CodexTabType type;
    protected final int index;
    protected final Identifier icon;
    protected final Component title;
    protected double scrollX;
    protected double scrollY;
    protected int minX;
    protected int minY;
    protected int maxX;
    protected int maxY;
    protected float fade;
    protected boolean centered;
    protected boolean increaseFade = false;

    public CodexTab(String title, CodexTabType type, int index, String icon) {
        this.minX = Integer.MAX_VALUE;
        this.minY = Integer.MAX_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxY = Integer.MIN_VALUE;
        this.type = type;
        this.index = index;
        this.icon = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/" + icon + ".png");
        this.title = Component.literal(title);
    }

    public CodexTabType getType() {
        return this.type;
    }

    public Component getTitle() {
        return this.title;
    }

    public void drawTab(GuiGraphics graphics, int p_282721_, int p_282964_, int p_470594_, int p_470666_, boolean p_283052_) {
        int i = p_282721_ + this.type.getX(this.index);
        int j = p_282964_ + this.type.getY(this.index);
        this.type.draw(graphics, i, j, p_283052_, this.index);
        if (!p_283052_ && p_470594_ > i && p_470666_ > j && p_470594_ < i + this.type.getWidth() && p_470666_ < j + this.type.getHeight()) {
            graphics.requestCursor(CursorTypes.POINTING_HAND);
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
    }

    public void drawTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height) {
        guiGraphics.fill(0, 0, 234, 113, Mth.floor(this.fade * 255.0F) << 24);

        if (increaseFade) {
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

    public void setScreen(CodexScreen screen) {
        this.screen = screen;
    }

    public CodexScreen getScreen() {
        return this.screen;
    }
}
