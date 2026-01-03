package org.exodusstudio.frostbite.common.item.weapons.elf;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.entity.custom.misc.HealingCircleEntity;

public class HealingStaffItem extends ModeWeapon {
    public HealingStaffItem(Properties properties) {
        super(properties, new String[]{"Heal", "Blessing"}, ChatFormatting.GREEN, ChatFormatting.DARK_GREEN);
    }

    @Override
    public void attack(Level level, LivingEntity owner) {
        switch (this.mode) {
            case "Heal":
                if (!level.isClientSide()) {
                    HealingCircleEntity healingCircle = new HealingCircleEntity(null, level);
                    healingCircle.setPos(owner.getX(), owner.getY() + 0.1f, owner.getZ());
                    healingCircle.setOwner(owner);
                    healingCircle.setRadius(1.7f);
                    healingCircle.setBlessing(false);
                    level.addFreshEntity(healingCircle);
                }
                break;
            case "Blessing":
                if (!level.isClientSide()) {
                    HealingCircleEntity healingCircle = new HealingCircleEntity(null, level);
                    healingCircle.setPos(owner.getX(), owner.getY() + 0.1f, owner.getZ());
                    healingCircle.setOwner(owner);
                    healingCircle.setRadius(1.7f);
                    healingCircle.setBlessing(true);
                    level.addFreshEntity(healingCircle);
                }

                break;
        }
    }
}