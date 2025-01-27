package com.frostbyte0x0.frostbite.item.custom.gun.bullet;

import com.frostbyte0x0.frostbite.entity.ModEntities;
import com.frostbyte0x0.frostbite.entity.custom.bullets.SniperBulletEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class RevolverBulletItem extends Item {
    public RevolverBulletItem(Properties properties) {
        super(properties);
    }

    public SniperBulletEntity createBullet(Level level) {
        return new SniperBulletEntity(ModEntities.SNIPER_BULLET_ENTITY.get(), level);
    }
}
