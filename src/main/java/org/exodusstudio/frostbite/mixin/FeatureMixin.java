package org.exodusstudio.frostbite.mixin;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.worldgen.features.IceCoreSpikeFeature;
import org.exodusstudio.frostbite.common.worldgen.features.IceCoreSpikeFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Feature.class)
public class FeatureMixin {
    @Unique
    private static boolean frostbite$hasRegistered = false;

    @Inject(at = @At("HEAD"), method = "register")
    private static <C extends FeatureConfiguration, F extends Feature<C>> void register(String name, F feature, CallbackInfoReturnable<F> cir) {
        if (!frostbite$hasRegistered) {
            IceCoreSpikeFeature ICE_CORE_SPIKE =
                    frostbite$register("ice_core_spikes", new IceCoreSpikeFeature(IceCoreSpikeFeatureConfiguration.CODEC));
            frostbite$hasRegistered = true;
        }
    }

    @Unique
    private static <C extends FeatureConfiguration, F extends Feature<C>> F frostbite$register(String name, F feature) {
        return Registry.register(BuiltInRegistries.FEATURE, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, name), feature);
    }
}
