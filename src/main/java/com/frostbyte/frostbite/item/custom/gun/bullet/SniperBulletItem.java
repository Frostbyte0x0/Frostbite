package com.frostbyte.frostbite.item.custom.gun.bullet;

import com.frostbyte.frostbite.entity.ModEntities;
import com.frostbyte.frostbite.entity.custom.bullets.SniperBulletEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class SniperBulletItem extends Item {
    public SniperBulletItem(Properties properties) {
        super(properties);
    }

    public SniperBulletEntity createBullet(Level level) {
        return new SniperBulletEntity(ModEntities.SNIPER_BULLET_ENTITY.get(), level);
    }
}
