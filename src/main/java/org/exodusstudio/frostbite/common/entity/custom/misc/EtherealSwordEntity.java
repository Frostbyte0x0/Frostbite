package org.exodusstudio.frostbite.common.entity.custom.misc;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class EtherealSwordEntity extends EtherealWeaponEntity {
    public EtherealSwordEntity(EntityType<? extends Entity> ignored, Level level) {
        super(EntityRegistry.ETHEREAL_SWORD.get(), level);
    }

    @Override
    public float getRange() {
        return 1.5f;
    }
}
