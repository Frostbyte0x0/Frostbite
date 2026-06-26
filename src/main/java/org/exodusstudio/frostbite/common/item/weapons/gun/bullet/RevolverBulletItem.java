package org.exodusstudio.frostbite.common.item.weapons.gun.bullet;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.entity.custom.bullets.RevolverBulletEntity;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class RevolverBulletItem extends Item {
    public RevolverBulletItem(Properties properties) {
        super(properties);
    }

    public RevolverBulletEntity createBullet(Level level) {
        return new RevolverBulletEntity(EntityRegistry.REVOLVER_BULLET_ENTITY.get(), level);
    }
}
