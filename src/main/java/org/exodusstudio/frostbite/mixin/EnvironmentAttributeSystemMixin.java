package org.exodusstudio.frostbite.mixin;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.dimension.DimensionType;
import org.exodusstudio.frostbite.common.util.Util;
import org.exodusstudio.frostbite.common.weather.FrostbiteWeatherAttributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.function.LongSupplier;
import java.util.stream.Stream;

@Mixin(EnvironmentAttributeSystem.class)
public class EnvironmentAttributeSystemMixin {
    @Inject(at = @At("TAIL"), method = "addDefaultLayers", cancellable = true)
    private static void addDefaultLayers(EnvironmentAttributeSystem.Builder builder, Level level, CallbackInfo ci) {
        if (Util.isFrostbite(level)) {
            RegistryAccess registryaccess = level.registryAccess();
            BiomeManager biomemanager = level.getBiomeManager();
            Objects.requireNonNull(level);
            LongSupplier longsupplier = level::getDayTime;
            frostbite$addDimensionLayer(builder, level.dimensionType());
            frostbite$addBiomeLayer(builder, registryaccess.lookupOrThrow(Registries.BIOME), biomemanager);
            level.dimensionType().timelines().forEach((p_466516_) -> builder.addTimelineLayer(p_466516_, longsupplier));
            FrostbiteWeatherAttributes.addBuiltinLayers(builder, FrostbiteWeatherAttributes.WeatherAccess.from());
            ci.cancel();
        }
    }

    @Unique
    private static void frostbite$addDimensionLayer(EnvironmentAttributeSystem.Builder p_466934_, DimensionType p_466961_) {
        p_466934_.addConstantLayer(p_466961_.attributes());
    }

    @Unique
    private static void frostbite$addBiomeLayer(EnvironmentAttributeSystem.Builder builder, HolderLookup<Biome> p_468636_, BiomeManager p_469748_) {
        Stream<EnvironmentAttribute<?>> stream = p_468636_.listElements().flatMap((p_457819_) -> p_457819_.value().getAttributes().keySet().stream()).distinct();
        stream.forEach((p_466513_) -> frostbite$addBiomeLayerForAttribute(builder, p_466513_, p_469748_));
    }

    @Unique
    private static <Value> void frostbite$addBiomeLayerForAttribute(EnvironmentAttributeSystem.Builder builder, EnvironmentAttribute<Value> attribute, BiomeManager manager) {
        builder.addPositionalLayer(attribute, (p_466519_, p_466520_, p_466521_) -> {
            if (p_466521_ != null && attribute.isSpatiallyInterpolated()) {
                return p_466521_.applyAttributeLayer(attribute, p_466519_);
            } else {
                Holder<Biome> holder = manager.getNoiseBiomeAtPosition(p_466520_.x, p_466520_.y, p_466520_.z);
                return holder.value().getAttributes().applyModifier(attribute, p_466519_);
            }
        });
    }
}
