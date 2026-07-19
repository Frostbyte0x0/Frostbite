package org.exodusstudio.frostbite.client.gui.scribing;

import com.ibm.icu.impl.Pair;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.level.block.state.BlockState;
import org.exodusstudio.frostbite.common.component.ContractData;
import org.exodusstudio.frostbite.common.contracts.*;
import org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem;
import org.exodusstudio.frostbite.common.item.contract.PartialContractItem;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.registry.MenuTypeRegistry;

import java.util.ArrayList;
import java.util.List;

public class CombiningMenu extends ItemCombinerMenu {
    private String errorKey = "";

    public CombiningMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public CombiningMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MenuTypeRegistry.COMBINING_MENU.get(), containerId, playerInventory, access, createInputSlotDefinitions());
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

    // Fragment + Fragment = Partial^1
    // Fragment x 4 = Partial^2
    // Partial^x + Partial^y = x + y == 4 ? Contract : Partial^(x + y)
    public void createResult() {
        List<ItemStack> inputStacks = new ArrayList<>();
        ItemStack stack0 = inputSlots.getItem(0);
        ItemStack stack1 = inputSlots.getItem(1);
        ItemStack stack2 = inputSlots.getItem(2);
        ItemStack stack3 = inputSlots.getItem(3);

        if (!stack0.isEmpty()) inputStacks.add(stack0);
        if (!stack1.isEmpty()) inputStacks.add(stack1);
        if (!stack2.isEmpty()) inputStacks.add(stack2);
        if (!stack3.isEmpty()) inputStacks.add(stack3);

        int total = inputStacks.size();

        if (total < 2) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            setErrorMessage("");
            return;
        }

        switch (total) {
            case 2 -> {
                if (isFragment(inputStacks.get(0)) && isFragment(inputStacks.get(1))) {
                    Pair<String, ItemStack> result = combineFragments(inputStacks.get(0), inputStacks.get(1));
                    setErrorMessage(result.first);
                    resultSlots.setItem(0, result.second);
                    return;
                }
                if (isPartial(inputStacks.get(0)) && isPartial(inputStacks.get(1))) {
                    Pair<String, ItemStack> result = combinePartials(inputStacks.get(0), inputStacks.get(1));
                    setErrorMessage(result.first);
                    resultSlots.setItem(0, result.second);
                    return;
                }
                setErrorMessage("container.combining.error.unknown_item");
            }
            case 3 -> {
                if (isPartial(inputStacks.get(0)) && isPartial(inputStacks.get(1)) && isPartial(inputStacks.get(2))) {
                    Pair<String, ItemStack> result = combinePartials(inputStacks.get(0), inputStacks.get(1), inputStacks.get(2));
                    setErrorMessage(result.first);
                    resultSlots.setItem(0, result.second);
                    return;
                }
                setErrorMessage("container.combining.error.unknown_item");
            }
            case 4 -> {
                if (isFragment(inputStacks.get(0)) && isFragment(inputStacks.get(1)) &&
                        isFragment(inputStacks.get(2)) && isFragment(inputStacks.get(3))) {
                    Pair<String, ItemStack> result = combineFragments(inputStacks.get(0), inputStacks.get(1),
                            inputStacks.get(2), inputStacks.get(3));
                    setErrorMessage(result.first);
                    resultSlots.setItem(0, result.second);
                    return;
                }
                if (isPartial(inputStacks.get(0)) && isPartial(inputStacks.get(1)) &&
                        isPartial(inputStacks.get(2)) && isPartial(inputStacks.get(3))) {
                    Pair<String, ItemStack> result = combinePartials(inputStacks.get(0), inputStacks.get(1),
                            inputStacks.get(2), inputStacks.get(3));
                    setErrorMessage(result.first);
                    resultSlots.setItem(0, result.second);
                    return;
                }
                setErrorMessage("container.combining.error.unknown_item");
            }
            default -> {
                resultSlots.setItem(0, ItemStack.EMPTY);
                setErrorMessage("");
            }
        }
    }

    public Pair<String, ItemStack> combineFragments(ItemStack... stacks) {
        List<ContractAttribute> positiveAttributes = new ArrayList<>();
        List<ContractAttribute> negativeAttributes = new ArrayList<>();
        List<ScalableContractAttribute> positiveScalableAttributes = new ArrayList<>();
        List<ScalableContractAttribute> negativeScalableAttributes = new ArrayList<>();
        ContractRank rank = ContractRank.WHITE;
        for (ItemStack stack : stacks) {
            ContractAttribute attribute = attribute(stack);
            rank = attribute.getRank();
            if (attribute instanceof ScalableContractAttribute scalableAttribute) {
                if (scalableAttribute.getPolarity() == Polarity.POSITIVE) {
                    positiveScalableAttributes.add(scalableAttribute);
                } else {
                    negativeScalableAttributes.add(scalableAttribute);
                }
            } else {
                if (attribute.getPolarity() == Polarity.POSITIVE) {
                    positiveAttributes.add(attribute);
                } else {
                    negativeAttributes.add(attribute);
                }
            }
        }

        Contract pc = new Contract(
                positiveAttributes,
                negativeAttributes,
                positiveScalableAttributes,
                negativeScalableAttributes,
                rank
        );
        if (!pc.allSameRank()) {
            return Pair.of("container.combining.error.rank", ItemStack.EMPTY);
        }
        if (!pc.balanced()) {
            return Pair.of("container.combining.error.unbalanced", ItemStack.EMPTY);
        }
        ItemStack result = new ItemStack(ItemRegistry.PARTIAL_CONTRACT.asItem());
        result.set(DataComponentTypeRegistry.CONTRACT, new ContractData(pc));
        result.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), List.of(pc.rank().name()), List.of()));
        return Pair.of("", result);
    }

    public Pair<String, ItemStack> combinePartials(ItemStack... stacks) {
        List<ContractAttribute> positiveAttributes = new ArrayList<>();
        List<ContractAttribute> negativeAttributes = new ArrayList<>();
        List<ScalableContractAttribute> positiveScalableAttributes = new ArrayList<>();
        List<ScalableContractAttribute> negativeScalableAttributes = new ArrayList<>();
        ContractRank rank = ContractRank.WHITE;
        for (ItemStack stack : stacks) {
            Contract c = partial(stack);
            rank = c.rank();
            for (ContractAttribute attribute : c.allAttributes()) {
                if (attribute instanceof ScalableContractAttribute scalableAttribute) {
                    if (scalableAttribute.getPolarity() == Polarity.POSITIVE) {
                        positiveScalableAttributes.add(scalableAttribute);
                    } else {
                        negativeScalableAttributes.add(scalableAttribute);
                    }
                } else {
                    if (attribute.getPolarity() == Polarity.POSITIVE) {
                        positiveAttributes.add(attribute);
                    } else {
                        negativeAttributes.add(attribute);
                    }
                }
            }
        }

        Contract c = new Contract(
                positiveAttributes,
                negativeAttributes,
                positiveScalableAttributes,
                negativeScalableAttributes,
                rank
        );

        ItemStack ct = new ItemStack(ItemRegistry.CONTRACT.asItem());
        ItemStack pct = new ItemStack(ItemRegistry.PARTIAL_CONTRACT.asItem());
        ct.set(DataComponentTypeRegistry.CONTRACT, new ContractData(c));
        ct.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), List.of(c.rank().name()), List.of()));
        pct.set(DataComponentTypeRegistry.CONTRACT, new ContractData(c));
        pct.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), List.of(c.rank().name()), List.of()));

        if (!c.balanced()) {
            return Pair.of("container.combining.error.unbalanced", ItemStack.EMPTY);
        }
        if (!c.allSameRank()) {
            return Pair.of("container.combining.error.rank", ItemStack.EMPTY);
        }
        if (c.isPartial()) {
            return Pair.of("", pct);
        }
        if (c.isComplete()) {
            return Pair.of("", ct);
        }

        return Pair.of("container.combining.error.too_many", ItemStack.EMPTY);
    }

    @SuppressWarnings("DataFlowIssue")
    public static ContractAttribute attribute(ItemStack stack) {
        return stack.get(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE).attribute();
    }

    @SuppressWarnings("DataFlowIssue")
    public static Contract partial(ItemStack stack) {
        return stack.get(DataComponentTypeRegistry.CONTRACT).contract();
    }

    public boolean canCraft() {
        return errorKey.isEmpty();
    }

    public MutableComponent getErrorMessage() {
        return Component.translatable(this.errorKey);
    }

    public void setErrorMessage(String key) {
        this.errorKey = key;
    }

    public static boolean isFragment(ItemStack stack) {
        return stack.getItem() instanceof ContractFragmentItem;
    }

    public static boolean isPartial(ItemStack stack) {
        return stack.getItem() instanceof PartialContractItem;
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
