package org.exodusstudio.frostbite.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.FirstPersonHandsAndItemsRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import org.exodusstudio.frostbite.common.combat.ParryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Placeholder first-person presentation until the final parry animation exists. */
@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class ItemInHandRendererMixin {
    @Inject(method = "submitHandsWithItems", at = @At("HEAD"))
    private void frostbite$parryPose(float partialTicks, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, PlayerRenderState playerState, FirstPersonHandsAndItemsRenderState state, CallbackInfo ci) {
        if (Minecraft.getInstance().player == null || !ParryManager.isParrying(Minecraft.getInstance().player)) return;
        poseStack.translate(-0.28F, -0.18F, -0.12F);
        poseStack.rotateDegrees(Axis.ZP, -38.0F);
    }
}
