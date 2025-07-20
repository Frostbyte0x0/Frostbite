package org.exodusstudio.frostbite.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.exodusstudio.frostbite.Frostbite;

public class HeaterBlock extends Block {
    private final int range;
    private final int temp;

    public HeaterBlock(Properties p_49795_, int range, int temp) {
        super(p_49795_);
        this.range = range;
        this.temp = temp;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        double h = (double) range / 2;
        level.getEntitiesOfClass(LivingEntity.class,
                new AABB(pos.getX() - h,
                        pos.getY() - h,
                        pos.getZ() - h,
                        pos.getX() + h,
                        pos.getY() + h,
                        pos.getZ() + h)).forEach(entity -> {
            Frostbite.savedTemperatures.increaseTemperature(entity, temp, false);
        });
    }
}
