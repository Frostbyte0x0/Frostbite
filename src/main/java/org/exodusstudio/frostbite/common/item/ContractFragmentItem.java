package org.exodusstudio.frostbite.common.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class ContractFragmentItem extends Item {
    public ContractFragmentItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        return InteractionResult.PASS;
    }
}