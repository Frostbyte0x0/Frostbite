package org.exodusstudio.frostbite.common.item.weapons.elf_weapons;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class HealingStaffItem extends AbstractStaff {
    public HealingStaffItem(Properties properties) {
        super(properties, new String[]{"circle", "hailcoil"});
    }

    @Override
    public void attack(Level level, LivingEntity owner) {
        switch (this.mode) {
            case "circle":

                break;
            case "hailcoil":

                break;
        }
    }
}