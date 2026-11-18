package org.exodusstudio.frostbite.common.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.VegetationPatchFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Set;

public class IcePatchFeature extends VegetationPatchFeature implements Feature {
    public static final MapCodec<IcePatchFeature> CODEC = makeCodec(IcePatchFeature::new);

    public IcePatchFeature(HolderSet<Block> replaceable, Holder<BlockStateProvider> groundState, Holder<PlacedFeature> vegetationFeature, CaveSurface surface, IntProvider depth, float extraBottomBlockChance, int verticalRange, float vegetationChance, IntProvider xzRadius, float extraEdgeColumnChance) {
        super(replaceable, groundState, vegetationFeature, surface, depth, extraBottomBlockChance, verticalRange, vegetationChance, xzRadius, extraEdgeColumnChance);
    }
//    public IcePatchFeature(Codec<VegetationPatchFeature> codec) {
//        super(codec);
//    }

    @Override
    public void distributeVegetation(
            final WorldGenLevel level, final ChunkGenerator generator, final RandomSource random, final Set<BlockPos> surface
    ) {
        if (surface.isEmpty()) {
            return;
        }

        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxZ = Integer.MIN_VALUE;
        for (BlockPos position : surface) {
            minX = Math.min(minX, position.getX());
            maxX = Math.max(maxX, position.getX());
            minZ = Math.min(minZ, position.getZ());
            maxZ = Math.max(maxZ, position.getZ());
        }

        int centerX = (minX + maxX) / 2;
        int centerZ = (minZ + maxZ) / 2;
        for (BlockPos position : surface) {
            int x = position.getX() - centerX;
            int z = position.getZ() - centerZ;

            // Keep the larger snow and ice center in the dirt patch and leave a dirt border.
            if (x * x + z * z > 5) {
                continue;
            }

            this.placeVegetation(level, generator, random, position);
        }
    }
}
