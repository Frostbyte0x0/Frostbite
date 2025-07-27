package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class IceSpikeEntity extends EvokerFangs {
    public IceSpikeEntity(EntityType<? extends IceSpikeEntity> p_36923_, Level p_36924_) {
        super(p_36923_, p_36924_);
    }

    public IceSpikeEntity(Level level, double x, double y, double z, float yRot, int warmupDelay, LivingEntity owner) {
        this(EntityRegistry.ICE_SPIKE.get(), level);
        this.warmupDelayTicks = warmupDelay;
        this.setOwner(owner);
        this.setYRot(yRot);
        this.setPos(x, y, z);
    }

    @Override
    public void handleEntityEvent(byte p_36935_) {
        //super.handleEntityEvent(p_36935_);
        if (p_36935_ == 4) {
            this.clientSideAttackStarted = true;
            if (!this.isSilent()) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.AMETHYST_BLOCK_BREAK, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.85F, false);
            }
        }
    }
}
