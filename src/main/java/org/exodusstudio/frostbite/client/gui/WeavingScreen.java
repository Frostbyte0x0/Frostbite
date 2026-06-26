package org.exodusstudio.frostbite.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;

public class WeavingScreen extends ItemCombinerScreen<WeavingMenu> {
    private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/anvil/error");
    private static final Identifier WEAVING_TABLE_LOCATION = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/gui/weaving_table.png");
    private final Player player;

    public WeavingScreen(WeavingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, WEAVING_TABLE_LOCATION);
        this.player = playerInventory.player;
        this.titleLabelX = 60;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == 256) {
            this.player.closeContainer();
        }
        return super.keyPressed(event);
    }

    @Override
    protected void extractErrorIcon(GuiGraphicsExtractor graphics, int i, int i1) {
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(this.menu.getResultSlot()).hasItem()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, i + 99, i1 + 45, 28, 21);
        }
    }
}
