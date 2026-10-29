package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.exodusstudio.frostbite.common.util.Util.isFrostbite;

@Mixin(WeatherEffectRenderer.class)
public class WeatherEffectRendererMixin {
    @Inject(at = @At("HEAD"), method = "extractRenderState", cancellable = true)
    public void extractRenderState(ClientLevel level, float partialTicks, Vec3 cameraPos, WeatherRenderState renderState, CallbackInfo ci) {
        if (isFrostbite(level)) {
            renderState.reset();
            ci.cancel();
        }
    }
}
