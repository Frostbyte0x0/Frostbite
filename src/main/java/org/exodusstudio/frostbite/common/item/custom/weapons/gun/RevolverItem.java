package org.exodusstudio.frostbite.common.item.custom.weapons.gun;

import org.exodusstudio.frostbite.common.registry.ItemRegistry;

public class RevolverItem extends AbstractGun {
    public RevolverItem(Properties properties) {
        super(properties, 4f, 1f, 60, 10, 6, ItemRegistry.REVOLVER_BULLET.toStack());
    }
}
