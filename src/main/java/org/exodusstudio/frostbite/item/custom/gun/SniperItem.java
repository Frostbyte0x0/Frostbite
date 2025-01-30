package org.exodusstudio.frostbite.item.custom.gun;

import org.exodusstudio.frostbite.item.ModItems;

public class SniperItem extends AbstractGun {
    public SniperItem(Properties properties) {
        super(properties, 3f, 0.5f, 80, 20, 5, ModItems.SNIPER_BULLET.toStack());
    }
}
