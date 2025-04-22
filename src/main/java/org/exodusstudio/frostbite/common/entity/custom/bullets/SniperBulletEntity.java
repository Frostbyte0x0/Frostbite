package org.exodusstudio.frostbite.common.entity.custom.bullets;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

public class SniperBulletEntity extends AbstractBullet {
    public SniperBulletEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(EntityRegistry.SNIPER_BULLET_ENTITY.get(), level);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ItemRegistry.SNIPER_BULLET.asItem());
    }
}
