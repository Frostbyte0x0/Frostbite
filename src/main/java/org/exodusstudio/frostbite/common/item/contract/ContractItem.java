package org.exodusstudio.frostbite.common.item.contract;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.contracts.Contract;
import org.exodusstudio.frostbite.common.contracts.PlayerContractInfo;

public class ContractItem extends Item {
    public ContractItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        Contract c;
        if (Contract.getContract(player.getItemInHand(usedHand)) != null && (c = Contract.getContract(player.getItemInHand(usedHand))) != null) {
            PlayerContractInfo.setContract(player, c);
            Frostbite.LOGGER.debug("Added contract {} to player {}", c, player.getName().getString());
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}