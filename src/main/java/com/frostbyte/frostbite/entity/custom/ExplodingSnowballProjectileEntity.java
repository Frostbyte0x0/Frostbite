package com.frostbyte.frostbite.entity.custom;

import com.frostbyte.frostbite.item.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ExplodingSnowballProjectileEntity extends AbstractArrow implements ItemSupplier {
    public ExplodingSnowballProjectileEntity(EntityType<? extends ExplodingSnowballProjectileEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ExplodingSnowballProjectileEntity(EntityType<? extends AbstractArrow> entityType, LivingEntity livingEntity, Level level, ItemStack itemStack) {
        super(entityType, livingEntity, level, new ItemStack(ModItems.EXPLODING_SNOWBALL.get()), null);
    }

    protected void explode() {
        this.level().explode(this, Explosion.getDefaultDamageSource(this.level(), this),
                null, this.getX(), this.getY((double)0.0625F), this.getZ(), 1f, false, Level.ExplosionInteraction.NONE);
    }

    private ParticleOptions getParticle() {
        ItemStack itemstack = this.getItem();
        return (itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, itemstack));
    }

    public void handleEntityEvent(byte p_37402_) {
        if (p_37402_ == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level().addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), (double)0.0F, (double)0.0F, (double)0.0F);
            }
        }

    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().getBlockState(this.getOnPos()).isAir() || !this.level()
                .getEntitiesOfClass(Entity.class, new AABB(new Vec3(this.getBlockX() - 1, this.getBlockY() - 1,
                        this.getBlockZ() - 1), new Vec3(this.getBlockX() + 1, this.getBlockY() + 1,
                        this.getBlockZ() + 1))).isEmpty()) {
            explode();
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ModItems.EXPLODING_SNOWBALL.get().getDefaultInstance();
    }

    public @NotNull ItemStack getItem() {
        return new ItemStack(ModItems.EXPLODING_SNOWBALL.get());
    }
}
