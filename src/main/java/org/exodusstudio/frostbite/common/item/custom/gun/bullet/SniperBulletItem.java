package org.exodusstudio.frostbite.common.item.custom.gun.bullet;

import org.exodusstudio.frostbite.common.entity.ModEntities;
import org.exodusstudio.frostbite.common.entity.custom.bullets.SniperBulletEntity;
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
