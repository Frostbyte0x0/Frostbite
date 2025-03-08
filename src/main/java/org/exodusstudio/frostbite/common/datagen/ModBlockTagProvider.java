package org.exodusstudio.frostbite.common.datagen;

import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,  @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Frostbite.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        //this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
        //        .add(BlockRegistry.BLACK_BLOCK.get());

        // MISTY WOOD
        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(BlockRegistry.MISTY_LOG.get())
                .add(BlockRegistry.STRIPPED_MISTY_LOG.get())
                .add(BlockRegistry.MISTY_WOOD.get())
                .add(BlockRegistry.STRIPPED_MISTY_WOOD.get());

        // DIM WOOD
        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(BlockRegistry.DIM_LOG.get())
                .add(BlockRegistry.STRIPPED_DIM_LOG.get())
                .add(BlockRegistry.DIM_WOOD.get())
                .add(BlockRegistry.STRIPPED_DIM_WOOD.get());

        // SILVER WOOD
        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(BlockRegistry.SILVER_LOG.get())
                .add(BlockRegistry.STRIPPED_SILVER_LOG.get())
                .add(BlockRegistry.SILVER_WOOD.get())
                .add(BlockRegistry.STRIPPED_SILVER_WOOD.get());

        // CHARM WOOD
        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(BlockRegistry.CHARM_LOG.get())
                .add(BlockRegistry.STRIPPED_CHARM_LOG.get())
                .add(BlockRegistry.CHARM_WOOD.get())
                .add(BlockRegistry.STRIPPED_CHARM_WOOD.get());
    }
}
