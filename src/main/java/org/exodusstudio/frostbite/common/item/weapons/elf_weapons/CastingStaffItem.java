package org.exodusstudio.frostbite.common.item.weapons.elf_weapons;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CastingStaffItem extends AbstractStaff {
    public CastingStaffItem(Properties properties) {
        super(properties, new String[]{"icy breath", "spikes"});
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        switch (this.mode) {
            case "icy breath":
                // Implement icy breath logic here
                break;
            case "spikes":
                // Implement spikes logic here
                break;
            default:
                return InteractionResult.FAIL;
        }

        return InteractionResult.SUCCESS;
    }
}