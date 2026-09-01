package org.exodusstudio.frostbite.common.item.weapons.elf;

import net.minecraft.ChatFormatting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.entity.custom.misc.HealingCircleEntity;

public class HealingStaffItem extends ModeWeapon {
    public static final String[] MODES = new String[]{"heal", "blessing"};

    public HealingStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public void attack(Level level, LivingEntity owner) {
        switch (getMode(owner.getItemInHand(InteractionHand.MAIN_HAND))) {
            case "heal":
                if (!level.isClientSide()) {
                    HealingCircleEntity healingCircle = new HealingCircleEntity(null, level);
                    healingCircle.setPos(owner.getX(), owner.getY() + 0.1f, owner.getZ());
                    healingCircle.setOwner(owner);
                    healingCircle.setRadius(1.7f);
                    healingCircle.setBlessing(false);
                    level.addFreshEntity(healingCircle);
                }
                break;
            case "blessing":
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

    @Override
    public String[] getModes() {
        return MODES;
    }

    @Override
    public ChatFormatting regularColour() {
        return ChatFormatting.GREEN;
    }

    @Override
    public ChatFormatting selectedColour() {
        return ChatFormatting.DARK_GREEN;
    }
}