package org.exodusstudio.frostbite.common.datagen;

import net.minecraft.tags.ItemTags;
import org.exodusstudio.frostbite.Frostbite;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Frostbite.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // MISTY WOOD
        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(BlockRegistry.MISTY_LOG.get().asItem())
                .add(BlockRegistry.STRIPPED_MISTY_LOG.get().asItem())
                .add(BlockRegistry.MISTY_WOOD.get().asItem())
                .add(BlockRegistry.STRIPPED_MISTY_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS).add(BlockRegistry.MISTY_PLANKS.get().asItem());

        // DIM WOOD
        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(BlockRegistry.DIM_LOG.get().asItem())
                .add(BlockRegistry.STRIPPED_DIM_LOG.get().asItem())
                .add(BlockRegistry.DIM_WOOD.get().asItem())
                .add(BlockRegistry.STRIPPED_DIM_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS).add(BlockRegistry.DIM_PLANKS.get().asItem());

        // SILVER WOOD
        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(BlockRegistry.SILVER_LOG.get().asItem())
                .add(BlockRegistry.STRIPPED_SILVER_LOG.get().asItem())
                .add(BlockRegistry.SILVER_WOOD.get().asItem())
                .add(BlockRegistry.STRIPPED_SILVER_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS).add(BlockRegistry.SILVER_PLANKS.get().asItem());

        // CHARM WOOD
        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(BlockRegistry.CHARM_LOG.get().asItem())
                .add(BlockRegistry.STRIPPED_CHARM_LOG.get().asItem())
                .add(BlockRegistry.CHARM_WOOD.get().asItem())
                .add(BlockRegistry.STRIPPED_CHARM_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS).add(BlockRegistry.CHARM_PLANKS.get().asItem());
    }
}
