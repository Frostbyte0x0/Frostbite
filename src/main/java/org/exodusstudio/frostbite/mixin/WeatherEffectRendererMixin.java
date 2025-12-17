package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.WeatherRenderState;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.exodusstudio.frostbite.common.util.Util.isFrostbite;

@Mixin(WeatherEffectRenderer.class)
public class WeatherEffectRendererMixin {
    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/client/renderer/state/WeatherRenderState;)V", cancellable = true)
    public void render(MultiBufferSource bufferSource, Vec3 cameraPosition, WeatherRenderState renderState, CallbackInfo ci) {
        if (Minecraft.getInstance().level != null && isFrostbite(Minecraft.getInstance().level)) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "tickRainParticles", cancellable = true)
    public void tickRainParticles(ClientLevel level, Camera camera, int ticks, ParticleStatus particleStatus, int p_455026_, CallbackInfo ci) {
        if (isFrostbite(level)) {
            ci.cancel();
        }
    }
}
