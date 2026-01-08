package org.exodusstudio.frostbite.common.entity.custom.projectiles;

import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrowableItemProjectile;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class HardenedSnowballProjectileEntity extends ThrowableItemProjectile {
    public HardenedSnowballProjectileEntity(EntityType<? extends ThrowableItemProjectile> ignored, Level level) {
        super(EntityRegistry.HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), level);
    }

    public HardenedSnowballProjectileEntity(ServerLevel serverLevel, LivingEntity owner, ItemStack itemStack) {
        super(EntityRegistry.HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), owner, serverLevel, itemStack);
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.HARDENED_SNOWBALL.get();
    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (level() instanceof ServerLevel serverLevel) {
            Entity entity = result.getEntity();
            entity.hurtServer(serverLevel, this.damageSources().thrown(this, this.getOwner()), 3f);
        }
    }

    protected void onHit(HitResult result) {
        super.onHit(result);

        if (!this.level().isClientSide()) {
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
