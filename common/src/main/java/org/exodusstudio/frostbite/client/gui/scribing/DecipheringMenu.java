package org.exodusstudio.frostbite.client.gui.scribing;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.exodusstudio.frostbite.common.item.ThermalLensItem;
import org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem;
import org.exodusstudio.frostbite.common.registry.MenuTypeRegistry;
import org.jspecify.annotations.Nullable;

public class DecipheringMenu extends RecipeBookMenu {
    private final Container container;
    private final ContainerData data;

    public DecipheringMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(3), new SimpleContainerData(4));
    }

    public DecipheringMenu(int containerId, Inventory inventory, Container container, ContainerData data) {
        super(MenuTypeRegistry.DECIPHERING_MENU.get(), containerId);
        this.container = container;
        this.data = data;
        this.addSlot(new Slot(container, 0, 26, 53));
        this.addSlot(new DecipheringLensSlot(container, 1, 26, 12));
        this.addSlot(new DecipheringResultSlot(inventory.player, container, 2, 92, 33));
        this.addStandardInventorySlots(inventory, 8, 84);
        this.addDataSlots(data);
    }

    public boolean canCraft() {
        return slots.getFirst().hasItem() && slots.getFirst().getItem().getItem() instanceof ContractFragmentItem &&
               slots.get(1).hasItem() && slots.get(1).getItem().getItem() instanceof ThermalLensItem &&
               !slots.get(2).hasItem();
    }

    public float getDecipheringProgress() {
        int current = this.data.get(0);
        int total = this.data.get(1);
        return total != 0 && current != 0 ? Mth.clamp((float)current / (float)total, 0.0F, 1.0F) : 0.0F;
    }

    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();
            if (slotIndex == 2) {
                if (!this.moveItemStackTo(stack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(stack, clicked);
            } else if (slotIndex != 1 && slotIndex != 0) {
                if (stack.getItem() instanceof ContractFragmentItem) {
                    if (!this.moveItemStackTo(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (stack.getItem() instanceof ThermalLensItem) {
                    if (!this.moveItemStackTo(stack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 3 && slotIndex < 30) {
                    if (!this.moveItemStackTo(stack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex >= 30 && slotIndex < 39 && !this.moveItemStackTo(stack, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == clicked.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return clicked;
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.isLocalPlayer();
    }

    @Override
    public @Nullable PostPlaceAction handlePlacement(boolean b, boolean b1, RecipeHolder<?> recipeHolder, ServerLevel serverLevel, Inventory inventory) {
        return null;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedItemContents stackedItemContents) {
        if (this.container instanceof StackedContentsCompatible stackedContentsCompatible) {
            stackedContentsCompatible.fillStackedContents(stackedItemContents);
        }
    }

    @Override
    public @Nullable RecipeBookType getRecipeBookType() {
        return RecipeBookType.FURNACE;
    }
}
