package org.exodusstudio.frostbite.common.item.custom.weapons.elf_weapons;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public abstract class AbstractStaff extends Item {
    protected final String[] modes;
    protected final String mode;

    public AbstractStaff(Properties properties, String[] modes) {
        super(properties);
        this.modes = modes;
        this.mode = modes[0];
    }

    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {

        return null;
    }
}
