package org.exodusstudio.frostbite.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class HeaterStorage {
    HeaterBlock block;
    BlockPos pos;
    String dimensionName;

    public HeaterStorage(BlockPos pos, HeaterBlock block, String dimensionName) {
        this.pos = pos;
        this.block = block;
        this.dimensionName = dimensionName;
    }

    public void tickBlock(ServerLevel level) {
        if (block == null) {
            block = (HeaterBlock) level.getBlockState(pos).getBlock();
        }
        block.tick(level.getBlockState(pos), level, pos, level.random);
    }

    public BlockPos getPos() {
        return pos;
    }

    public String getDimensionName() {
        return dimensionName;
    }
}
