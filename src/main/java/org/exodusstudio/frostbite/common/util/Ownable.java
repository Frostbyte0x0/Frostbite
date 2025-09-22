package org.exodusstudio.frostbite.common.util;

import net.minecraft.world.entity.LivingEntity;

public interface Ownable {
    LivingEntity getOwner();
    void setOwner(LivingEntity owner);
}
