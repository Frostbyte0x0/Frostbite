package org.exodusstudio.frostbite.common.item;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.entity.custom.projectiles.FrozenArrow;

import javax.annotation.Nullable;

public class FrozenArrowItem extends ArrowItem implements ProjectileItem {
    public FrozenArrowItem(Properties pProperties) {
        super(pProperties);
    }

    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new FrozenArrow(level, shooter, ammo.copyWithCount(1), weapon);
    }

    public Projectile asProjectile(Level level, Position position, ItemStack stack, Direction dir) {
        FrozenArrow arrow = new FrozenArrow(level, position.x(), position.y(), position.z(), stack.copyWithCount(1), null);
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }

    public boolean isInfinite(ItemStack ammo, ItemStack bow, LivingEntity livingEntity) {
        return false;
    }
}