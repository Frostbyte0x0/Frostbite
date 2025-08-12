package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.worldgen.foliage.CharmFoliagePlacer;
import org.exodusstudio.frostbite.common.worldgen.foliage.MistyFoliagePlacer;
import org.exodusstudio.frostbite.common.worldgen.foliage.MegaMistyFoliagePlacer;

public class FoliagePlacerRegistry {
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES =
            DeferredRegister.create(BuiltInRegistries.FOLIAGE_PLACER_TYPE, Frostbite.MOD_ID);


    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<MistyFoliagePlacer>> MISTY_FOLIAGE_PLACER =
            FOLIAGE_PLACER_TYPES.register("misty_foliage_placer",
                    () -> new FoliagePlacerType<>(MistyFoliagePlacer.CODEC));

    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<MegaMistyFoliagePlacer>> MEGA_MISTY_FOLIAGE_PLACER =
            FOLIAGE_PLACER_TYPES.register("mega_misty_foliage_placer",
                    () -> new FoliagePlacerType<>(MegaMistyFoliagePlacer.CODEC));

    public static final DeferredHolder<FoliagePlacerType<?>, FoliagePlacerType<CharmFoliagePlacer>> CHARM_FOLIAGE_PLACER =
            FOLIAGE_PLACER_TYPES.register("charm_foliage_placer",
                    () -> new FoliagePlacerType<>(CharmFoliagePlacer.CODEC));
}
