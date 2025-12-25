package org.exodusstudio.frostbite.common.util;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface TargetingEntity {
    default boolean canTargetEntity(@Nullable Entity entity) {
        return entity != null && this != entity;
    }
}
