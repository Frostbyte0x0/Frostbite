package org.exodusstudio.frostbite.common.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public interface TargetingEntity {
    default boolean canTargetEntity(@Nullable Entity entity) {
        if (!(entity instanceof Player player) ||
                getInstance().level() != entity.level() ||
                !EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) ||
                getInstance().isAlliedTo(entity) ||
                player.isInvulnerable() || player.isDeadOrDying()) {
            return false;
        }
        return getInstance().level().getWorldBorder().isWithinBounds(player.getBoundingBox());
    }

    default Entity getInstance() {
        return null;
    }
}
