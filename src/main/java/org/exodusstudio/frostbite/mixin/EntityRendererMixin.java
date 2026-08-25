package org.exodusstudio.frostbite.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.mixinterfaces.TE;
import org.exodusstudio.frostbite.common.mixinterfaces.UUIDState;
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
    private void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        UUID uuid = ((UUIDState) state).frostbite$getUUID();
        if (uuid == null) return;
        if (frostbite$shouldShowEntityOutlines() && Minecraft.getInstance().level.getEntity(uuid) instanceof LivingEntity l && l.distanceTo(Minecraft.getInstance().player) <= 30) {
            state.nameTag = null;
            poseStack.pushPose();
            poseStack.translate(0, 0.5, 0);
            submitNodeCollector.submitNameTag(poseStack, state.nameTagAttachment, 0,
                    Component.literal(((TE) l).getOuterTemp() + "°C"),
                    true, state.lightCoords, camera);
            poseStack.translate(0, -0.3, 0);
            submitNodeCollector.submitNameTag(poseStack, state.nameTagAttachment, 0,
                    Component.literal(((TE) l).getInnerTemp() + "°C"),
                    true, state.lightCoords, camera);
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
