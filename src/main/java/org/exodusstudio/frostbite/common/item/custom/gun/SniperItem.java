package org.exodusstudio.frostbite.common.item.custom.gun;

import org.exodusstudio.frostbite.common.registry.ItemRegistry;

public class SniperItem extends AbstractGun {
    public SniperItem(Properties properties) {
        super(properties, 3f, 0.5f, 80, 20, 5, ItemRegistry.SNIPER_BULLET.toStack());
    }
}
