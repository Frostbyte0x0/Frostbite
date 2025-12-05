package org.exodusstudio.frostbite.common.item.weapons;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.item.weapons.elf.ModeWeapon;

public class LyreItem extends ModeWeapon {
    public LyreItem(Properties properties) {
        super(properties, new String[]{"hammer", "hands", "sword"});
    }

    @Override
    public void attack(Level level, LivingEntity owner) {
        switch (mode) {
            case "hammer" -> {
                // Hammer attack logic
            }
            case "hands" -> {
                // Hands attack logic
            }
            case "sword" -> {
                // Sword attack logic
            }
        }
    }
}
