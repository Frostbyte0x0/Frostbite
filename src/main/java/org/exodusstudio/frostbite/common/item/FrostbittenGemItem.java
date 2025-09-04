package org.exodusstudio.frostbite.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.exodusstudio.frostbite.common.block.ReinforcedBlackIceReceptacleBlock;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;

public class FrostbittenGemItem extends Item {
    public FrostbittenGemItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (state.getBlock() instanceof ReinforcedBlackIceReceptacleBlock block &&
                level instanceof ServerLevel &&
                !state.getValue(BlockStateProperties.LIT)) {
            stack.consume(1, player);
            BlockState state1 = state.setValue(BlockStateProperties.LIT, true);
            level.setBlock(pos, state1, 3);
            level.playSound(player, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);

            BlockPos.MutableBlockPos mutablePos = pos.mutable();

            if (state.getValue(BlockStateProperties.FACING) == (Direction.EAST) ||
                state.getValue(BlockStateProperties.FACING) == (Direction.WEST)) {

                for (int i = 1; i <= 3; i++) {
                    for (int j = -1; j <= 1; j++) {
                        mutablePos.set(pos.getX(), pos.getY() + i, pos.getZ() + j);
                        level.setBlock(mutablePos, BlockRegistry.FROSTBITE_PORTAL.get().defaultBlockState()
                                .setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.Z), Block.UPDATE_ALL);
                    }
                }
            } else if (state.getValue(BlockStateProperties.FACING) == (Direction.NORTH) ||
                       state.getValue(BlockStateProperties.FACING) == (Direction.SOUTH)) {
                for (int i = 1; i <= 3; i++) {
                    for (int j = -1; j <= 1; j++) {
                        mutablePos.set(pos.getX() + j, pos.getY() + i, pos.getZ());
                        level.setBlock(mutablePos, BlockRegistry.FROSTBITE_PORTAL.get().defaultBlockState()
                                .setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.X), Block.UPDATE_ALL);
                    }
                }
            }

//            level.levelEvent(1503, pos, 0);
//            BlockPattern.BlockPatternMatch blockpattern$blockpatternmatch =
//                    ReinforcedBlackIceReceptacleBlock.getOrCreatePortalShape().find(level, pos);
//            if (blockpattern$blockpatternmatch != null) {
//                BlockPos blockpos1 = blockpattern$blockpatternmatch.getFrontTopLeft().offset(-3, 0, -3);
//
//                for (int i = 0; i < 3; i++) {
//                    for (int j = 0; j < 3; j++) {
//                        level.setBlock(blockpos1.offset(i, 0, j), Blocks.END_PORTAL.defaultBlockState(), 2);
//                    }
//                }
//
//                level.globalLevelEvent(1038, blockpos1.offset(1, 0, 1), 0);
//            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
}
