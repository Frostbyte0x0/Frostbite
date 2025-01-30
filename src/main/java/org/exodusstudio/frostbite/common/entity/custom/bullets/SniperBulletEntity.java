package org.exodusstudio.frostbite.common.entity.custom.bullets;

import org.exodusstudio.frostbite.common.entity.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class SniperBulletEntity extends AbstractBullet {
    public SniperBulletEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(ModEntities.SNIPER_BULLET_ENTITY.get(), level);
    }
}
