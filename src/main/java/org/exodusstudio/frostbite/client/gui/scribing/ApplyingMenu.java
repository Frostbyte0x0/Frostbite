package org.exodusstudio.frostbite.client.gui.scribing;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.exodusstudio.frostbite.common.component.ContractData;
import org.exodusstudio.frostbite.common.contracts.Contract;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.item.contract.ContractItem;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.MenuTypeRegistry;
import org.exodusstudio.frostbite.common.util.Util;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;

public class ApplyingMenu extends ItemCombinerMenu {
    public ApplyingMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public ApplyingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MenuTypeRegistry.APPLYING_MENU.get(), containerId, playerInventory, access, createInputSlotDefinitions());
    }

    protected boolean isValidBlock(BlockState state) {
        return state.is(BlockTags.ANVIL);
    }

    protected boolean mayPickup(Player player, boolean p_39024_) {
        return true;
    }

    private static ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 27, 30, (_) -> true)
                .withSlot(1, 76, 30, (_) -> true)
                .withResultSlot(2, 134, 30).build();
    }

    protected void onTake(Player player, ItemStack stack) {
        resultSlots.setItem(0, ItemStack.EMPTY);
        inputSlots.getItem(0).shrink(1);
        inputSlots.getItem(1).shrink(1);
        player.level().playLocalSound(
                player.getX(), player.getY(), player.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS,
                1.0F, 0.8F + 0.4F * player.getRandom().nextFloat(), false
        );
    }

    public void createResult() {
        if (canCraft()) {
            ItemStack stack = inputSlots.getItem(0);
            ItemStack contract = inputSlots.getItem(1);
            Contract c = Contract.getContract(contract);

            ItemStack result = stack.copy();
            result.set(DataComponentTypeRegistry.CONTRACT, new ContractData(c));
            if (c.hasAttribute(ContractAttributes.DULL)) Util.setEnchantmentsLevelOne(result);
            if (c.hasAttribute(ContractAttributes.CORROSION)) DataHelper.setData(result, "corrosion_start", Math.toIntExact(player.level().getGameTime()));
            resultSlots.setItem(0, result);
        } else {
            resultSlots.setItem(0, ItemStack.EMPTY);
        }
    }

    public boolean canCraft() {
        return inputSlots.getItem(1).getItem() instanceof ContractItem && Contract.getContract(inputSlots.getItem(1)) != null;
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.isLocalPlayer();
    }
}
