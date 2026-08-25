package org.exodusstudio.frostbite.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

import static net.minecraft.world.level.block.SnowyBlock.SNOWY;

public class SnowableStairBlock extends StairBlock {
    public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;

    public SnowableStairBlock(BlockState state, Properties properties) {
        super(state, properties);
        this.registerDefaultState((this.stateDefinition.any())
                .setValue(SNOWY, false)
                .setValue(BOTTOM, false)
                .setValue(WATERLOGGED, false));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SNOWY);
        builder.add(BOTTOM);
    }

    protected BlockState updateShape(BlockState state, LevelReader reader, ScheduledTickAccess access, BlockPos pos, Direction dir, BlockPos pos1, BlockState state1, RandomSource source) {
        BlockState s = getState(reader, pos);

        return super.updateShape(state, reader, access, pos, dir, pos1, state1, source)
                .setValue(SNOWY, s.getValue(SNOWY))
                .setValue(BOTTOM, s.getValue(BOTTOM));
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (isSnowy(itemStack.getItem()) && state.getValue(HALF).equals(Half.BOTTOM)) {
            state = state.setValue(SNOWY, true);
            itemStack.consume(1, player);
        }
        level.setBlock(pos, state, 2);
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    public BlockState getState(LevelReader reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos).is(this) ? reader.getBlockState(pos) : defaultBlockState();

        if (reader.getBlockState(pos.above()).is(Blocks.SNOW) && reader instanceof Level level && state.getValue(HALF).equals(Half.BOTTOM)) {
            state = state.setValue(SNOWY, true);
            level.setBlock(pos.above(), Blocks.AIR.defaultBlockState(), 2);
        }

        return state
                .setValue(SNOWY, reader.getBlockState(pos).getValueOrElse(SNOWY, false))
                .setValue(BOTTOM, !reader.getBlockState(pos.above()).isAir());
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState s = getState(context.getLevel(), context.getClickedPos());
        return super.getStateForPlacement(context)
                .setValue(SNOWY, s.getValue(SNOWY))
                .setValue(BOTTOM, s.getValue(BOTTOM));
    }

    public static boolean isSnowy(Item item) {
        return item.equals(Items.SNOW) ||
                item.equals(Items.SNOWBALL) ||
                item.equals(Items.SNOW_BLOCK);
    }
}
