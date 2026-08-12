package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import org.exodusstudio.frostbite.Frostbite;

public class PlacedFeatureRegistry {
    public static final ResourceKey<PlacedFeature> MISTY_KEY = registerKey("trees_misty");
    public static final ResourceKey<PlacedFeature> MISTY_FOLIAGE_KEY = registerKey("placed_misty_foliage");
    public static final ResourceKey<PlacedFeature> MEGA_MISTY_KEY = registerKey("mega_trees_misty");

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, name));
    }
}
