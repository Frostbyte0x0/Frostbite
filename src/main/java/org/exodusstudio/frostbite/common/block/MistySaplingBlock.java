package org.exodusstudio.frostbite.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;

public class MistySaplingBlock extends SaplingBlock {
    public MistySaplingBlock(TreeGrower treeGrower, Properties properties) {
        super(treeGrower, properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockRegistry.SNOWY_MISTY_GRASS.get()) || state.is(BlockRegistry.MISTY_GRASS.get());
    }
}
