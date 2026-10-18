package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.util.ScreenShakeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);

    @Inject(method = "setPosition(DDD)V", at = @At("TAIL"))
    private void applyScreenShake(double x, double y, double z, CallbackInfo ci) {
        Vec3 shake = ScreenShakeHandler.getOffset();
        Camera camera = (Camera) (Object) this;
        this.setRotation((float) (camera.yaw() + shake.x), (float) (camera.xRot() + shake.y));

    }
}
