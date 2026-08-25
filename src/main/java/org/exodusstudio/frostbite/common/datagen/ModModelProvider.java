package org.exodusstudio.frostbite.common.datagen;

import com.mojang.math.Quadrant;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

import java.util.stream.Collectors;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, Frostbite.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModels.family(BlockRegistry.SLATED_SNOW_BRICKS.get())
//                .fullBlock(BlockRegistry.SLATED_SNOW_BRICKS.get(), ModelTemplates.CUBE)
                .stairs(BlockRegistry.SLATED_SNOW_BRICK_STAIRS.get())
                .slab(BlockRegistry.SLATED_SNOW_BRICK_SLAB.get())
                .wall(BlockRegistry.SLATED_SNOW_BRICK_WALL.get());

        blockModels.family(BlockRegistry.SLATED_SNOW_TILES.get())
//                .fullBlock(BlockRegistry.SLATED_SNOW_TILES.get(), ModelTemplates.CUBE)
                .stairs(BlockRegistry.SLATED_SNOW_TILE_STAIRS.get())
                .slab(BlockRegistry.SLATED_SNOW_TILE_SLAB.get())
                .wall(BlockRegistry.SLATED_SNOW_TILE_WALL.get());

        blockModels.family(BlockRegistry.COBBLED_SLATED_SNOW.get())
//                .fullBlock(BlockRegistry.COBBLED_SLATED_SNOW.get(), ModelTemplates.CUBE)
                .stairs(BlockRegistry.COBBLED_SLATED_SNOW_STAIRS.get())
                .slab(BlockRegistry.COBBLED_SLATED_SNOW_SLAB.get())
                .wall(BlockRegistry.COBBLED_SLATED_SNOW_WALL.get());

        blockModels.family(BlockRegistry.POLISHED_SLATED_SNOW.get())
//                .fullBlock(BlockRegistry.POLISHED_SLATED_SNOW.get(), ModelTemplates.CUBE)
                .stairs(BlockRegistry.POLISHED_SLATED_SNOW_STAIRS.get())
                .slab(BlockRegistry.POLISHED_SLATED_SNOW_SLAB.get())
                .wall(BlockRegistry.POLISHED_SLATED_SNOW_WALL.get());



        for (Item i : ItemRegistry.ITEMS.getEntries().stream().toList().stream().map(h -> h.asOptional().orElseThrow()).collect(Collectors.toSet())) {
            if (i.getDescriptionId().contains("slated_snow") &&
                    !i.getDescriptionId().equals("item.frostbite.slated_snow_pillar") &&
                    !i.getDescriptionId().equals("item.frostbite.ornate_slated_snow") &&
                    !i.getDescriptionId().equals("item.frostbite.chiseled_slated_snow") &&
                    !i.getDescriptionId().equals("item.frostbite.slated_snow")
            ) continue;
            itemModels.generateFlatItem(i, ModelTemplates.FLAT_ITEM);
        }


        for (Block b : BlockRegistry.BLOCKS.getEntries().stream().toList().stream().map(h -> h.asOptional().orElseThrow()).collect(Collectors.toSet())) {
            if (b.getName().getString().contains("slated_snow") &&
                    !b.getDescriptionId().equals("block.frostbite.slated_snow_pillar") &&
                    !b.getDescriptionId().equals("block.frostbite.ornate_slated_snow") &&
                    !b.getDescriptionId().equals("block.frostbite.chiseled_slated_snow") &&
                    !b.getDescriptionId().equals("block.frostbite.slated_snow")
            ) continue;
            Identifier modelLoc = TexturedModel.CUBE.create(b, blockModels.modelOutput);
            Variant variant = new Variant(modelLoc);
            blockModels.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(
                            b,
                            new MultiVariant(
                                    WeightedList.of(
                                            new Weighted<>(
                                                    // Set model
                                                    variant
                                                            // Set rotations around the x and y axes
                                                            .with(VariantMutator.X_ROT.withValue(Quadrant.R90))
                                                            .with(VariantMutator.Y_ROT.withValue(Quadrant.R180))
                                                            // Set a uvlock
                                                            .with(VariantMutator.UV_LOCK.withValue(true)),
                                                    // Set a weight
                                                    5
                                            ).value()
                                    )
                            )
                    )
            );
        }

//
//        // Add one or multiple models based on the block state properties
//        blockModels.blockStateOutput.accept(
//                MultiVariantGenerator.dispatch(
//                        slatedSnowBricks,
//                        // Create the basic multi-variant
//                        BlockModelGenerators.variant(variant)
//                ).with(
//                        // Apply a property dispatch
//                        // Will mutate the variant based on the provided mutators
//                        PropertyDispatch.modify(BlockStateProperties.AXIS)
//                                .select(Direction.Axis.Y, BlockModelGenerators.NOP)
//                                .select(Direction.Axis.Z, BlockModelGenerators.X_ROT_90)
//                                .select(Direction.Axis.X, BlockModelGenerators.X_ROT_90.then(BlockModelGenerators.Y_ROT_90))
//                )
//        );
//
//        // Generate a multipart
//        blockModels.blockStateOutput.accept(
//                MultiPartGenerator.multiPart(slatedSnowBricks)
//                        // Provide the base model
//                        .with(BlockModelGenerators.variant(variant))
//                        // Add conditions for variant to appear
//                        .with(
//                                // Add conditions to apply
//                                new CombinedCondition(
//                                        CombinedCondition.Operation.OR,
//                                        List.of(
//                                                // Where at least one of the conditions are true
//                                                BlockModelGenerators.condition().term(BlockStateProperties.FACING, Direction.NORTH, Direction.SOUTH),
//                                                // Can nest as many conditions or groups as necessary
//        new CombinedCondition(
//                CombinedCondition.Operation.AND,
//                List.of(
//                        BlockModelGenerators.condition().term(BlockStateProperties.FACING, Direction.NORTH)
//                )
//        )
//                        )
//                    ),
//        // Supply variant to mutate
//        BlockModelGenerators.variant(variant)
//                )
//        );
    }
}
