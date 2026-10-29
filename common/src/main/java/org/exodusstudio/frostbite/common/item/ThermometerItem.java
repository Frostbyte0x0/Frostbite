package org.exodusstudio.frostbite.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;


public class ThermometerItem extends Item {
    private boolean used;

    public ThermometerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        if (used) {
            player.sendOverlayMessage(Component.literal(String.format("You are currently in a %s biome", level.getBiome(player.blockPosition()).value().getBaseTemperature())));
        }
        used = !used;
        return super.use(level, player, usedHand);
    }
}
