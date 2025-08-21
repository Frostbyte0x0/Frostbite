package org.exodusstudio.frostbite.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.exodusstudio.frostbite.common.block.block_entities.LodestarBlockEntity;
import org.exodusstudio.frostbite.common.registry.BlockEntityRegistry;
import org.jetbrains.annotations.Nullable;

public class LodestarBlock extends BaseEntityBlock {
    public static final MapCodec<LodestarBlock> CODEC = simpleCodec(LodestarBlock::new);

    public LodestarBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LodestarBlockEntity(blockPos, blockState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, BlockEntityRegistry.LODESTAR.get(),
                level.isClientSide ? LodestarBlockEntity::clientTick : LodestarBlockEntity::serverTick);
    }
}
