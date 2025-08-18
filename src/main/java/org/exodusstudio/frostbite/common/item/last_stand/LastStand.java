package org.exodusstudio.frostbite.common.item.last_stand;

import net.minecraft.server.level.ServerLevel;

public interface LastStand {

    default void frostbite$startAccumulatingDamage(ServerLevel serverLevel) {

    }

    default void frostbite$addDamage(float amount) {

    }
}
