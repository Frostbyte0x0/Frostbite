package com.frostbyte0x0.frostbite.mixin;

import com.frostbyte0x0.frostbite.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Blaze;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    LivingEntity frostbite$livingEntity = (LivingEntity) ((Object) this);

    @Inject(at = @At("HEAD"), method = "tick()V")
    private void tick(CallbackInfo ci) {
        if (frostbite$livingEntity instanceof Blaze) {
            //Frostbite.LOGGER.error("WooowWW!!11!");
            frostbite$livingEntity.addEffect(new MobEffectInstance(ModEffects.IRRITATION));
        }
    }
}
