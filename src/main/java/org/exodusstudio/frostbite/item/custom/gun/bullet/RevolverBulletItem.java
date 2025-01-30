package org.exodusstudio.frostbite.item.custom.gun.bullet;

import org.exodusstudio.frostbite.entity.ModEntities;
import org.exodusstudio.frostbite.entity.custom.bullets.SniperBulletEntity;
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
