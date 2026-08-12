package org.exodusstudio.frostbite.common.worldgen.tree;

import net.minecraft.world.level.block.grower.TreeGrower;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.ConfiguredFeatureRegistry;

import java.util.Optional;

public class TreeGrowers {
    public static final TreeGrower MISTY = new TreeGrower(Frostbite.MOD_ID + ":misty",
            Optional.of(ConfiguredFeatureRegistry.MEGA_MISTY_KEY), Optional.of(ConfiguredFeatureRegistry.MISTY_KEY), Optional.empty());

    public static final TreeGrower DIM = new TreeGrower(Frostbite.MOD_ID + ":dim",
            Optional.empty(), Optional.of(ConfiguredFeatureRegistry.DIM_KEY), Optional.empty());

    public static final TreeGrower SILVER = new TreeGrower(Frostbite.MOD_ID + ":silver",
            Optional.empty(), Optional.of(ConfiguredFeatureRegistry.SILVER_KEY), Optional.empty());

    public static final TreeGrower CHARM = new TreeGrower(Frostbite.MOD_ID + ":charm",
            Optional.empty(), Optional.of(ConfiguredFeatureRegistry.CHARM_KEY), Optional.empty());

    public static final TreeGrower LAVENDER = new TreeGrower(Frostbite.MOD_ID + ":lavender",
            Optional.empty(), Optional.of(ConfiguredFeatureRegistry.LAVENDER_KEY), Optional.empty());
}
