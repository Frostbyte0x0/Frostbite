package org.exodusstudio.frostbite.common.entity.custom.projectiles;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

import javax.annotation.Nullable;

public class FrozenArrow extends AbstractArrow {
    public FrozenArrow(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public FrozenArrow(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(EntityRegistry.FROZEN_ARROW.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
    }

    public FrozenArrow(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
        super(EntityRegistry.FROZEN_ARROW.get(), owner, level, pickupItemStack, firedFromWeapon);
    }

    protected void doPostHurtEffects(LivingEntity living) {
        super.doPostHurtEffects(living);
        Frostbite.temperatureStorage.decreaseTemperature(living, 5, false);
        living.addEffect(new MobEffectInstance(EffectRegistry.COLD_WEAKNESS, 60*20, 0));
    }

    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ItemRegistry.FROZEN_ARROW.asItem());
    }
}
