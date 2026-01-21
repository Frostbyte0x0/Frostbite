package org.exodusstudio.frostbite.client.codex.entries;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.codex.formations.CodexFormation;
import org.exodusstudio.frostbite.client.codex.tabs.CodexTab;
import org.exodusstudio.frostbite.common.util.Util;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class CodexWidget {
    private static final Identifier TITLE_BOX_SPRITE = Identifier.withDefaultNamespace("advancements/title_box");
    private static final Identifier UNDISCOVERED_ENTRY = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/codex/entries/undiscovered_entry.png");
    private static final int[] TEST_SPLIT_OFFSETS = new int[]{0, 10, -10, 25, -25};
    private CodexTab tab;
    private final TargetCodexEntry codexEntry;
    private final List<FormattedCharSequence> titleLines;
    private final int width;
    private final List<FormattedCharSequence> description;
    private final Minecraft minecraft = Minecraft.getInstance();
    private @Nullable CodexWidget parent;
    private final List<CodexWidget> children = Lists.newArrayList();
    public CodexFormation codexFormation;

    public CodexWidget(TargetCodexEntry codexEntry) {
        this.codexEntry = codexEntry;
        this.titleLines = minecraft.font.split(codexEntry.title, 163);
        Stream<FormattedCharSequence> var10000 = this.titleLines.stream();
        Font var10001 = minecraft.font;
        Objects.requireNonNull(var10001);
        int i = Math.max(var10000.mapToInt(var10001::width).max().orElse(0), 80);
        int k = 29 + i;
        this.description = Language.getInstance().getVisualOrder(findOptimalLines(ComponentUtils.mergeStyles(codexEntry.description, Style.EMPTY.withColor(ChatFormatting.AQUA)), k));

        for (FormattedCharSequence formattedcharsequence : this.description) {
            k = Math.max(k, minecraft.font.width(formattedcharsequence));
        }

        this.width = k + 3 + 5;
    }

    private static float getMaxWidth(StringSplitter manager, List<FormattedText> text) {
        Stream<FormattedText> var10000 = text.stream();
        Objects.requireNonNull(manager);
        return (float)var10000.mapToDouble(manager::stringWidth).max().orElse(0);
    }

    private List<FormattedText> findOptimalLines(Component component, int maxWidth) {
        StringSplitter stringsplitter = this.minecraft.font.getSplitter();
        List<FormattedText> list = null;
        float f = Float.MAX_VALUE;

        for (int i : TEST_SPLIT_OFFSETS) {
            List<FormattedText> list1 = stringsplitter.splitLines(component, maxWidth - i, Style.EMPTY);
            float f1 = Math.abs(getMaxWidth(stringsplitter, list1) - (float)maxWidth);
            if (f1 <= 10.0F) {
                return list1;
            }

            if (f1 < f) {
                f = f1;
                list = list1;
            }
        }

        return list;
    }

    public void drawConnectivity(GuiGraphics guiGraphics, int x, int y, boolean dropShadow) {
        if (this.parent != null) {
            int i = x + this.parent.getX() + 13;
            int j = x + this.parent.getX() + 26 + 4;
            int k = y + this.parent.getY() + 13;
            int l = x + this.getX() + 13;
            int i1 = y + getY() + 13;
            int j1 = dropShadow ? -16777216 : -1;
            if (dropShadow) {
                guiGraphics.hLine(j, i, k - 1, j1);
                guiGraphics.hLine(j + 1, i, k, j1);
                guiGraphics.hLine(j, i, k + 1, j1);
                guiGraphics.hLine(l, j - 1, i1 - 1, j1);
                guiGraphics.hLine(l, j - 1, i1, j1);
                guiGraphics.hLine(l, j - 1, i1 + 1, j1);
                guiGraphics.vLine(j - 1, i1, k, j1);
                guiGraphics.vLine(j + 1, i1, k, j1);
            } else {
                guiGraphics.hLine(j, i, k, j1);
                guiGraphics.hLine(l, j, i1, j1);
                guiGraphics.vLine(j, i1, k, j1);
            }
        }

        for (CodexWidget widget : this.children) {
            widget.drawConnectivity(guiGraphics, x, y, dropShadow);
        }
    }

    public void draw(GuiGraphics guiGraphics, int x, int y) {
        Util.drawTexture(guiGraphics, x + getX() + 8, y + getY() + 5, 24, 24, getImage());

        for (CodexWidget widget : this.children) {
            widget.draw(guiGraphics, x, y);
        }
    }

    public Identifier getImage() {
        return CodexEntry.playerHasEntry(Minecraft.getInstance().player, codexEntry) ? codexEntry.image : UNDISCOVERED_ENTRY;
    }

    public int getWidth() {
        return this.width;
    }

    public void setTab(CodexTab tabs) {
        this.tab = tabs;
    }

    public void addChild(CodexWidget advancementWidget) {
        this.children.add(advancementWidget);
    }

    public void drawHover(GuiGraphics guiGraphics, int x, int y, int width) {
        int i = 9 * this.titleLines.size() + 9 + 8;
        int j = y + getY() + (24 - i) / 2;
        int k = j + i;
        int l = this.description.size() * 9;
        int i1 = 6 + l;
        boolean flag = width + x + getX() + this.width + 24 >= this.tab.getScreen().width;
        boolean flag1 = k + i1 >= 113;

        int i2;
        if (flag) {
            i2 = x + getX() - this.width + 24 + 6;
        } else {
            i2 = x + getX();
        }

        int j2 = i + i1;
        if (!this.description.isEmpty()) {
            if (flag1) {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, TITLE_BOX_SPRITE, i2 + 3, k - j2 - 3, this.width, j2);
            } else {
                guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, TITLE_BOX_SPRITE, i2 + 3, j - 3, this.width, j2);
            }
        }

        int k2 = i2 + 5;

        if (flag1) {
            this.drawMultilineText(guiGraphics, this.description, k2 + 30, j - l - 19, -16711936);
        } else {
            this.drawMultilineText(guiGraphics, this.description, k2 + 30, k - 20, -16711936);
        }
        Util.drawTexture(guiGraphics, x + getX() + 8, y + getY() + 5, 24, 24, getImage());
    }

    private void drawMultilineText(GuiGraphics guiGraphics, List<FormattedCharSequence> text, int x, int y, int color) {
        Font font = this.minecraft.font;

        for (int i = 0; i < text.size(); ++i) {
            guiGraphics.drawString(font, text.get(i), x, y + i * 9, color);
        }
    }

    public boolean isMouseOver(int x, int y, int mouseX, int mouseY) {
        int i = x + getX();
        int j = i + 26;
        int k = y + getY();
        int l = k + 26;
        return mouseX >= i && mouseX <= j && mouseY >= k && mouseY <= l;
    }

    public void attachToParent() {
        if (this.parent == null && this.codexEntry.parent != null) {
            //this.parent = codexEntry.parent;
            if (this.parent != null) {
                this.parent.addChild(this);
            }
        }
    }

    public int getY() {
        return codexFormation.getYPlacement(this);
    }

    public int getX() {
        return codexFormation.getXPlacement(this);
    }
}
