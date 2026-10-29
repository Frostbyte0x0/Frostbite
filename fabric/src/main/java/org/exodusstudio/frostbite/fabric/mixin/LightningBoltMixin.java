package org.exodusstudio.frostbite.fabric.mixin;

import net.minecraft.world.entity.LightningBolt;
import org.exodusstudio.frostbite.fabric.mixinterfaces.LightningBoltDamage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LightningBolt.class)
public abstract class LightningBoltMixin implements LightningBoltDamage {
    @Unique
    private float frostbite$damage = 5.0F;

    @Override
    public float frostbite$getDamage() {
        return frostbite$damage;
    }

    @Override
    public void frostbite$setDamage(float damage) {
        this.frostbite$damage = damage;
    }
}
