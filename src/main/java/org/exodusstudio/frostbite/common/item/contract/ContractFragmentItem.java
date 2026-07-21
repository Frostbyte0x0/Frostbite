package org.exodusstudio.frostbite.common.item.contract;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.contracts.Contract;
import org.exodusstudio.frostbite.common.contracts.ContractAttribute;
import org.exodusstudio.frostbite.common.contracts.ContractTarget;
import org.exodusstudio.frostbite.common.contracts.PlayerContractInfo;

import java.util.List;
import java.util.Map;

import static org.exodusstudio.frostbite.common.contracts.ContractAttribute.getAttribute;

public class ContractFragmentItem extends Item {
    public ContractFragmentItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ContractAttribute a;
        if (getAttribute(player.getItemInHand(usedHand)) != null && (a = getAttribute(player.getItemInHand(usedHand))) != null && a.getTarget() == ContractTarget.PLAYER) {
            PlayerContractInfo.setContract(player, new Contract(
                    List.of(a),
                    List.of(),
                    Map.of(),
                    Map.of(),
                    a.getRank()
            ));
            Frostbite.LOGGER.debug("Added contract attribute {} to player {}", a.id, player.getName().getString());
        }
        return InteractionResult.PASS;
    }
}