package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.exodusstudio.frostbite.Frostbite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(Entity.class)
public class EntityMixin {
    @Unique
    Entity frostbite$entity = (Entity) ((Object) this);

    @Inject(at = @At("HEAD"), method = "isFullyFrozen", cancellable = true)
    private void isFullyFrozen(CallbackInfoReturnable<Boolean> cir) {
        if (frostbite$entity instanceof LivingEntity livingEntity) {
            cir.setReturnValue(frostbite$entity.getTicksFrozen() >= frostbite$entity.getTicksRequiredToFreeze()
                    || (Frostbite.temperatureStorage.getTemperature(livingEntity, true) < 10 && livingEntity.canFreeze()));
        }
    }

    @Inject(at = @At("HEAD"), method = "load")
    private void load(ValueInput input, CallbackInfo ci) {
        if (frostbite$entity instanceof LivingEntity livingEntity) {
            Frostbite.temperatureStorage.setTemperatures(livingEntity.getStringUUID(),
                    List.of(input.getFloatOr("innerTemperature", 0f), input.getFloatOr("outerTemperature", 0f)));
        }
    }

    @Inject(at = @At("HEAD"), method = "saveWithoutId")
    private void saveWithoutId(ValueOutput output, CallbackInfo ci) {
        if (frostbite$entity instanceof LivingEntity livingEntity) {
            output.putFloat("innerTemperature", Frostbite.temperatureStorage.getTemperature(livingEntity, true));
            output.putFloat("outerTemperature", Frostbite.temperatureStorage.getTemperature(livingEntity, false));
        }
    }
}
