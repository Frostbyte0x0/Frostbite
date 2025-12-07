package org.exodusstudio.frostbite.common.entity.custom.misc;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class EtherealHandsEntity extends EtherealWeaponEntity {
    public EtherealHandsEntity(EntityType<? extends Entity> ignored, Level level) {
        super(EntityRegistry.ETHEREAL_HANDS.get(), level);
    }
}
