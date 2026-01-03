package org.exodusstudio.frostbite.common.item.weapons.elf;

import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.HailcoilEntity;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.RoamingBlizzardEntity;

public class SummoningStaffItem extends ModeWeapon {
    public SummoningStaffItem(Properties properties) {
        super(properties, new String[]{"Roaming Blizzard", "Hailcoil"}, ChatFormatting.AQUA, ChatFormatting.DARK_AQUA);
    }

    @Override
    public void attack(Level level, LivingEntity owner) {
        if (level instanceof ServerLevel serverLevel) {
            switch (this.mode) {
                case "Roaming Blizzard":
                    RoamingBlizzardEntity blizzard = new RoamingBlizzardEntity(null, level);

                    blizzard.setPos(owner.blockPosition().getX(), owner.blockPosition().getY(), owner.blockPosition().getZ());
                    blizzard.setOwner(owner);

                    serverLevel.addFreshEntityWithPassengers(blizzard);
                    serverLevel.gameEvent(GameEvent.ENTITY_PLACE, owner.blockPosition(), GameEvent.Context.of(owner));

                    break;
                case "Hailcoil":
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
}