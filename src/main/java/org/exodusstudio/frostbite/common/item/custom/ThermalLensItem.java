package org.exodusstudio.frostbite.common.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class ThermalLensItem extends Item {
    private boolean used;

    public ThermalLensItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        return InteractionResult.SUCCESS;
    }
}