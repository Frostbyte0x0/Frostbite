package org.exodusstudio.frostbite.fabric.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import org.exodusstudio.frostbite.fabric.mixinterfaces.LightningBoltDamage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @ModifyExpressionValue(method = "thunderHit", at = @At(value = "CONSTANT", args = "floatValue=5.0F"))
    private float frostbite$lightningDamage(float damage, @Local(argsOnly = true) LightningBolt lightningBolt) {
        return ((LightningBoltDamage) lightningBolt).frostbite$getDamage();
    }
}
