package org.exodusstudio.frostbite.client.gui.scribing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;

public class ApplyingScreen extends ItemCombinerScreen<ApplyingMenu> {
    private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/anvil/error");
    private static final Identifier SCRIBING_TABLE_LOCATION = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID,
            "textures/gui/applying_table.png");
    private final Player player;

    public ApplyingScreen(ApplyingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, SCRIBING_TABLE_LOCATION);
        this.player = playerInventory.player;
        this.titleLabelX = (180 - Minecraft.getInstance().font.width(title)) / 2;
        this.titleLabelY = -Minecraft.getInstance().font.lineHeight - 2;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == 256) {
            player.closeContainer();
        }
        return super.keyPressed(event);
    }

    @Override
    protected void extractErrorIcon(GuiGraphicsExtractor graphics, int i, int i1) {
        if (!menu.canCraft()) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, i + 87, i1 + 32, 28, 21);
        }
    }
}
