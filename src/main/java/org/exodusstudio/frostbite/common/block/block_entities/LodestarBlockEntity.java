package org.exodusstudio.frostbite.common.block.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.exodusstudio.frostbite.common.registry.BlockEntityRegistry;

public class LodestarBlockEntity extends BlockEntity {
    public int tickCount;
    public float activeRotation;

    public LodestarBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.LODESTAR.get(), pos, blockState);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, LodestarBlockEntity blockEntity) {
        ++blockEntity.tickCount;
        ++blockEntity.activeRotation;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, LodestarBlockEntity blockEntity) {
        ++blockEntity.tickCount;
    }

    public float getActiveRotation(float partialTick) {
        return (this.activeRotation + partialTick) * -0.0375F;
    }
}
