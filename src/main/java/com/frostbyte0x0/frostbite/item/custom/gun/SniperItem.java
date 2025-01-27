package com.frostbyte0x0.frostbite.item.custom.gun;

import com.frostbyte0x0.frostbite.item.ModItems;

public class SniperItem extends AbstractGun {
    public SniperItem(Properties properties) {
        super(properties, 3f, 0.5f, 80, 20, 5, ModItems.SNIPER_BULLET.toStack());
    }
}
