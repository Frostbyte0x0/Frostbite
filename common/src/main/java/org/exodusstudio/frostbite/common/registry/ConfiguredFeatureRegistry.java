package org.exodusstudio.frostbite.common.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.worldgen.features.FrostbiteSnowAndFreezeFeature;
import org.exodusstudio.frostbite.common.worldgen.features.IceCoreSpikeFeature;
import org.exodusstudio.frostbite.common.worldgen.features.IcePatchFeature;

public class ConfiguredFeatureRegistry {
    public static final ResourceKey<Feature> MISTY_KEY = registerKey("misty");
    public static final ResourceKey<Feature> MISTY_FOLIAGE_KEY = registerKey("misty_foliage");
    public static final ResourceKey<Feature> MEGA_MISTY_KEY = registerKey("mega_misty");
    public static final ResourceKey<Feature> DIM_KEY = registerKey("dim");
    public static final ResourceKey<Feature> SILVER_KEY = registerKey("silver");
    public static final ResourceKey<Feature> CHARM_KEY = registerKey("charm");
    public static final ResourceKey<Feature> LAVENDER_KEY = registerKey("lavender");

    public static final ResourceKey<Feature> MOSS_PATCH = registerKey("moss_patch");
    public static final ResourceKey<Feature> MOSS_PATCH_VEGETATION = registerKey("moss_patch_vegetation");
    public static final ResourceKey<Feature> FALLEN_LOG = registerKey("fallen_log");
    public static final ResourceKey<Feature> ICE_PATCH_DEBRIS = registerKey("ice_patch_debris");
    public static final ResourceKey<Feature> ICE_PATCH = registerKey("ice_patch");

    public static ResourceKey<Feature> registerKey(String name) {
        return ResourceKey.create(Registries.FEATURE, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, name));
    }
}
