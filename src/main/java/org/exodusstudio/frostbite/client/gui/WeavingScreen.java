package org.exodusstudio.frostbite.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WeavingScreen extends ItemCombinerScreen<WeavingMenu> {
    private static final ResourceLocation TEXT_FIELD_SPRITE = ResourceLocation.withDefaultNamespace("container/anvil/text_field");
    private static final ResourceLocation TEXT_FIELD_DISABLED_SPRITE = ResourceLocation.withDefaultNamespace("container/anvil/text_field_disabled");
    private static final ResourceLocation ERROR_SPRITE = ResourceLocation.withDefaultNamespace("container/anvil/error");
    private static final ResourceLocation ANVIL_LOCATION = ResourceLocation.withDefaultNamespace("textures/gui/container/anvil.png");
    private static final Component TOO_EXPENSIVE_TEXT = Component.translatable("container.repair.expensive");
    private EditBox name;
    private final Player player;

    public WeavingScreen(WeavingMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, ANVIL_LOCATION);
        this.player = playerInventory.player;
        this.titleLabelX = 60;
    }

    protected void subInit() {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.name = new EditBox(this.font, i + 62, j + 24, 103, 12, Component.translatable("container.repair"));
        this.name.setCanLoseFocus(false);
        this.name.setTextColor(-1);
        this.name.setTextColorUneditable(-1);
        this.name.setBordered(false);
        this.name.setMaxLength(50);
        this.name.setResponder(this::onNameChanged);
        this.name.setValue("");
        this.addWidget(this.name);
        this.name.setEditable(this.menu.getSlot(0).hasItem());
    }

    protected void setInitialFocus() {
        this.setInitialFocus(this.name);
    }

    public void resize(Minecraft minecraft, int width, int height) {
        String s = this.name.getValue();
        this.init(minecraft, width, height);
        this.name.setValue(s);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.minecraft.player.closeContainer();
        }

        return this.name.keyPressed(keyCode, scanCode, modifiers) || this.name.canConsumeInput() || super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void onNameChanged(String name) {
        Slot slot = (this.menu).getSlot(0);
        if (slot.hasItem()) {
            String s = name;
            if (!slot.getItem().has(DataComponents.CUSTOM_NAME) && name.equals(slot.getItem().getHoverName().getString())) {
                s = "";
            }

            if (this.menu.setItemName(s)) {
                this.minecraft.player.connection.send(new ServerboundRenameItemPacket(s));
            }
        }

    }

    protected void renderLabels(GuiGraphics p_281442_, int p_282417_, int p_283022_) {
        super.renderLabels(p_281442_, p_282417_, p_283022_);
        int i = this.menu.getCost();
        if (i > 0) {
            int j = 8453920;
            Component component;
            if (i >= 40 && !this.minecraft.player.getAbilities().instabuild) {
                component = TOO_EXPENSIVE_TEXT;
                j = 16736352;
            } else if (!this.menu.getSlot(2).hasItem()) {
                component = null;
            } else {
                component = Component.translatable("container.repair.cost", new Object[]{i});
                if (!this.menu.getSlot(2).mayPickup(this.player)) {
                    j = 16736352;
                }
            }

            if (component != null) {
                int k = this.imageWidth - 8 - this.font.width(component) - 2;
                p_281442_.fill(k - 2, 67, this.imageWidth - 8, 79, 1325400064);
                p_281442_.drawString(this.font, component, k, 69, j);
            }
        }

    }

    protected void renderBg(GuiGraphics p_283345_, float p_283412_, int p_282871_, int p_281306_) {
        super.renderBg(p_283345_, p_283412_, p_282871_, p_281306_);
        p_283345_.blitSprite(RenderType::guiTextured, this.menu.getSlot(0).hasItem() ? TEXT_FIELD_SPRITE : TEXT_FIELD_DISABLED_SPRITE, this.leftPos + 59, this.topPos + 20, 110, 16);
    }

    public void renderFg(GuiGraphics p_283449_, int p_283263_, int p_281526_, float p_282957_) {
        this.name.render(p_283449_, p_283263_, p_281526_, p_282957_);
    }

    protected void renderErrorIcon(GuiGraphics p_282905_, int p_283237_, int p_282237_) {
        if ((this.menu.getSlot(0).hasItem() || this.menu.getSlot(1).hasItem()) && !this.menu.getSlot(this.menu.getResultSlot()).hasItem()) {
            p_282905_.blitSprite(RenderType::guiTextured, ERROR_SPRITE, p_283237_ + 99, p_282237_ + 45, 28, 21);
        }

    }

    public void slotChanged(AbstractContainerMenu containerToSend, int slotInd, ItemStack stack) {
        if (slotInd == 0) {
            this.name.setValue(stack.isEmpty() ? "" : stack.getHoverName().getString());
            this.name.setEditable(!stack.isEmpty());
            this.setFocused(this.name);
        }
    }
}
