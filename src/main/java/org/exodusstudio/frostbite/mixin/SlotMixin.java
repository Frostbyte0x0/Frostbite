package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.item.custom.lining.LiningItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class SlotMixin {
    @Unique
    Slot frostbite$slot = (Slot) ((Object) this);

    @Inject(at = @At("HEAD"), method = "isActive", cancellable = true)
    public void isActive(CallbackInfoReturnable<Boolean> cir) {
        if (frostbite$slot instanceof ArmorSlot && Frostbite.shouldShowLining) cir.setReturnValue(false);
    }

    @Inject(at = @At("TAIL"), method = "mayPlace", cancellable = true)
    public void mayPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (frostbite$slot instanceof ArmorSlot && !Frostbite.shouldShowLining)
            cir.setReturnValue(cir.getReturnValueZ() && !(frostbite$slot.getItem().getItem() instanceof LiningItem));
    }
}
