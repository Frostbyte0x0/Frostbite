package org.exodusstudio.frostbite.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.util.UUIDState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
    @Unique
    Minecraft frostbite$mc = Minecraft.getInstance();

    @Inject(at = @At("HEAD"), method = "submit")
    private void submit(S renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        UUID uuid = ((UUIDState) renderState).frostbite$getUUID();
        if (uuid == null) return;
        if (frostbite$shouldShowEntityOutlines() && Minecraft.getInstance().level.getEntity(uuid) instanceof LivingEntity l && l.distanceTo(Minecraft.getInstance().player) <= 30) {
            renderState.nameTag = null;
            poseStack.pushPose();
            poseStack.translate(0, 0.5, 0);
            nodeCollector.submitNameTag(poseStack, renderState.nameTagAttachment, 0,
                    Component.literal(Frostbite.temperatureStorage.getTemperature(uuid.toString(), false) + "°C"),
                    true, renderState.lightCoords, renderState.distanceToCameraSq, cameraRenderState);
            poseStack.translate(0, -0.3, 0);
            nodeCollector.submitNameTag(poseStack, renderState.nameTagAttachment, 0,
                    Component.literal(Frostbite.temperatureStorage.getTemperature(uuid.toString(), true) + "°C"),
                    true, renderState.lightCoords, renderState.distanceToCameraSq, cameraRenderState);
            poseStack.popPose();
        }
    }

    @Inject(at = @At("HEAD"), method = "shouldShowName", cancellable = true)
    private void shouldShowName(T entity, double distanceToCameraSq, CallbackInfoReturnable<Boolean> cir) {
        if (frostbite$shouldShowEntityOutlines() && entity instanceof LivingEntity) {
            cir.setReturnValue(true);
        }
    }

    @Unique
    public boolean frostbite$shouldShowEntityOutlines() {
        assert frostbite$mc.player != null;
        return (frostbite$mc.player.getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.ADVANCED_THERMAL_LENS) ||
                frostbite$mc.player.getItemInHand(InteractionHand.OFF_HAND).is(ItemRegistry.ADVANCED_THERMAL_LENS))
                && frostbite$mc.player.isUsingItem() && frostbite$mc.options.getCameraType().isFirstPerson();
    }
}
