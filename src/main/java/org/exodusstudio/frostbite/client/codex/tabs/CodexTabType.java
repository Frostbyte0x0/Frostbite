package org.exodusstudio.frostbite.client.codex.tabs;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public enum CodexTabType {
    ABOVE(
            new Sprites(
                    Identifier.withDefaultNamespace("advancements/tab_above_left_selected"),
                    Identifier.withDefaultNamespace("advancements/tab_above_middle_selected"),
                    Identifier.withDefaultNamespace("advancements/tab_above_right_selected")),
            new Sprites(
                    Identifier.withDefaultNamespace("advancements/tab_above_left"),
                    Identifier.withDefaultNamespace("advancements/tab_above_middle"),
                    Identifier.withDefaultNamespace("advancements/tab_above_right")), 28, 32, 8),
    BELOW(
            new Sprites(
                    Identifier.withDefaultNamespace("advancements/tab_below_left_selected"),
                    Identifier.withDefaultNamespace("advancements/tab_below_middle_selected"),
                    Identifier.withDefaultNamespace("advancements/tab_below_right_selected")),
            new Sprites(
                    Identifier.withDefaultNamespace("advancements/tab_below_left"),
                    Identifier.withDefaultNamespace("advancements/tab_below_middle"),
                    Identifier.withDefaultNamespace("advancements/tab_below_right")), 28, 32, 8),
    LEFT(
            new Sprites(
                    Identifier.withDefaultNamespace("advancements/tab_left_top_selected"),
                    Identifier.withDefaultNamespace("advancements/tab_left_middle_selected"),
                    Identifier.withDefaultNamespace("advancements/tab_left_bottom_selected")),
            new Sprites(
                    Identifier.withDefaultNamespace("advancements/tab_left_top"),
                    Identifier.withDefaultNamespace("advancements/tab_left_middle"),
                    Identifier.withDefaultNamespace("advancements/tab_left_bottom")), 32, 28, 5),
    RIGHT(
            new Sprites(
                    Identifier.withDefaultNamespace("advancements/tab_right_top_selected"),
                    Identifier.withDefaultNamespace("advancements/tab_right_middle_selected"),
                    Identifier.withDefaultNamespace("advancements/tab_right_bottom_selected")),
            new Sprites(
                    Identifier.withDefaultNamespace("advancements/tab_right_top"),
                    Identifier.withDefaultNamespace("advancements/tab_right_middle"),
                    Identifier.withDefaultNamespace("advancements/tab_right_bottom")), 32, 28, 5);

    private final Sprites selectedSprites;
    private final Sprites unselectedSprites;
    public static final int MAX_TABS = Arrays.stream(values()).mapToInt(CodexTabType::getMax).sum();
    private final int width;
    private final int height;
    private final int max;

    CodexTabType(Sprites selectedSprites, Sprites unselectedSprites, int width, int height, int max) {
        this.selectedSprites = selectedSprites;
        this.unselectedSprites = unselectedSprites;
        this.width = width;
        this.height = height;
        this.max = max;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getMax() {
        return this.max;
    }

    public void draw(GuiGraphics guiGraphics, int offsetX, int offsetY, boolean isSelected, int index) {
        Sprites advancementtabtype$sprites = isSelected ? this.selectedSprites : this.unselectedSprites;
        Identifier identifier;
        if (index == 0) {
            identifier = advancementtabtype$sprites.first();
        } else if (index == this.max - 1) {
            identifier = advancementtabtype$sprites.last();
        } else {
            identifier = advancementtabtype$sprites.middle();
        }

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, identifier, offsetX, offsetY, this.width, this.height);
    }

    public void drawIcon(GuiGraphics guiGraphics, int offsetX, int offsetY, int index, ItemStack stack) {
        int i = offsetX + this.getX(index);
        int j = offsetY + this.getY(index);
        switch (this.ordinal()) {
            case 0:
                i += 6;
                j += 9;
                break;
            case 1:
                i += 6;
                j += 6;
                break;
            case 2:
                i += 10;
                j += 5;
                break;
            case 3:
                i += 6;
                j += 5;
        }

        guiGraphics.renderFakeItem(stack, i, j);
    }

    public int getX(int index) {
        switch (this.ordinal()) {
            case 0 -> {
                return (this.width + 4) * index;
            }
            case 1 -> {
                return (this.width + 4) * index;
            }
            case 2 -> {
                return -this.width + 4;
            }
            case 3 -> {
                return 248;
            }
            default -> throw new UnsupportedOperationException("Don't know what this tab type is!" + this);
        }
    }

    public int getY(int index) {
        switch (this.ordinal()) {
            case 0 -> {
                return -this.height + 4;
            }
            case 1 -> {
                return 136;
            }
            case 2 -> {
                return this.height * index;
            }
            case 3 -> {
                return this.height * index;
            }
            default -> throw new UnsupportedOperationException("Don't know what this tab type is!" + String.valueOf(this));
        }
    }

    public boolean isMouseOver(int offsetX, int offsetY, int index, double mouseX, double mouseY) {
        int i = offsetX + this.getX(index);
        int j = offsetY + this.getY(index);
        return mouseX > (double)i && mouseX < (double)(i + this.width) && mouseY > (double)j && mouseY < (double)(j + this.height);
    }

    record Sprites(Identifier first, Identifier middle, Identifier last) {
    }
}
