package org.exodusstudio.frostbite.common.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.worldgen.foliage.MistyFoliagePlacer;

public class FoliagePlacerRegistry {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES =
            DeferredRegister.create(BuiltInRegistries.FOLIAGE_PLACER_TYPE, Frostbite.MOD_ID);


    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<MistyFoliagePlacer>> MISTY_FOLIAGE_PLACER =
            FOLIAGE_PLACER_TYPES.register("misty_foliage_placer",
                    () -> new FoliagePlacerType<>(MistyFoliagePlacer.CODEC));
}
