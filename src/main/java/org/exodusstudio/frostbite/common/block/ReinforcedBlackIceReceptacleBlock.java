package org.exodusstudio.frostbite.common.block;

import com.google.common.base.Predicates;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;

public class ReinforcedBlackIceReceptacleBlock extends DirectionalBlock {
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final MapCodec<ReinforcedBlackIceReceptacleBlock> CODEC = simpleCodec(ReinforcedBlackIceReceptacleBlock::new);
    private static BlockPattern portalShape;

    public ReinforcedBlackIceReceptacleBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                .setValue(LIT, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }


    public static BlockPattern getOrCreatePortalShape() {
        if (portalShape == null) {
            portalShape = BlockPatternBuilder.start().aisle("?vvv?", ">???<", ">???<", ">???<", "?^^^?")
                    .where('?', BlockInWorld.hasState(BlockStatePredicate.ANY))
                    .where('^', BlockInWorld.hasState(BlockStatePredicate.forBlock(BlockRegistry.REINFORCED_BLACK_ICE_RECEPTACLE.get())
                            .where(LIT, Predicates.equalTo(true))
                            .where(FACING, Predicates.equalTo(Direction.SOUTH))))
                    .where('>', BlockInWorld.hasState(BlockStatePredicate.forBlock(BlockRegistry.REINFORCED_BLACK_ICE_RECEPTACLE.get())
                            .where(LIT, Predicates.equalTo(true))
                            .where(FACING, Predicates.equalTo(Direction.WEST))))
                    .where('v', BlockInWorld.hasState(BlockStatePredicate.forBlock(BlockRegistry.REINFORCED_BLACK_ICE_RECEPTACLE.get())
                            .where(LIT, Predicates.equalTo(true))
                            .where(FACING, Predicates.equalTo(Direction.NORTH))))
                    .where('<', BlockInWorld.hasState(BlockStatePredicate.forBlock(BlockRegistry.REINFORCED_BLACK_ICE_RECEPTACLE.get())
                            .where(LIT, Predicates.equalTo(true))
                            .where(FACING, Predicates.equalTo(Direction.EAST))))
                    .build();
        }

        return portalShape;
    }

//    protected BlockState rotate(BlockState state, Rotation rot) {
//        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
//    }
//
//    protected BlockState mirror(BlockState state, Mirror mirror) {
//        return state.rotate(mirror.getRotation(state.getValue(FACING)));
//    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
        builder.add(FACING);
    }
}
