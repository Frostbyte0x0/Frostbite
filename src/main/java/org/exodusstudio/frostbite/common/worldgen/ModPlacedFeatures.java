package org.exodusstudio.frostbite.common.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import org.exodusstudio.frostbite.Frostbite;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> MISTY_KEY = registerKey("trees_misty");
    public static final ResourceKey<PlacedFeature> MISTY_FOLIAGE_KEY = registerKey("placed_misty_foliage");
    public static final ResourceKey<PlacedFeature> MEGA_MISTY_KEY = registerKey("mega_trees_misty");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);

        Holder<ConfiguredFeature<?, ?>> holder = holdergetter.getOrThrow(ModConfiguredFeatures.MISTY_KEY);
        register(context, MISTY_KEY, holder, List.of());

        Holder<ConfiguredFeature<?, ?>> holder1 = holdergetter.getOrThrow(ModConfiguredFeatures.MISTY_FOLIAGE_KEY);
        register(context, MISTY_FOLIAGE_KEY, holder1, List.of());

        Holder<ConfiguredFeature<?, ?>> holder2 = holdergetter.getOrThrow(ModConfiguredFeatures.MEGA_MISTY_KEY);
        register(context, MEGA_MISTY_KEY, holder2, List.of());
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, name));
    }

    private static void register(
            BootstrapContext<PlacedFeature> context,
            ResourceKey<PlacedFeature> key,
            Holder<ConfiguredFeature<?, ?>> configuration,
            List<PlacementModifier> modifiers
    ) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
