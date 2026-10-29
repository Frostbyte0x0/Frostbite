package org.exodusstudio.frostbite.common.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.exodusstudio.frostbite.common.registry.deferred.DeferredHolder;
import org.exodusstudio.frostbite.common.registry.deferred.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.worldgen.features.FrostbiteSnowAndFreezeFeature;
import org.exodusstudio.frostbite.common.worldgen.features.IceCoreSpikeFeature;
import org.exodusstudio.frostbite.common.worldgen.features.IcePatchFeature;

public class FeatureRegistry {
    public static final DeferredRegister<MapCodec<? extends Feature>> FEATURE_TYPES =
            DeferredRegister.create(BuiltInRegistries.FEATURE_TYPE, Frostbite.MOD_ID);


    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<IceCoreSpikeFeature>> ICE_CORE_SPIKES =
            FEATURE_TYPES.register("ice_core_spikes", () -> IceCoreSpikeFeature.CODEC);

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<FrostbiteSnowAndFreezeFeature>> FROSTBITE_SNOW_AND_FREEZE =
            FEATURE_TYPES.register("frostbite_snow_and_freeze", () -> FrostbiteSnowAndFreezeFeature.CODEC);

    public static final DeferredHolder<MapCodec<? extends Feature>, MapCodec<IcePatchFeature>> ICE_PATCH =
            FEATURE_TYPES.register("ice_patch", () -> IcePatchFeature.CODEC);
}
