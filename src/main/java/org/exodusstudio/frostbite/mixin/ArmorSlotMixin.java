package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.item.lining.LiningItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorSlot.class)
public class ArmorSlotMixin {
    @Inject(at = @At("HEAD"), method = "isActive", cancellable = true)
    public void isActive(CallbackInfoReturnable<Boolean> cir) {
        if (Frostbite.shouldShowLining) {
            cir.setReturnValue(false);
        }
    }

    @Inject(at = @At("TAIL"), method = "mayPlace", cancellable = true)
    public void mayPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!Frostbite.shouldShowLining) {
            cir.setReturnValue(cir.getReturnValueZ() && !(stack.getItem() instanceof LiningItem));
        }
    }
}
