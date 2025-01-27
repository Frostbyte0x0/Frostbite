package com.frostbyte0x0.frostbite.entity.custom.bullets;

import com.frostbyte0x0.frostbite.entity.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class SniperBulletEntity extends AbstractBullet {
    public SniperBulletEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(ModEntities.SNIPER_BULLET_ENTITY.get(), level);
    }
}
