package org.exodusstudio.frostbite.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class SnowySaplingBlock extends SaplingBlock {
    public SnowySaplingBlock(TreeGrower treeGrower, Properties properties) {
        super(treeGrower, properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return super.mayPlaceOn(state, level, pos) || state.is(Blocks.SNOW_BLOCK);
    }
}
