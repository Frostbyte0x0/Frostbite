package org.exodusstudio.frostbite.common.entity.custom.projectiles;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

public class ImpactPelletEntity extends ThrowableItemProjectile implements ItemSupplier {
    public ImpactPelletEntity(EntityType<? extends ImpactPelletEntity> type, Level level) {
        super(type, level);
    }

    public ImpactPelletEntity(EntityType<? extends ImpactPelletEntity> type, Level level, LivingEntity owner) {
        super(type, owner, level, new ItemStack(ItemRegistry.IMPACT_PELLET.get()));
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.IMPACT_PELLET.get();
    }

    @Override
    public ItemStack getItem() {
        return new ItemStack(ItemRegistry.IMPACT_PELLET.get());
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!level().isClientSide()) {
            ImpactZoneEntity zone = new ImpactZoneEntity(EntityRegistry.IMPACT_ZONE.get(), level());
            if (getOwner() instanceof LivingEntity living) zone.setOwner(living);
            zone.setPos(result.getLocation());
            level().addFreshEntity(zone);
            discard();
        }
    }
}
