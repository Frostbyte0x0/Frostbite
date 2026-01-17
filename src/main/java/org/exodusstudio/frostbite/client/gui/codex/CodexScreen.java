package org.exodusstudio.frostbite.client.gui.codex;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.common.registry.KeyMappingRegistry;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class CodexScreen extends Screen implements ClientAdvancements.Listener {
    private static final Identifier WINDOW_LOCATION = Identifier.withDefaultNamespace("textures/gui/advancements/window.png");
    public static final int WINDOW_WIDTH = 252;
    public static final int WINDOW_HEIGHT = 140;
    private static final int WINDOW_INSIDE_X = 9;
    private static final int WINDOW_INSIDE_Y = 18;
    public static final int WINDOW_INSIDE_WIDTH = 234;
    public static final int WINDOW_INSIDE_HEIGHT = 113;
    private static final int WINDOW_TITLE_X = 8;
    private static final int WINDOW_TITLE_Y = 6;
    private static final int BACKGROUND_TEXTURE_WIDTH = 256;
    private static final int BACKGROUND_TEXTURE_HEIGHT = 256;
    public static final int BACKGROUND_TILE_WIDTH = 16;
    public static final int BACKGROUND_TILE_HEIGHT = 16;
    public static final int BACKGROUND_TILE_COUNT_X = 14;
    public static final int BACKGROUND_TILE_COUNT_Y = 7;
    private static final double SCROLL_SPEED = 16.0;
    private static final Component VERY_SAD_LABEL = Component.translatable("advancements.sad_label");
    private static final Component NO_ADVANCEMENTS_LABEL = Component.translatable("advancements.empty");
    private static final Component TITLE = Component.translatable("gui.codex");
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final @Nullable Screen lastScreen;
    private final ClientAdvancements advancements;
    private final Map<AdvancementHolder, CodexTab> tabs = Maps.newLinkedHashMap();
    private @Nullable CodexTab selectedTab;
    private boolean isScrolling;
    private static int tabPage, maxPages;

    public CodexScreen(ClientAdvancements advancements, @Nullable Screen lastScreen) {
        super(TITLE);
        this.advancements = advancements;
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(TITLE, this.font);
        this.tabs.clear();
        this.selectedTab = null;
        this.advancements.setListener(this);
        if (this.selectedTab == null && !this.tabs.isEmpty()) {
            CodexTab tab = this.tabs.values().iterator().next();
            this.advancements.setSelectedTab(tab.getRootNode().holder(), true);
        } else {
            this.advancements.setSelectedTab(this.selectedTab == null ? null : this.selectedTab.getRootNode().holder(), true);
        }

        if (this.tabs.size() > CodexTabType.MAX_TABS) {
            int guiLeft = (this.width - 252) / 2;
            int guiTop = (this.height - 140) / 2;
            addRenderableWidget(net.minecraft.client.gui.components.Button.builder(Component.literal("<"), b -> tabPage = Math.max(tabPage - 1, 0         ))
                    .pos(guiLeft, guiTop - 50).size(20, 20).build());
            addRenderableWidget(net.minecraft.client.gui.components.Button.builder(Component.literal(">"), b -> tabPage = Math.min(tabPage + 1, maxPages))
                    .pos(guiLeft + WINDOW_WIDTH - 20, guiTop - 50).size(20, 20).build());
            maxPages = this.tabs.size() / CodexTabType.MAX_TABS;
        }

        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, p_331557_ -> this.onClose()).width(200).build());
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.lastScreen);
    }

    @Override
    public void removed() {
        this.advancements.setListener(null);
        ClientPacketListener clientpacketlistener = this.minecraft.getConnection();
        if (clientpacketlistener != null) {
            clientpacketlistener.send(ServerboundSeenAdvancementsPacket.closedScreen());
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean p_433867_) {
        if (event.button() == 0) {
            int i = (this.width - 252) / 2;
            int j = (this.height - 140) / 2;

            for (CodexTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage && tab.isMouseOver(i, j, event.x(), event.y())) {
                    this.advancements.setSelectedTab(tab.getRootNode().holder(), true);
                    break;
                }
            }
        }

        return super.mouseClicked(event, p_433867_);
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (KeyMappingRegistry.CODEX.matches(keyEvent)) {
            this.minecraft.setScreen(null);
            this.minecraft.mouseHandler.grabMouse();
            return true;
        } else {
            return super.keyPressed(keyEvent);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int p_282255_, int p_283354_, float p_283123_) {
        super.render(graphics, p_282255_, p_283354_, p_283123_);
        int i = (this.width - 252) / 2;
        int j = (this.height - 140) / 2;
        if (maxPages != 0) {
            net.minecraft.network.chat.Component page = Component.literal(String.format("%d / %d", tabPage + 1, maxPages + 1));
            int width = this.font.width(page);
            graphics.drawString(this.font, page.getVisualOrderText(), i + (252 / 2) - (width / 2), j - 44, -1);
        }
        graphics.nextStratum();
        this.renderInside(graphics, i, j);
        graphics.nextStratum();
        this.renderWindow(graphics, i, j, p_282255_, p_283354_);
        if (this.isScrolling && this.selectedTab != null) {
            if (this.selectedTab.canScrollHorizontally() && this.selectedTab.canScrollVertically()) {
                graphics.requestCursor(CursorTypes.RESIZE_ALL);
            } else if (this.selectedTab.canScrollHorizontally()) {
                graphics.requestCursor(CursorTypes.RESIZE_EW);
            } else if (this.selectedTab.canScrollVertically()) {
                graphics.requestCursor(CursorTypes.RESIZE_NS);
            }
        }

        this.renderTooltips(graphics, p_282255_, p_283354_, i, j);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double p_97347_, double p_97348_) {
        if (event.button() != 0) {
            this.isScrolling = false;
            return false;
        } else {
            if (!this.isScrolling) {
                this.isScrolling = true;
            } else if (this.selectedTab != null) {
                this.selectedTab.scroll(p_97347_, p_97348_);
            }

            return true;
        }
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        this.isScrolling = false;
        return super.mouseReleased(event);
    }

    @Override
    public boolean mouseScrolled(double p_295690_, double p_295286_, double p_295339_, double p_296270_) {
        if (this.selectedTab != null) {
            this.selectedTab.scroll(p_295339_ * 16.0, p_296270_ * 16.0);
            return true;
        } else {
            return false;
        }
    }

    private void renderInside(GuiGraphics guiGraphics, int x, int y) {
        CodexTab advancementtab = this.selectedTab;
        if (advancementtab == null) {
            guiGraphics.fill(x + 9, y + 18, x + 9 + 234, y + 18 + 113, -16777216);
            int i = x + 9 + 117;
            guiGraphics.drawCenteredString(this.font, NO_ADVANCEMENTS_LABEL, i, y + 18 + 56 - 9 / 2, -1);
            guiGraphics.drawCenteredString(this.font, VERY_SAD_LABEL, i, y + 18 + 113 - 9, -1);
        } else {
            advancementtab.drawContents(guiGraphics, x + 9, y + 18);
        }
    }

    public void renderWindow(GuiGraphics guiGraphics, int offsetX, int offsetY, int p_470848_, int p_470691_) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_LOCATION, offsetX, offsetY, 0.0F, 0.0F, 252, 140, 256, 256);
        if (this.tabs.size() > 1) {
            for (CodexTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage)
                    tab.drawTab(guiGraphics, offsetX, offsetY, p_470848_, p_470691_, tab == this.selectedTab);
            }

            for (CodexTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage)
                    tab.drawIcon(guiGraphics, offsetX, offsetY);
            }
        }

        guiGraphics.drawString(this.font, this.selectedTab != null ? this.selectedTab.getTitle() : TITLE, offsetX + 8, offsetY + 6, -12566464, false);
    }

    private void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY, int offsetX, int offsetY) {
        if (this.selectedTab != null) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(offsetX + 9, offsetY + 18);
            guiGraphics.nextStratum();
            this.selectedTab.drawTooltips(guiGraphics, mouseX - offsetX - 9, mouseY - offsetY - 18, offsetX, offsetY);
            guiGraphics.pose().popMatrix();
        }

        if (this.tabs.size() > 1) {
            for (CodexTab tab : this.tabs.values()) {
                if (tab.getPage() == tabPage && tab.isMouseOver(offsetX, offsetY, mouseX, mouseY)) {
                    guiGraphics.setTooltipForNextFrame(this.font, tab.getTitle(), mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void onAddAdvancementRoot(AdvancementNode node) {
        CodexTab tab = CodexTab.create(this.minecraft, this, this.tabs.size(), node);
        if (tab != null) {
            this.tabs.put(node.holder(), tab);
        }
    }

    @Override
    public void onRemoveAdvancementRoot(AdvancementNode p_301028_) {
    }

    @Override
    public void onAddAdvancementTask(AdvancementNode node) {
        CodexTab tab = this.getTab(node);
        if (tab != null) {
            tab.addAdvancement(node);
        }
    }

    @Override
    public void onRemoveAdvancementTask(AdvancementNode p_301004_) {
    }

    @Override
    public void onUpdateAdvancementProgress(AdvancementNode node, AdvancementProgress progress) {
        CodexWidget widget = this.getAdvancementWidget(node);
        if (widget != null) {
            widget.setProgress(progress);
        }
    }

    @Override
    public void onSelectedTabChanged(@Nullable AdvancementHolder holder) {
        this.selectedTab = this.tabs.get(holder);
    }

    @Override
    public void onAdvancementsCleared() {
        this.tabs.clear();
        this.selectedTab = null;
    }

    public @Nullable CodexWidget getAdvancementWidget(AdvancementNode advancement) {
        CodexTab tab = this.getTab(advancement);
        return tab == null ? null : tab.getWidget(advancement.holder());
    }

    private @Nullable CodexTab getTab(AdvancementNode advancement) {
        AdvancementNode advancementnode = advancement.root();
        return this.tabs.get(advancementnode.holder());
    }
}
