package org.exodusstudio.frostbite.common.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Frostbite.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        //blockWithItem(BlockRegistry.BLACK_BLOCK);

        // MISTY WOOD
        logBlock((RotatedPillarBlock) BlockRegistry.MISTY_LOG.get());
        logBlock((RotatedPillarBlock) BlockRegistry.STRIPPED_MISTY_LOG.get());
        axisBlock((RotatedPillarBlock) BlockRegistry.MISTY_WOOD.get(),
                blockTexture(BlockRegistry.MISTY_LOG.get()), blockTexture(BlockRegistry.MISTY_LOG.get()));
        axisBlock((RotatedPillarBlock) BlockRegistry.STRIPPED_MISTY_WOOD.get(),
                blockTexture(BlockRegistry.STRIPPED_MISTY_LOG.get()), blockTexture(BlockRegistry.STRIPPED_MISTY_LOG.get()));

        blockItem(BlockRegistry.MISTY_LOG);
        blockItem(BlockRegistry.STRIPPED_MISTY_LOG);
        blockItem(BlockRegistry.MISTY_WOOD);
        blockItem(BlockRegistry.STRIPPED_MISTY_WOOD);

        blockWithItem(BlockRegistry.MISTY_PLANKS);

        leavesBlock(BlockRegistry.MISTY_LEAVES);
        saplingBlock(BlockRegistry.MISTY_SAPLING);

        // DIM WOOD
        logBlock((RotatedPillarBlock) BlockRegistry.DIM_LOG.get());
        logBlock((RotatedPillarBlock) BlockRegistry.STRIPPED_DIM_LOG.get());
        axisBlock((RotatedPillarBlock) BlockRegistry.DIM_WOOD.get(),
                blockTexture(BlockRegistry.DIM_LOG.get()), blockTexture(BlockRegistry.DIM_LOG.get()));
        axisBlock((RotatedPillarBlock) BlockRegistry.STRIPPED_DIM_WOOD.get(),
                blockTexture(BlockRegistry.STRIPPED_DIM_LOG.get()), blockTexture(BlockRegistry.STRIPPED_DIM_LOG.get()));

        blockItem(BlockRegistry.DIM_LOG);
        blockItem(BlockRegistry.STRIPPED_DIM_LOG);
        blockItem(BlockRegistry.DIM_WOOD);
        blockItem(BlockRegistry.STRIPPED_DIM_WOOD);

        blockWithItem(BlockRegistry.DIM_PLANKS);

        leavesBlock(BlockRegistry.DIM_LEAVES);
        saplingBlock(BlockRegistry.DIM_SAPLING);

        // SILVER WOOD
        logBlock((RotatedPillarBlock) BlockRegistry.SILVER_LOG.get());
        logBlock((RotatedPillarBlock) BlockRegistry.STRIPPED_SILVER_LOG.get());
        axisBlock((RotatedPillarBlock) BlockRegistry.SILVER_WOOD.get(),
                blockTexture(BlockRegistry.SILVER_LOG.get()), blockTexture(BlockRegistry.SILVER_LOG.get()));
        axisBlock((RotatedPillarBlock) BlockRegistry.STRIPPED_SILVER_WOOD.get(),
                blockTexture(BlockRegistry.STRIPPED_SILVER_LOG.get()), blockTexture(BlockRegistry.STRIPPED_SILVER_LOG.get()));

        blockItem(BlockRegistry.SILVER_LOG);
        blockItem(BlockRegistry.STRIPPED_SILVER_LOG);
        blockItem(BlockRegistry.SILVER_WOOD);
        blockItem(BlockRegistry.STRIPPED_SILVER_WOOD);

        blockWithItem(BlockRegistry.SILVER_PLANKS);

        leavesBlock(BlockRegistry.SILVER_LEAVES);
        saplingBlock(BlockRegistry.SILVER_SAPLING);

        // CHARM WOOD
        logBlock((RotatedPillarBlock) BlockRegistry.CHARM_LOG.get());
        logBlock((RotatedPillarBlock) BlockRegistry.STRIPPED_CHARM_LOG.get());
        axisBlock((RotatedPillarBlock) BlockRegistry.CHARM_WOOD.get(),
                blockTexture(BlockRegistry.CHARM_LOG.get()), blockTexture(BlockRegistry.CHARM_LOG.get()));
        axisBlock((RotatedPillarBlock) BlockRegistry.STRIPPED_CHARM_WOOD.get(),
                blockTexture(BlockRegistry.STRIPPED_CHARM_LOG.get()), blockTexture(BlockRegistry.STRIPPED_CHARM_LOG.get()));

        blockItem(BlockRegistry.CHARM_LOG);
        blockItem(BlockRegistry.STRIPPED_CHARM_LOG);
        blockItem(BlockRegistry.CHARM_WOOD);
        blockItem(BlockRegistry.STRIPPED_CHARM_WOOD);

        blockWithItem(BlockRegistry.CHARM_PLANKS);

        leavesBlock(BlockRegistry.CHARM_LEAVES);
        saplingBlock(BlockRegistry.CHARM_SAPLING);
    }

    private void leavesBlock(DeferredBlock<Block> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(),
                models().singleTexture(BuiltInRegistries.BLOCK.getKey(deferredBlock.get()).getPath(), ResourceLocation.parse("minecraft:block/leaves"),
                        "all", blockTexture(deferredBlock.get())).renderType("cutout"));
    }

    private void saplingBlock(DeferredBlock<Block> deferredBlock) {
        simpleBlock(deferredBlock.get(), models().cross(BuiltInRegistries.BLOCK.getKey(deferredBlock.get()).getPath(), blockTexture(deferredBlock.get())).renderType("cutout"));
    }

    private void blockWithItem(DeferredBlock<Block> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void blockItem(DeferredBlock<Block> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("frostbite:block/" + deferredBlock.getId().getPath()));
    }
}
