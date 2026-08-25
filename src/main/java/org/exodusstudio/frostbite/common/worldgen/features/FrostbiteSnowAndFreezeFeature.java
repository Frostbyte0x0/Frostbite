package org.exodusstudio.frostbite.common.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.BrazierBlock;
import org.exodusstudio.frostbite.common.block.SnowableSlabBlock;
import org.exodusstudio.frostbite.common.block.SnowableStairBlock;
import org.exodusstudio.frostbite.common.block.SnowableWallBlock;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;

public class FrostbiteSnowAndFreezeFeature extends Feature<NoneFeatureConfiguration> {
    public FrostbiteSnowAndFreezeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos below = context.origin().below();
        BlockState belowState = context.level().getBlockState(below);

        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        BlockPos.MutableBlockPos topPos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();

        for(int dx = 0; dx < 16; ++dx) {
            for(int dz = 0; dz < 16; ++dz) {
                int x = origin.getX() + dx;
                int z = origin.getZ() + dz;
                int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
                topPos.set(x, y, z);
                belowPos.set(topPos).move(Direction.DOWN, 1);
                Biome biome = level.getBiome(topPos).value();
                if (biome.shouldFreeze(level, belowPos, false)) {
                    level.setBlock(belowPos, Blocks.ICE.defaultBlockState(), 2);
                }

                if (biome.shouldSnow(level, topPos)) {
                    if (isSnowableBlock(belowState)) {
                        context.level().setBlock(below, belowState.setValue(BlockStateProperties.SNOWY, true), 2);
                        continue;
                    }
                    if (shouldUnlight(belowState)) {
                        context.level().setBlock(below, belowState.setValue(BlockStateProperties.LIT, false), 2);
                        continue;
                    }
                    if (isInvalidBlock(belowState)) continue;

                    level.setBlock(topPos, Blocks.SNOW.defaultBlockState(), 2);
                    belowState = level.getBlockState(belowPos);
                    if (belowState.hasProperty(SnowyBlock.SNOWY)) {
                        level.setBlock(belowPos, belowState.setValue(SnowyBlock.SNOWY, true), 2);
                    }
                }
            }
        }

        return true;
    }

    public static boolean isSnowableBlock(BlockState state) {
        return
                state.getBlock() instanceof SnowableSlabBlock ||
                state.getBlock() instanceof SnowableStairBlock ||
                state.getBlock() instanceof SnowableWallBlock;
    }

    public static boolean shouldUnlight(BlockState state) {
        return
                state.getBlock() instanceof BrazierBlock;
    }

    public static boolean isInvalidBlock(BlockState state) {
        return
                state.is(Blocks.PACKED_ICE) ||
                state.is(BlockRegistry.STONE_LANTERN) ||
                state.is(BlockRegistry.FROZEN_STONE_LANTERN) ||
                state.is(BlockRegistry.SLATED_ICE_BRICKS);
    }
}
