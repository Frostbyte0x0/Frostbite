package org.exodusstudio.frostbite.common.item.weapons.elf;

import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.HailcoilEntity;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.RoamingBlizzardEntity;

public class SummoningStaffItem extends ModeWeapon {
    public static final String[] MODES = new String[]{"roaming_blizzard", "hailcoil"};

    public SummoningStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public void attack(Level level, LivingEntity owner) {
        if (level instanceof ServerLevel serverLevel) {
            switch (getMode(owner.getItemInHand(InteractionHand.MAIN_HAND))) {
                case "roaming_blizzard":
                    RoamingBlizzardEntity blizzard = new RoamingBlizzardEntity(null, level);

                    blizzard.setPos(owner.blockPosition().getX(), owner.blockPosition().getY(), owner.blockPosition().getZ());
                    blizzard.setOwner(owner);

                    serverLevel.addFreshEntityWithPassengers(blizzard);
                    serverLevel.gameEvent(GameEvent.ENTITY_PLACE, owner.blockPosition(), GameEvent.Context.of(owner));

                    break;
                case "hailcoil":
                    for (int i = 0; i < 4; i++) {
                        HailcoilEntity hailcoil = new HailcoilEntity(null, level);

                        hailcoil.setPos(
                                owner.blockPosition().getX() + (random.nextFloat() - 0.5f) * 3,
                                owner.blockPosition().getY() + 1,
                                owner.blockPosition().getZ() + (random.nextFloat() - 0.5f) * 3);
                        hailcoil.setOwner(owner);

                        serverLevel.addFreshEntityWithPassengers(hailcoil);
                        serverLevel.gameEvent(GameEvent.ENTITY_PLACE, owner.blockPosition(), GameEvent.Context.of(owner));
                    }
                    break;
            }
        }
    }

    @Override
    public String[] getModes() {
        return MODES;
    }

    @Override
    public ChatFormatting regularColour() {
        return ChatFormatting.AQUA;
    }

    @Override
    public ChatFormatting selectedColour() {
        return ChatFormatting.DARK_AQUA;
    }
}