package org.exodusstudio.frostbite.common.entity.custom;

import org.exodusstudio.frostbite.common.entity.ModEntities;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class PackedHardenedSnowballProjectileEntity extends ThrowableItemProjectile {
    public PackedHardenedSnowballProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(ModEntities.PACKED_HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), level);
    }

    public PackedHardenedSnowballProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, double x, double y, double z, Level level, ItemStack item) {
        super(ModEntities.PACKED_HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), x, y, z, level, item);
    }

    public PackedHardenedSnowballProjectileEntity(ServerLevel serverLevel, LivingEntity owner, ItemStack itemStack) {
        super(ModEntities.PACKED_HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), owner, serverLevel, itemStack);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.PACKED_HARDENED_SNOWBALL.get();
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        Entity entity = result.getEntity();
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), 2f);
    }

    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    private ParticleOptions getParticle() {
        ItemStack itemstack = this.getItem();
        return itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, itemstack);
    }

    public void handleEntityEvent(byte p_37402_) {
        if (p_37402_ == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
            }
        }
    }
}
