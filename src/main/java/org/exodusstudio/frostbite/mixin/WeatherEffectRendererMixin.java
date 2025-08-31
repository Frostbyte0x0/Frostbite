package org.exodusstudio.frostbite.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WeatherEffectRenderer.class)
public class WeatherEffectRendererMixin {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public void render(Level level, MultiBufferSource bufferSource, int ticks, float partialTick, Vec3 cameraPosition, CallbackInfo ci) {
        if (level.dimension().toString().equals("ResourceKey[minecraft:dimension / frostbite:frostbite]")) {
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "tickRainParticles", cancellable = true)
    public void tickRainParticles(ClientLevel level, Camera camera, int ticks, ParticleStatus particleStatus, CallbackInfo ci) {
        if (level.dimension().toString().equals("ResourceKey[minecraft:dimension / frostbite:frostbite]")) {
            ci.cancel();
        }
    }
}
