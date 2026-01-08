package org.exodusstudio.frostbite.common.entity.custom.bullets;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

public class RevolverBulletEntity extends AbstractBullet {
    public RevolverBulletEntity(EntityType<? extends Projectile> ignored, Level level) {
        super(EntityRegistry.REVOLVER_BULLET_ENTITY.get(), level);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ItemRegistry.REVOLVER_BULLET.asItem());
    }
}
