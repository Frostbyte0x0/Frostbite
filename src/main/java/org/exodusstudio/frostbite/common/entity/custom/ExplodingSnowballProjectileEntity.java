package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

public class ExplodingSnowballProjectileEntity extends ThrowableItemProjectile implements ItemSupplier {
    public ExplodingSnowballProjectileEntity(EntityType<? extends ExplodingSnowballProjectileEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ExplodingSnowballProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, LivingEntity livingEntity, Level level, ItemStack itemStack) {
        super(entityType, livingEntity, level, new ItemStack(ItemRegistry.EXPLODING_SNOWBALL.get()));
    }

    protected void explode() {
        this.level().explode(this, Explosion.getDefaultDamageSource(this.level(), this),
                null, this.getX(), this.getY(0.0625F), this.getZ(), 1f, false, Level.ExplosionInteraction.NONE);
    }

    private ParticleOptions getParticle() {
        ItemStack itemstack = this.getItem();
        return (itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, itemstack));
    }

    public void handleEntityEvent(byte p_37402_) {
        if (p_37402_ == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F, 0.0F);
            }
        }

    }

    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);

        explode();
    }

    protected void onHit(HitResult result) {
        super.onHit(result);

        explode();
    }

    @Override
    protected Item getDefaultItem() {
        return ItemRegistry.EXPLODING_SNOWBALL.get();
    }

    public @NotNull ItemStack getItem() {
        return new ItemStack(ItemRegistry.EXPLODING_SNOWBALL.get());
    }
}
