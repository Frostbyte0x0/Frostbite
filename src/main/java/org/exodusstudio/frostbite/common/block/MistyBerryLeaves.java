package org.exodusstudio.frostbite.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;

import static net.minecraft.world.level.block.SnowyBlock.SNOWY;

public class MistyBerryLeaves extends RangedLeavesBlock {
    ResourceKey<LootTable> MIST_BERRY =
            ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "harvest/mist_berry"));

    public MistyBerryLeaves(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level instanceof ServerLevel serverLevel) {
            Block.dropFromBlockInteractLootTable(
                    serverLevel,
                    MIST_BERRY,
                    state,
                    level.getBlockEntity(pos),
                    null,
                    player,
                    (serverlvl, itemStack) -> Block.popResource(serverlvl, pos, itemStack)
            );
            serverLevel.playSound(
                    null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + serverLevel.getRandom().nextFloat() * 0.4F
            );
            BlockState newState = state.getValue(SNOWY) ?
                    BlockRegistry.MISTY_LEAVES.get().defaultBlockState().setValue(SNOWY, true) :
                    BlockRegistry.MISTY_LEAVES.get().defaultBlockState();
            serverLevel.setBlock(pos, newState, 2);
            serverLevel.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState));
        }

        return InteractionResult.SUCCESS;
    }
}
