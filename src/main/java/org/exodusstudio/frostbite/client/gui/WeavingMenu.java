package org.exodusstudio.frostbite.client.gui;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.exodusstudio.frostbite.common.item.lining.LiningItem;
import org.exodusstudio.frostbite.common.registry.MenuTypeRegistry;

import javax.annotation.Nullable;

public class WeavingMenu extends ItemCombinerMenu {
    public int repairItemCountCost;
    @Nullable
    private String itemName;

    public WeavingMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public WeavingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MenuTypeRegistry.WEAVING_MENU.get(), containerId, playerInventory, access, createInputSlotDefinitions());
    }

    private static ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create().withSlot(0, 27, 47, (p_266635_) -> true).withSlot(1, 76, 47, (p_266634_) -> true).withResultSlot(2, 134, 47).build();
    }

    protected boolean isValidBlock(BlockState state) {
        return state.is(BlockTags.ANVIL);
    }

    protected boolean mayPickup(Player player, boolean p_39024_) {
        return player.hasInfiniteMaterials();
    }

    protected void onTake(Player player, ItemStack stack) {
        if (this.repairItemCountCost > 0) {
            ItemStack itemstack = this.inputSlots.getItem(1);
            if (!itemstack.isEmpty() && itemstack.getCount() > this.repairItemCountCost) {
                itemstack.shrink(this.repairItemCountCost);
                this.inputSlots.setItem(1, itemstack);
            } else {
                this.inputSlots.setItem(1, ItemStack.EMPTY);
            }
        }

        this.inputSlots.setItem(0, ItemStack.EMPTY);
        this.access.execute((level, pos) -> level.levelEvent(1030, pos, 0));
    }

    public void createResult() {
        if (this.inputSlots.getItem(1).getItem() instanceof LiningItem) {

        }

        resultSlots.setItem(0, this.inputSlots.getItem(0).copy());
    }

    public boolean setItemName(String itemName) {
        String s = validateName(itemName);
        if (s != null && !s.equals(this.itemName)) {
            this.itemName = s;
            if (this.getSlot(2).hasItem()) {
                ItemStack itemstack = this.getSlot(2).getItem();
                if (StringUtil.isBlank(s)) {
                    itemstack.remove(DataComponents.CUSTOM_NAME);
                } else {
                    itemstack.set(DataComponents.CUSTOM_NAME, Component.literal(s));
                }
            }

            this.createResult();
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    private static String validateName(String itemName) {
        String s = StringUtil.filterText(itemName);
        return s.length() <= 50 ? s : null;
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.isLocalPlayer();
    }
}
