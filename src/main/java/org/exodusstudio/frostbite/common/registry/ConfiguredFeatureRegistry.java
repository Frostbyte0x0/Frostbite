package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.exodusstudio.frostbite.Frostbite;

public class ConfiguredFeatureRegistry {
    public static final ResourceKey<ConfiguredFeature<?, ?>> MISTY_KEY = registerKey("misty");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MISTY_FOLIAGE_KEY = registerKey("misty_foliage");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MEGA_MISTY_KEY = registerKey("mega_misty");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DIM_KEY = registerKey("dim");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SILVER_KEY = registerKey("silver");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CHARM_KEY = registerKey("charm");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LAVENDER_KEY = registerKey("lavender");

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, name));
    }
}
