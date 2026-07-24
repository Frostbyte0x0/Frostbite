package org.exodusstudio.frostbite.common.item.contract;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.contracts.*;

import java.util.List;
import java.util.Map;

public class ContractFragmentItem extends Item {
    public ContractFragmentItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        if (!player.isCreative() || !player.isShiftKeyDown()) return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(usedHand);

        ContractAttribute a;
        if ((a = ContractAttribute.getAttribute(stack)) != null && (a.getTarget() == ContractTarget.PLAYER || a.getTarget() == ContractTarget.LIVING)) {
            Contract c;
            if (a.getPolarity() == Polarity.POSITIVE) {
                if (a instanceof ScalableContractAttribute scalableContractAttribute) {
                    int l = ScalableContractAttribute.getLevel(stack, scalableContractAttribute);
                    c = Contract.create(
                            List.of(),
                            List.of(),
                            Map.of(scalableContractAttribute, l),
                            Map.of(),
                            ContractRank.fromNum(l)
                    );
                } else {
                    c = Contract.create(
                            List.of(a),
                            List.of(),
                            Map.of(),
                            Map.of(),
                            a.getRank()
                    );
                }
            } else {
                if (a instanceof ScalableContractAttribute scalableContractAttribute) {
                    int l = ScalableContractAttribute.getLevel(stack, scalableContractAttribute);
                    c = Contract.create(
                            List.of(),
                            List.of(),
                            Map.of(),
                            Map.of(scalableContractAttribute, l),
                            ContractRank.fromNum(l)
                    );
                } else {
                    c = Contract.create(
                            List.of(),
                            List.of(a),
                            Map.of(),
                            Map.of(),
                            a.getRank()
                    );
                }
            }

            LivingContractInfo.setContract(player, c);
            Frostbite.LOGGER.debug("Added contract attribute {} to player {}", a.id, player.getName().getString());
        }
        return InteractionResult.PASS;
    }
}