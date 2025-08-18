package org.exodusstudio.frostbite.common.item.weapons.gun.bullet;

import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.entity.custom.bullets.SniperBulletEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class RevolverBulletItem extends Item {
    public RevolverBulletItem(Properties properties) {
        super(properties);
    }

    public SniperBulletEntity createBullet(Level level) {
        return new SniperBulletEntity(EntityRegistry.REVOLVER_BULLET_ENTITY.get(), level);
    }
}
