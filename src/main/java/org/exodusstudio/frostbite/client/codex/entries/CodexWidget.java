package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.codex.formations.CircleCodexFormation;
import org.exodusstudio.frostbite.client.codex.formations.CodexFormation;
import org.exodusstudio.frostbite.client.codex.tabs.CodexTab;
import org.exodusstudio.frostbite.common.util.Util;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class CodexWidget {
    private static final Identifier TITLE_BOX_SPRITE = Identifier.withDefaultNamespace("advancements/title_box");
    private static final Identifier UNDISCOVERED_ENTRY = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/codex/entries/undiscovered_entry.png");
    private static final int[] TEST_SPLIT_OFFSETS = new int[]{0, 10, -10, 25, -25};
    public final TargetCodexEntry codexEntry;
    private final List<FormattedCharSequence> titleLines;
    private final int width;
    private final List<FormattedCharSequence> description;
    private final Minecraft minecraft = Minecraft.getInstance();
    public @Nullable CodexWidget parent;
    public CodexFormation codexFormation;
    private GuiGraphicsExtractor gui;
    private final Optional<Item> drops;

    public CodexWidget(TargetCodexEntry codexEntry) {
        this.codexEntry = codexEntry;
        this.titleLines = minecraft.font.split(codexEntry.title, 163);
        int i = Math.max(this.titleLines.stream().mapToInt(minecraft.font::width).max().orElse(0), 100);
        int k = 29 + i;
        this.description = Language.getInstance().getVisualOrder(findOptimalLines(ComponentUtils.mergeStyles(codexEntry.description, Style.EMPTY.withColor(ChatFormatting.AQUA)), 163));

        this.drops = codexEntry.drops;
        for (FormattedCharSequence formattedcharsequence : this.description) {
            k = Math.max(k, minecraft.font.width(formattedcharsequence));
        }

        this.width = k + 40;
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

    public void drawConnectivity(GuiGraphicsExtractor gui, int x, int y, float zoom) {
        if (this.parent != null) {
            int originX = (int) (x + (this.parent.getX() + 19) * zoom);
            int originY = (int) (y + (this.parent.getY() + 16) * zoom);
            int endX = (int) (x + (this.getX() + 19) * zoom);
            int endY = (int) (y + (this.getY() + 16) * zoom);
            int stepY = (originY + endY) / 2;
            gui.verticalLine(originX, originY, stepY, -1);
            gui.horizontalLine(originX, endX, stepY, -1);
            gui.verticalLine(endX, stepY, endY, -1);
        }

        if (codexFormation instanceof CircleCodexFormation cf) {
            int index = cf.getWidgetPlacements().sequencedKeySet().stream().toList().indexOf(this) + 1;
            CodexWidget next = cf.getWidgetPlacements().sequencedKeySet().stream().toList().get(index % cf.getWidgetPlacements().sequencedKeySet().size());
            int originX = (int) (x + (this.getX() + 19) * zoom);
            int originY = (int) (y + (this.getY() + 16) * zoom);
            int endX = (int) (x + (codexFormation.getWidgetPlacements().get(next)[0] + 19) * zoom);
            int endY = (int) (y + (codexFormation.getWidgetPlacements().get(next)[1] + 16) * zoom);
            if ((endX - originX) * (endY - originY) < 0) {
                gui.verticalLine(originX, originY, endY, -1);
                gui.horizontalLine(originX, endX, endY, -1);
            } else {
                gui.horizontalLine(originX, endX, originY, -1);
                gui.verticalLine(endX, originY, endY, -1);
            }
        }
    }

    public void draw(GuiGraphicsExtractor extractor, int x, int y, float zoom) {
        gui = extractor;
        Util.drawTexture(gui, (int) (x + (getX() + 8) * zoom), (int) (y + (getY() + 5) * zoom), (int) (24 * zoom), (int) (24 * zoom), getImage());
    }

    public Identifier getImage() {
        return CodexEntry.playerHasEntry(Minecraft.getInstance().player, codexEntry) ? codexEntry.image : UNDISCOVERED_ENTRY;
    }

    public int getWidth() {
        return this.width;
    }

    public void drawHover(GuiGraphicsExtractor gui, int scrollX, int scrollY, int width, float zoom) {
        int i = 9 * this.titleLines.size() + 5;
        int l = this.description.size() * 9;

        int j2 = i + 32 + l;

        float sz = (1 - zoom) * 2;

        int boxX = (int) (scrollX + (getX() + 8 * (1 - sz)) * zoom - 3 * sz);
        int boxY = (int) (scrollY + (getY() + 5 * (1 - sz)) * zoom - 3 * sz);
        if (!this.description.isEmpty()) {
            gui.blitSprite(RenderPipelines.GUI_TEXTURED, TITLE_BOX_SPRITE, boxX - 6, boxY - 8, this.width, j2);
        }
        this.drops.ifPresent(item -> gui.item(item.getDefaultInstance(), boxX + 4, boxY + 29));

        Util.drawMultilineText(gui, this.titleLines, boxX + 30, boxY + 1, 0xFF5836e0);
        Util.drawMultilineText(gui, this.description, boxX + 30, boxY + i, -16711936);

        Util.drawTexture(gui, (int) (scrollX + (8 * (1 - sz) + getX()) * zoom - 3 * sz), (int) (scrollY + (5 * (1 - sz) + getY()) * zoom - 3 * sz), 24, 24, getImage());
    }

    public boolean isMouseOver(int x, int y, int mouseX, int mouseY, float zoom) {
        int x0 = (int) (x + (8 + getX()) * zoom);
        int x1 = (int) (x0 + 24 * zoom);
        int y0 = (int) (y + (4 + getY()) * zoom);
        int y1 = (int) (y0 + 24 * zoom);

//            gui.verticalLine(x0, y0, y1, -1);
//            gui.verticalLine(x1, y0, y1, -1);
//            gui.horizontalLine(x0, x1, y0, -1);
//            gui.horizontalLine(x0, x1, y1, -1);


        return mouseX >= x0 && mouseX <= x1 && mouseY >= y0 && mouseY <= y1;
    }

    public int getY() {
        return codexFormation.getYPlacement(this);
    }

    public int getX() {
        return codexFormation.getXPlacement(this);
    }

    public int getDepth() {
        if (parent == null) return 0;
        return parent.getDepth() + 1;
    }

    public static int getNearestSharedParentDepth(CodexWidget w1, CodexWidget w2) {
        int depth = 1;
        while (w1.parent != null && w2.parent != null) {
            if (w1.parent == w2.parent) {
                break;
            }
            depth++;
            w1 = w1.parent;
            w2 = w2.parent;
        }
        return depth;
    }
}
