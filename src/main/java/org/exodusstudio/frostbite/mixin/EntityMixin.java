package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.Frostbite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Unique
    Entity frostbite$entity = (Entity) ((Object) this);

    @Inject(at = @At("HEAD"), method = "isFullyFrozen", cancellable = true)
    private void isFullyFrozen(CallbackInfoReturnable<Boolean> cir) {
        if (frostbite$entity instanceof LivingEntity livingEntity) {
            cir.setReturnValue(frostbite$entity.getTicksFrozen() >= frostbite$entity.getTicksRequiredToFreeze()
                    || Frostbite.temperatures.getTemperature(livingEntity, true) < 10);
        }
    }
}
