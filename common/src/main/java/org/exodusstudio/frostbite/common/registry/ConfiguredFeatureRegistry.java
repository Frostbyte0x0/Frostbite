package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.exodusstudio.frostbite.Frostbite;

public class ConfiguredFeatureRegistry {
    public static final ResourceKey<Feature> MISTY_KEY = registerKey("misty");
    public static final ResourceKey<Feature> MISTY_FOLIAGE_KEY = registerKey("misty_foliage");
    public static final ResourceKey<Feature> MEGA_MISTY_KEY = registerKey("mega_misty");
    public static final ResourceKey<Feature> DIM_KEY = registerKey("dim");
    public static final ResourceKey<Feature> SILVER_KEY = registerKey("silver");
    public static final ResourceKey<Feature> CHARM_KEY = registerKey("charm");
    public static final ResourceKey<Feature> LAVENDER_KEY = registerKey("lavender");

    public static ResourceKey<Feature> registerKey(String name) {
        return ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, name));
    }
}
