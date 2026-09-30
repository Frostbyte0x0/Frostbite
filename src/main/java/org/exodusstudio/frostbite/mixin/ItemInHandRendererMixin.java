package org.exodusstudio.frostbite.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import org.exodusstudio.frostbite.common.combat.ParryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Placeholder first-person presentation until the final parry animation exists. */
@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
    @Inject(method = "submitHandsWithItems", at = @At("HEAD"))
    private void frostbite$parryPose(float partialTick, PoseStack poseStack,
                                     SubmitNodeCollector collector, LocalPlayer player,
                                     int light, CallbackInfo ci) {
        if (!ParryManager.isParrying(player)) return;
        poseStack.translate(-0.28F, -0.18F, -0.12F);
        poseStack.mulPose(com.mojang.math.Axis.ZP.rotationDegrees(-38.0F));
    }
}
