package org.exodusstudio.frostbite.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class HeaterStorage {
    HeaterBlock block;
    BlockPos pos;

    public HeaterStorage(BlockPos pos, HeaterBlock block) {
        this.pos = pos;
        this.block = block;
    }

    public void tickBlock(ServerLevel level) {
        block.tick(level.getBlockState(pos), level, pos, level.random);
    }

    public HeaterBlock getBlock() {
        return block;
    }

    public BlockPos getPos() {
        return pos;
    }
}
