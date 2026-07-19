package org.exodusstudio.frostbite.client.gui.scribing;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.exodusstudio.frostbite.common.registry.MenuTypeRegistry;
import org.exodusstudio.frostbite.common.util.Util;

import static org.exodusstudio.frostbite.common.util.Util.isLiningMaterial;
import static org.exodusstudio.frostbite.common.util.Util.isWeavingPattern;

public class DecipheringMenu extends ItemCombinerMenu {
    public DecipheringMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public DecipheringMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MenuTypeRegistry.DECIPHERING_MENU.get(), containerId, playerInventory, access, createInputSlotDefinitions());
    }

    protected boolean isValidBlock(BlockState state) {
        return state.is(BlockTags.ANVIL);
    }

    protected boolean mayPickup(Player player, boolean p_39024_) {
        return true;
    }

    private static ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 48, 17, (_) -> true)
                .withSlot(1, 66, 35, (_) -> true)
                .withSlot(2, 48, 53, (_) -> true)
                .withSlot(3, 30, 35, (_) -> true)
                .withResultSlot(4, 124, 35).build();
    }

    protected void onTake(Player player, ItemStack stack) {
        resultSlots.setItem(0, ItemStack.EMPTY);
        inputSlots.getItem(0).shrink(1);
        inputSlots.getItem(1).shrink(1);
        inputSlots.getItem(2).shrink(1);
        inputSlots.getItem(3).shrink(1);
    }

    public void createResult() {
        Item material = inputSlots.getItem(0).getItem();
        Item pattern = inputSlots.getItem(1).getItem();
        if ((isLiningMaterial(inputSlots.getItem(0))) &&
                isWeavingPattern(pattern)) {
            if (inputSlots.getItem(0).is(ItemTags.WOOL)) {
                material = Items.WOOL.white();
            }

            resultSlots.setItem(0, new ItemStack(Util.linings.get(material).get(pattern)));
        } else {
            resultSlots.setItem(0, ItemStack.EMPTY);
        }
    }

    public boolean canCraft() {
        return !inputSlots.getItem(0).isEmpty();
    }

//    @Override
//    public void synchronizeCarriedToRemote() {
//        if (!suppressRemoteUpdates) {
//            ItemStack itemstack = getCarried();
//            if (!remoteCarried.matches(itemstack)) {
//                setCarried(new ItemStack(((HashedStack.ActualItem) ((RemoteSlot.Synchronized) remoteCarried).remoteHash).item()));
//                if (synchronizer != null) {
//                    synchronizer.sendCarriedChange(this, itemstack.copy());
//                }
//            }
//        }
//    }

    @Override
    public boolean stillValid(Player player) {
        return !player.isLocalPlayer();
    }
}
