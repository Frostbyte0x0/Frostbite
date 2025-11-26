package org.exodusstudio.frostbite.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.OptionalInt;

import static net.minecraft.world.level.block.SnowyDirtBlock.SNOWY;

public class RangedLeavesBlock extends LeavesBlock {
    public static final IntegerProperty DISTANCE;

    public RangedLeavesBlock(Properties properties) {
        super(0.01F, properties);
        this.registerDefaultState((((this.stateDefinition.any())
                .setValue(SNOWY, false)
                .setValue(DISTANCE, 11))
                .setValue(PERSISTENT, false))
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected boolean decaying(BlockState state) {
        return !(Boolean)state.getValue(PERSISTENT) && state.getValue(DISTANCE) == 11;
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return simpleCodec(RangedLeavesBlock::new);
    }

    @Override
    protected boolean isRandomlyTicking(BlockState state) {
        return state.getValue(DISTANCE) == 11 && !(Boolean)state.getValue(PERSISTENT);
    }

    @Override
    protected void tick(BlockState p_221369_, ServerLevel p_221370_, BlockPos p_221371_, RandomSource p_221372_) {
        p_221370_.setBlock(p_221371_, updateDistance(p_221369_, p_221370_, p_221371_), 3);
    }

    private static BlockState updateDistance(BlockState state, LevelAccessor level, BlockPos pos) {
        int i = 11;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(Direction direction : Direction.values()) {
            blockpos$mutableblockpos.setWithOffset(pos, direction);
            i = Math.min(i, getDistanceAt(level.getBlockState(blockpos$mutableblockpos)) + 1);
            if (i == 1) {
                break;
            }
        }

        return state.setValue(DISTANCE, i);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DISTANCE, SNOWY);
    }

    protected BlockState updateShape(BlockState state, LevelReader reader, ScheduledTickAccess access, BlockPos pos, Direction dir, BlockPos pos1, BlockState state1, RandomSource source) {
        return dir == Direction.UP ? state.setValue(SNOWY, isSnowySetting(state1)) : super.updateShape(state, reader, access, pos, dir, pos1, state1, source);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.getLevel().getBlockState(context.getClickedPos().above());
        return this.defaultBlockState().setValue(SNOWY, isSnowySetting(blockstate));
    }

    protected static boolean isSnowySetting(BlockState state) {
        return state.is(BlockTags.SNOW);
    }

    private static int getDistanceAt(BlockState neighbor) {
        return getOptionalDistanceAt(neighbor).orElse(11);
    }

    public static OptionalInt getOptionalDistanceAt(BlockState state) {
        if (state.is(BlockTags.LOGS)) {
            return OptionalInt.of(0);
        } else {
            return state.hasProperty(DISTANCE) ? OptionalInt.of(state.getValue(DISTANCE)) : OptionalInt.empty();
        }
    }

    @Override
    protected void spawnFallingLeavesParticle(Level level, BlockPos blockPos, RandomSource randomSource) {
        // TODO
    }

    static {
        DISTANCE = BlockStateProperties.NOTE;
    }
}
