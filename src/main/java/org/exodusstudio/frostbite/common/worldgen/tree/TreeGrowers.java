package org.exodusstudio.frostbite.common.worldgen.tree;

import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.grower.TreeGrower;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.ConfiguredFeatureRegistry;

public class TreeGrowers {
    public static final TreeGrower MISTY = new TreeGrower(Frostbite.MOD_ID + ":misty",
            WeightedList.of(ConfiguredFeatureRegistry.MISTY_KEY), WeightedList.of(ConfiguredFeatureRegistry.MEGA_MISTY_KEY), WeightedList.of(),
            ConfiguredFeatureRegistry.MISTY_KEY);

    public static final TreeGrower DIM = new TreeGrower(Frostbite.MOD_ID + ":dim",
            WeightedList.of(ConfiguredFeatureRegistry.DIM_KEY), WeightedList.of(), WeightedList.of(), ConfiguredFeatureRegistry.DIM_KEY);

    public static final TreeGrower SILVER = new TreeGrower(Frostbite.MOD_ID + ":silver",
            WeightedList.of(ConfiguredFeatureRegistry.SILVER_KEY), WeightedList.of(), WeightedList.of(), ConfiguredFeatureRegistry.SILVER_KEY);

    public static final TreeGrower CHARM = new TreeGrower(Frostbite.MOD_ID + ":charm",
            WeightedList.of(ConfiguredFeatureRegistry.CHARM_KEY), WeightedList.of(), WeightedList.of(), ConfiguredFeatureRegistry.CHARM_KEY);

    public static final TreeGrower LAVENDER = new TreeGrower(Frostbite.MOD_ID + ":lavender",
            WeightedList.of(ConfiguredFeatureRegistry.LAVENDER_KEY), WeightedList.of(), WeightedList.of(), ConfiguredFeatureRegistry.LAVENDER_KEY);
}
