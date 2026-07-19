package org.exodusstudio.frostbite.common.item.contract;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.contracts.*;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;

import java.util.List;

public class ContractFragmentItem extends Item {
    public ContractFragmentItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ContractAttribute a;
        if (getAttribute(player.getItemInHand(usedHand)) != null && (a = getAttribute(player.getItemInHand(usedHand))) != null) {
            player.setData(AttachmentRegistry.PLAYER_CONTRACT_INFO.get(), new PlayerContractInfo(PlayerLiteracy.ILLITERATE, new Contract(
                    List.of(a),
                    List.of(),
                    List.of(),
                    List.of(),
                    ContractRank.WHITE
            )));
            Frostbite.LOGGER.debug("Added contract attribute {} to player {}", a.id, player.getName().getString());
        }
        return InteractionResult.PASS;
    }

    @SuppressWarnings("DataFlowIssue")
    public static ContractAttribute getAttribute(ItemStack stack) {
        ContractAttribute a;
        if (stack.getItem() instanceof ContractFragmentItem && (a = stack.get(DataComponentTypeRegistry.CONTRACT_ATTRIBUTE).attribute()) != null) {
            return a;
        }
        return null;
    }
}