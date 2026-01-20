package org.exodusstudio.frostbite.client.gui;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundSeenAdvancementsPacket;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.client.codex.Codex;
import org.exodusstudio.frostbite.client.codex.tabs.CodexTab;
import org.exodusstudio.frostbite.common.registry.KeyMappingRegistry;
import org.jspecify.annotations.Nullable;

public class CodexScreen extends Screen {
    private static final Identifier WINDOW_LOCATION = Identifier.withDefaultNamespace("textures/gui/advancements/window.png");
    private static final Component VERY_SAD_LABEL = Component.translatable("advancements.sad_label");
    private static final Component NO_ADVANCEMENTS_LABEL = Component.translatable("advancements.empty");
    private static final Component TITLE = Component.translatable("gui.codex");
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private @Nullable CodexTab selectedTab;
    private boolean isScrolling;

    public CodexScreen() {
        super(TITLE);
    }

    @Override
    protected void init() {
        this.layout.addTitleHeader(TITLE, this.font);
        this.selectedTab = Codex.GENERAL_TAB;

        Codex.TABS.forEach(t -> t.setScreen(this));

        this.layout.addToFooter(Button.builder(CommonComponents.GUI_DONE, p_331557_ -> this.onClose()).width(200).build());
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
    }

    @Override
    public void removed() {
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

            for (CodexTab tab : Codex.TABS) {
                if (tab.isMouseOver(i, j, event.x(), event.y())) {
                    selectedTab = tab;
                    break;
                }
            }
        }

        return super.mouseClicked(event, p_433867_);
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        if (keyEvent.key() == 256) {
            Minecraft.getInstance().player.closeContainer();
        }

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
        CodexTab tab = this.selectedTab;
        if (tab == null) {
            guiGraphics.fill(x + 9, y + 18, x + 9 + 234, y + 18 + 113, -16777216);
            int i = x + 9 + 117;
            guiGraphics.drawCenteredString(this.font, NO_ADVANCEMENTS_LABEL, i, y + 18 + 56 - 9 / 2, -1);
            guiGraphics.drawCenteredString(this.font, VERY_SAD_LABEL, i, y + 18 + 113 - 9, -1);
        } else {
            tab.drawContents(guiGraphics, x + 9, y + 18);
        }
    }

    public void renderWindow(GuiGraphics guiGraphics, int offsetX, int offsetY, int p_470848_, int p_470691_) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, WINDOW_LOCATION, offsetX, offsetY, 0.0F, 0.0F, 252, 140, 256, 256);
        for (CodexTab tab : Codex.TABS) {
            tab.drawTab(guiGraphics, offsetX, offsetY, p_470848_, p_470691_, tab == this.selectedTab);
        }

        for (CodexTab tab : Codex.TABS) {
            tab.drawIcon(guiGraphics, offsetX, offsetY);
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

        for (CodexTab tab : Codex.TABS) {
            if (tab.isMouseOver(offsetX, offsetY, mouseX, mouseY)) {
                guiGraphics.setTooltipForNextFrame(this.font, tab.getTitle(), mouseX, mouseY);
            }
        }
    }
}
