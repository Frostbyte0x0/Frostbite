package org.exodusstudio.frostbite.common.entity.custom.bullets;

import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class SniperBulletEntity extends AbstractBullet {
    public SniperBulletEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(EntityRegistry.SNIPER_BULLET_ENTITY.get(), level);
    }
}
