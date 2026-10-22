package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import org.exodusstudio.frostbite.common.registry.AttachmentRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin {
    @Inject(at = @At("HEAD"), method = "containerTick")
    private void containerTick(CallbackInfo ci) {
        if (Minecraft.getInstance().player.isCreative()) {
            Minecraft.getInstance().player.setData(AttachmentRegistry.SHOW_LINING, false);
        }
    }
}
