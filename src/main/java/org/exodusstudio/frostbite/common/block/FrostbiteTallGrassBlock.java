package org.exodusstudio.frostbite.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;

public class FrostbiteTallGrassBlock extends TallGrassBlock {
    public FrostbiteTallGrassBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.is(BlockRegistry.SNOWY_MISTY_GRASS.get()) || state.is(BlockRegistry.MISTY_GRASS.get());
    }
}
