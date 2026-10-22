package org.exodusstudio.frostbite.common.item.contract;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.component.ContractData;
import org.exodusstudio.frostbite.common.contracts.Contract;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.contracts.ContractTarget;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;

public class PartialContractItem extends Item {
    public PartialContractItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        if (!player.isShiftKeyDown()) return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(usedHand);
        Contract contract = Contract.getContract(stack);
        if (contract == null) return InteractionResult.PASS;

        ItemStack otherHand = player.getItemInHand(usedHand == InteractionHand.MAIN_HAND
                ? InteractionHand.OFF_HAND
                : InteractionHand.MAIN_HAND);
        if (contract.getStrictestTarget() == ContractTarget.WEAPON && !otherHand.isEmpty()) {
            otherHand.set(DataComponentTypeRegistry.CONTRACT.get(), new ContractData(contract));
            if (contract.hasAttribute(ContractAttributes.CORROSION)) {
                DataHelper.setData(otherHand, "corrosion_start", Math.toIntExact(level.getGameTime()));
            }
            Frostbite.LOGGER.debug("Applied partial weapon contract {} to {}", contract, otherHand.getItem());
            return InteractionResult.SUCCESS;
        }

        if (contract.getStrictestTarget() != ContractTarget.PLAYER &&
                contract.getStrictestTarget() != ContractTarget.LIVING) return InteractionResult.PASS;

        LivingContractInfo.setContract(player, contract);
        Frostbite.LOGGER.debug("Added partial contract {} to player {}", contract, player.getName().getString());
        return InteractionResult.PASS;
    }
}
