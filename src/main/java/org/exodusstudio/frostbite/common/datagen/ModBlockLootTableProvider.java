package org.exodusstudio.frostbite.common.datagen;

import org.exodusstudio.frostbite.common.registry.BlockRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        dropSelf(BlockRegistry.BLACK_BLOCK.get());

        // MISTY WOOD
        dropSelf(BlockRegistry.MISTY_LOG.get());
        dropSelf(BlockRegistry.MISTY_WOOD.get());
        dropSelf(BlockRegistry.STRIPPED_MISTY_LOG.get());
        dropSelf(BlockRegistry.STRIPPED_MISTY_WOOD.get());
        dropSelf(BlockRegistry.MISTY_PLANKS.get());
        dropSelf(BlockRegistry.MISTY_SAPLING.get());

        add(BlockRegistry.MISTY_LEAVES.get(), block ->
                createLeavesDrops(block, BlockRegistry.MISTY_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));

        // DIM WOOD
        dropSelf(BlockRegistry.DIM_LOG.get());
        dropSelf(BlockRegistry.DIM_WOOD.get());
        dropSelf(BlockRegistry.STRIPPED_DIM_LOG.get());
        dropSelf(BlockRegistry.STRIPPED_DIM_WOOD.get());
        dropSelf(BlockRegistry.DIM_PLANKS.get());
        dropSelf(BlockRegistry.DIM_SAPLING.get());

        add(BlockRegistry.DIM_LEAVES.get(), block ->
                createLeavesDrops(block, BlockRegistry.DIM_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));

        // SILVER WOOD
        dropSelf(BlockRegistry.SILVER_LOG.get());
        dropSelf(BlockRegistry.SILVER_WOOD.get());
        dropSelf(BlockRegistry.STRIPPED_SILVER_LOG.get());
        dropSelf(BlockRegistry.STRIPPED_SILVER_WOOD.get());
        dropSelf(BlockRegistry.SILVER_PLANKS.get());
        dropSelf(BlockRegistry.SILVER_SAPLING.get());

        add(BlockRegistry.SILVER_LEAVES.get(), block ->
                createLeavesDrops(block, BlockRegistry.SILVER_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));

        // CHARM WOOD
        dropSelf(BlockRegistry.CHARM_LOG.get());
        dropSelf(BlockRegistry.CHARM_WOOD.get());
        dropSelf(BlockRegistry.STRIPPED_CHARM_LOG.get());
        dropSelf(BlockRegistry.STRIPPED_CHARM_WOOD.get());
        dropSelf(BlockRegistry.CHARM_PLANKS.get());
        dropSelf(BlockRegistry.CHARM_SAPLING.get());

        add(BlockRegistry.CHARM_LEAVES.get(), block ->
                createLeavesDrops(block, BlockRegistry.CHARM_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockRegistry.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
