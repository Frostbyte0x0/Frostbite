package org.exodusstudio.frostbite.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
                    || Frostbite.savedTemperatures.getTemperature(livingEntity, true) < 10);
        }
    }

    @Inject(at = @At("HEAD"), method = "load")
    private void load(CompoundTag compound, CallbackInfo ci) {
        if (frostbite$entity instanceof LivingEntity livingEntity) {
            Frostbite.savedTemperatures.setTemperatures(livingEntity.getStringUUID(),
                    List.of(compound.getFloat("innerTemperature"), compound.getFloat("outerTemperature")));
        }
    }

    @Inject(at = @At("HEAD"), method = "saveWithoutId")
    private void saveWithoutId(CompoundTag compound, CallbackInfoReturnable<CompoundTag> cir) {
        if (frostbite$entity instanceof LivingEntity livingEntity) {
            compound.putFloat("innerTemperature", Frostbite.savedTemperatures.getTemperature(livingEntity, true));
            compound.putFloat("outerTemperature", Frostbite.savedTemperatures.getTemperature(livingEntity, false));
        }
    }
}
