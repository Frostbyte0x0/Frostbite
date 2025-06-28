package org.exodusstudio.frostbite.common.util;

import net.minecraft.world.entity.LivingEntity;

public interface ColdAffectedEntity {

    default void raiseTemperature(LivingEntity entity) {

    }
}
