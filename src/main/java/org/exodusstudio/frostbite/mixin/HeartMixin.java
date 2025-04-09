package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import org.exodusstudio.frostbite.client.PlayerHeartData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class HeartMixin {
    @Inject(at = @At("HEAD"), method = "renderHeart", cancellable = true)
    private void renderCustomHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, boolean hardcore, boolean halfHeart, boolean blinking, CallbackInfo ci) {
        if (PlayerHeartData.shouldShow) {
            guiGraphics.blitSprite(RenderType::guiTextured, PlayerHeartData.sprite, x, y, 9, 9);
            ci.cancel();
        }
    }
}
