package org.exodusstudio.frostbite.mixin;

import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.util.Util;
import org.exodusstudio.frostbite.common.weather.FrostbiteWeatherAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnvironmentAttributeSystem.class)
public class EnvironmentAttributeSystemMixin {
    @Inject(at = @At("TAIL"), method = "addDefaultLayers")
    private static void addDefaultLayers(EnvironmentAttributeSystem.Builder builder, Level level, CallbackInfo ci) {
        if (Util.isFrostbite(level)) {
            FrostbiteWeatherAttributes.addBuiltinLayers(builder, FrostbiteWeatherAttributes.WeatherAccess.from());
        }
    }
}
