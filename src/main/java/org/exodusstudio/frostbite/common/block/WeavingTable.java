package org.exodusstudio.frostbite.common.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.exodusstudio.frostbite.client.screens.WeavingMenu;

public class WeavingTable extends HorizontalDirectionalBlock {
    public static final MapCodec<WeavingTable> CODEC = simpleCodec(WeavingTable::new);
    private static final Component CONTAINER_TITLE = Component.translatable("container.loom");

    @Override
    public MapCodec<WeavingTable> codec() {
        return CODEC;
    }

    public WeavingTable(Properties p_49795_) {
        super(p_49795_);
    }

    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            player.openMenu(state.getMenuProvider(level, pos));
        }

        return InteractionResult.SUCCESS;
    }

    protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((p_54783_, inventory, player) ->
                new WeavingMenu(p_54783_, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
