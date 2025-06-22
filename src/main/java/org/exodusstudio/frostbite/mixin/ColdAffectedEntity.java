package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.entity.LivingEntity;

import java.util.Map;

public interface ColdAffectedEntity {

    default void raiseTemperature(LivingEntity entity) {

    }
}
