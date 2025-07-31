package org.exodusstudio.frostbite.common.block;

import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class RangedLeavesBlock extends LeavesBlock {
    public RangedLeavesBlock(Properties p_54422_) {
        super(p_54422_);
    }

    @Override
    protected boolean decaying(BlockState state) {
        return !(Boolean)state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 11;
    }
}
