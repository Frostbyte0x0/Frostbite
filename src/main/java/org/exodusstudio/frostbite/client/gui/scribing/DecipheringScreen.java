package org.exodusstudio.frostbite.client.gui.scribing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.inventory.AbstractRecipeBookScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;

public class DecipheringScreen extends AbstractRecipeBookScreen<DecipheringMenu> {
    private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/anvil/error");
    private static final Identifier BURN_PROGRESS_SPRITE = Identifier.withDefaultNamespace("container/furnace/burn_progress");
    private static final Identifier SCRIBING_TABLE_LOCATION = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/gui/deciphering_table.png");
    private final Player player;

    public DecipheringScreen(DecipheringMenu menu, Inventory playerInventory, Component title) {
        super(menu, new BSRecipeBookComponent(menu), playerInventory, title);
        this.player = playerInventory.player;
        this.titleLabelX = (180 - Minecraft.getInstance().font.width(title)) / 2;
        this.titleLabelY = -Minecraft.getInstance().font.lineHeight - 2;
    }

    @Override
    protected ScreenPosition getRecipeBookButtonPosition() {
        return new ScreenPosition(-100, -100);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == 256) {
            this.player.closeContainer();
        }
        return super.keyPressed(event);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        int xo = this.leftPos;
        int yo = this.topPos;
        graphics.blit(RenderPipelines.GUI_TEXTURED, SCRIBING_TABLE_LOCATION, xo, yo, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        if (!this.menu.canCraft()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, xo + 78, yo + 32, 28, 21);
        }

        int burnProgressWidth = Mth.ceil(this.menu.getDecipheringProgress() * 24.0F);
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, BURN_PROGRESS_SPRITE, 24, 16, 0, 0,
                xo + 79, yo + 34, burnProgressWidth, 16);
    }
}
