package org.exodusstudio.frostbite.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;

public class MistyGrassBlock extends GrassBlock {
    public MistyGrassBlock(Properties p_53685_) {
        super(p_53685_);
    }

    private static boolean canBeGrass(BlockState state, LevelReader levelReader, BlockPos pos) {
        BlockPos blockpos = pos.above();
        BlockState blockstate = levelReader.getBlockState(blockpos);
        if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(state, blockstate, Direction.UP, blockstate.getLightBlock());
            return i < 15;
        }
    }

    private static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.above();
        return canBeGrass(state, level, pos) && !level.getFluidState(blockpos).is(FluidTags.WATER);
    }

    protected void randomTick(BlockState p_222508_, ServerLevel p_222509_, BlockPos p_222510_, RandomSource p_222511_) {
        if (!canBeGrass(p_222508_, p_222509_, p_222510_)) {
            if (!p_222509_.isAreaLoaded(p_222510_, 1)) {
                return;
            }

            p_222509_.setBlockAndUpdate(p_222510_, BlockRegistry.FROZEN_DIRT.get().defaultBlockState());
        } else {
            if (!p_222509_.isAreaLoaded(p_222510_, 3)) {
                return;
            }

            if (p_222509_.getMaxLocalRawBrightness(p_222510_.above()) >= 9) {
                BlockState blockstate = this.defaultBlockState();

                for(int i = 0; i < 4; ++i) {
                    BlockPos blockpos = p_222510_.offset(p_222511_.nextInt(3) - 1, p_222511_.nextInt(5) - 3, p_222511_.nextInt(3) - 1);
                    if (p_222509_.getBlockState(blockpos).is(Blocks.DIRT) && canPropagate(blockstate, p_222509_, blockpos)) {
                        p_222509_.setBlockAndUpdate(blockpos, (BlockState)blockstate.setValue(SNOWY, isSnowySetting(p_222509_.getBlockState(blockpos.above()))));
                    }
                }
            }
        }

    }
}
