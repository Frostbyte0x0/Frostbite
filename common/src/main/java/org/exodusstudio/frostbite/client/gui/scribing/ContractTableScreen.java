package org.exodusstudio.frostbite.client.gui.scribing;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;

public class ContractTableScreen extends ItemCombinerScreen<ContractTableMenu> {
    private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("container/anvil/error");
    private static final Identifier BACKGROUND = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/gui/combining_table.png");
    private final Player player;

    public ContractTableScreen(ContractTableMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, BACKGROUND);
        player = inventory.player;
        titleLabelX = (180 - Minecraft.getInstance().font.width(title)) / 2;
        titleLabelY = -Minecraft.getInstance().font.lineHeight - 27;
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(Button.builder(Component.translatable("contract_table.tab.deciphering"), b -> select(ContractTableMenu.DECIPHERING))
                .bounds(leftPos, topPos - 22, 58, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("contract_table.tab.combining"), b -> select(ContractTableMenu.COMBINING))
                .bounds(leftPos + 60, topPos - 22, 58, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("contract_table.tab.applying"), b -> select(ContractTableMenu.APPLYING))
                .bounds(leftPos + 120, topPos - 22, 58, 20).build());
    }

    private void select(int mode) {
        minecraft.gameMode.handleInventoryButtonClick(menu.containerId, mode);
        menu.clickMenuButton(player, mode);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (event.key() == InputConstants.KEY_ESCAPE) player.closeContainer();
        return super.keyPressed(event);
    }

    @Override
    protected void extractErrorIcon(GuiGraphicsExtractor graphics, int x, int y) {
        if (!menu.canCraft()) graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ERROR_SPRITE, x + 87, y + 32, 28, 21);
    }
}
