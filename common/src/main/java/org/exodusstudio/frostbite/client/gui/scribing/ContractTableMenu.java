package org.exodusstudio.frostbite.client.gui.scribing;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import org.exodusstudio.frostbite.common.component.ContractData;
import org.exodusstudio.frostbite.common.contracts.Contract;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.contracts.ContractAttribute;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;
import org.exodusstudio.frostbite.common.item.contract.ContractItem;
import org.exodusstudio.frostbite.common.item.contract.PartialContractItem;
import org.exodusstudio.frostbite.common.item.ThermalLensItem;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.registry.MenuTypeRegistry;
import org.exodusstudio.frostbite.common.util.Util;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;

import java.util.List;

public class ContractTableMenu extends CombiningMenu {
    public static final int DECIPHERING = 0;
    public static final int COMBINING = 1;
    public static final int APPLYING = 2;
    private int mode = COMBINING;
    private final DataSlot modeData;
    private int consumedSlotA = -1;
    private int consumedSlotB = -1;

    public ContractTableMenu(int id, Inventory inventory) {
        this(id, inventory, ContainerLevelAccess.NULL);
    }

    public ContractTableMenu(int id, Inventory inventory, ContainerLevelAccess access) {
        super(MenuTypeRegistry.CONTRACT_TABLE_MENU.get(), id, inventory, access);
        modeData = addDataSlot(new DataSlot() {
            public int get() { return mode; }
            public void set(int value) { mode = value; }
        });
    }

    public int mode() { return mode; }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id < DECIPHERING || id > APPLYING) return false;
        mode = id;
        modeData.set(id);
        setErrorMessage("");
        createResult();
        return true;
    }

    @Override
    public void broadcastChanges() {
        mode = modeData.get();
        super.broadcastChanges();
    }

    @Override
    public void createResult() {
        if (mode == COMBINING) {
            super.createResult();
            return;
        }
        if (mode == APPLYING) {
            int contractSlot = findSlot(stack -> stack.getItem() instanceof ContractItem || stack.getItem() instanceof PartialContractItem);
            int targetSlot = findSlot(stack -> !stack.isEmpty(), contractSlot);
            ItemStack target = targetSlot < 0 ? ItemStack.EMPTY : inputSlots.getItem(targetSlot);
            ItemStack contract = contractSlot < 0 ? ItemStack.EMPTY : inputSlots.getItem(contractSlot);
            Contract c = Contract.getContract(contract);
            if (!target.isEmpty() && (contract.getItem() instanceof ContractItem || contract.getItem() instanceof PartialContractItem) && c != null) {
                consumedSlotA = targetSlot;
                consumedSlotB = contractSlot;
                ItemStack result = target.copy();
                result.set(DataComponentTypeRegistry.CONTRACT.get(), new ContractData(c));
                if (c.hasAttribute(ContractAttributes.DULL)) Util.setEnchantmentsLevelOne(result);
                if (c.hasAttribute(ContractAttributes.CORROSION)) DataHelper.setData(result, "corrosion_start", Math.toIntExact(player.level().getGameTime()));
                resultSlots.setItem(0, result);
            } else resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }
        int fragmentSlot = findSlot(stack -> stack.getItem() instanceof org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem);
        int lensSlot = findSlot(stack -> stack.getItem() instanceof ThermalLensItem, fragmentSlot);
        ItemStack fragment = fragmentSlot < 0 ? ItemStack.EMPTY : inputSlots.getItem(fragmentSlot);
        ItemStack lens = lensSlot < 0 ? ItemStack.EMPTY : inputSlots.getItem(lensSlot);
        if (fragment.getItem() instanceof org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem && lens.getItem() instanceof ThermalLensItem) {
            consumedSlotA = fragmentSlot;
            consumedSlotB = lensSlot;
            ItemStack result = fragment.copy();
            result.setCount(1);
            resultSlots.setItem(0, result);
        } else resultSlots.setItem(0, ItemStack.EMPTY);
    }

    @Override
    protected void onTake(Player player, ItemStack stack) {
        resultSlots.setItem(0, ItemStack.EMPTY);
        if (mode == DECIPHERING) {
            ContractAttribute attribute = ContractAttribute.getAttribute(stack);
            if (attribute != null) LivingContractInfo.addDiscoveredAttribute(player, attribute);
        }
        if (mode == COMBINING) {
            for (int i = 0; i < inputSlots.getContainerSize(); i++) inputSlots.getItem(i).shrink(1);
        } else {
            if (consumedSlotA >= 0) inputSlots.getItem(consumedSlotA).shrink(1);
            if (mode == APPLYING && consumedSlotB >= 0 && consumedSlotB != consumedSlotA) {
                inputSlots.getItem(consumedSlotB).shrink(1);
            }
        }
        consumedSlotA = -1;
        consumedSlotB = -1;
        player.level().playLocalSound(player.getX(), player.getY(), player.getZ(),
                mode == APPLYING ? SoundEvents.ENCHANTMENT_TABLE_USE : SoundEvents.BOOK_PAGE_TURN,
                SoundSource.BLOCKS, 1.0F, 0.8F + 0.4F * player.getRandom().nextFloat(), false);
    }

    @Override
    public boolean canCraft() {
        if (mode == COMBINING) return super.canCraft();
        return !resultSlots.getItem(0).isEmpty();
    }

    @Override
    public boolean stillValid(Player player) { return !player.isLocalPlayer(); }

    private int findSlot(java.util.function.Predicate<ItemStack> predicate) {
        return findSlot(predicate, -1);
    }

    private int findSlot(java.util.function.Predicate<ItemStack> predicate, int excludedSlot) {
        for (int i = 0; i < inputSlots.getContainerSize(); i++) {
            if (i != excludedSlot && predicate.test(inputSlots.getItem(i))) return i;
        }
        return -1;
    }
}
