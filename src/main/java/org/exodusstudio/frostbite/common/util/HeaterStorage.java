package org.exodusstudio.frostbite.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.HeaterBlock;

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
            Block potentialBlock = level.getBlockState(pos).getBlock();
            if (potentialBlock instanceof HeaterBlock heaterBlock) {
                block = heaterBlock;
            } else {
                Frostbite.heatersToRemove.add(this);
            }
        } else {
            block.tick(level.getBlockState(pos), level, pos, level.random);
        }
    }

    public BlockPos getPos() {
        return pos;
    }

    public String getDimensionName() {
        return dimensionName;
    }
}
