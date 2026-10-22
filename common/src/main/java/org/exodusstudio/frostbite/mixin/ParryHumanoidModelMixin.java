package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.exodusstudio.frostbite.common.combat.ParryManager;
import org.exodusstudio.frostbite.common.mixinterfaces.UUIDState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Simple crossed-arm pose visible to other players; replace with animation later. */
@Mixin(HumanoidModel.class)
public class ParryHumanoidModelMixin {
    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void frostbite$parryPose(HumanoidRenderState state, CallbackInfo ci) {
        if (!(state instanceof UUIDState uuidState) || Minecraft.getInstance().level == null) return;
        var entity = Minecraft.getInstance().level.getEntity(uuidState.frostbite$getUUID());
        if (! (entity instanceof net.minecraft.world.entity.LivingEntity living) || !ParryManager.isParrying(living)) return;
        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
        model.rightArm.xRot = -1.25F;
        model.rightArm.zRot = -0.45F;
        model.leftArm.xRot = -1.0F;
        model.leftArm.zRot = 0.35F;
    }
}
