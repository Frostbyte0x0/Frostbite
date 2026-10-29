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
        for (BlockPos position : surface) {
            int x = position.getX();
            int z = position.getZ();

            // Keep the larger snow and ice center in the dirt patch and leave a dirt border.
            if (x * x + z * z > 5) {
                continue;
            }

            this.placeVegetation(level, generator, random, position);
        }
    }
}
