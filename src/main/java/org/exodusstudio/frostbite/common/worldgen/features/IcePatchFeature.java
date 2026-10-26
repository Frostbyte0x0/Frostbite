package org.exodusstudio.frostbite.common.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.VegetationPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;

import java.util.Set;

public class IcePatchFeature extends VegetationPatchFeature {
    public IcePatchFeature(Codec<VegetationPatchConfiguration> codec) {
        super(codec);
    }

    @Override
    protected void distributeVegetation(
            FeaturePlaceContext<VegetationPatchConfiguration> context,
            WorldGenLevel level,
            VegetationPatchConfiguration config,
            RandomSource random,
            Set<BlockPos> possiblePositions,
            int xRadius,
            int zRadius
    ) {
        BlockPos origin = context.origin();

        for (BlockPos position : possiblePositions) {
            int x = position.getX() - origin.getX();
            int z = position.getZ() - origin.getZ();

            // Keep the larger snow and ice center in the dirt patch and leave a dirt border.
            if (x * x + z * z > 5) {
                continue;
            }

            this.placeVegetation(level, config, context.chunkGenerator(), random, position);
        }
    }
}
