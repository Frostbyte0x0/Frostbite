package org.exodusstudio.frostbite.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;

public class WeavingScreen extends ItemCombinerScreen<WeavingMenu> {
    private static final ResourceLocation ERROR_SPRITE = ResourceLocation.withDefaultNamespace("container/anvil/error");
    private static final ResourceLocation WEAVING_TABLE_LOCATION = ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/gui/weaving_table.png");
    private final Player player;

    public WeavingScreen(WeavingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, WEAVING_TABLE_LOCATION);
        this.player = playerInventory.player;
        this.titleLabelX = 60;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.player.closeContainer();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    protected void renderErrorIcon(GuiGraphics graphics, int p_283237_, int p_282237_) {
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(this.menu.getResultSlot()).hasItem()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, p_283237_ + 99, p_282237_ + 45, 28, 21);
        }
    }
}
