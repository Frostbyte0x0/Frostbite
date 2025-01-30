package org.exodusstudio.frostbite.common.datagen;

import org.exodusstudio.frostbite.common.registry.BlockRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        final HolderLookup.RegistryLookup<Item> holderGetter = registries.lookupOrThrow(Registries.ITEM);

        ShapedRecipeBuilder.shaped(holderGetter, RecipeCategory.MISC, BlockRegistry.BLACK_BLOCK.get())
                .pattern("MMM")
                .pattern("MMM")
                .pattern("MMM")
                .define('M', ItemRegistry.METAL_COG.get())
                .unlockedBy("has_metal_cog", has(ItemRegistry.METAL_COG.get())).save(output);

        ShapedRecipeBuilder.shaped(holderGetter, RecipeCategory.MISC, ItemRegistry.VIAL.get())
                .pattern(" G ")
                .pattern("G G")
                .pattern(" G ")
                .define('G', Items.GLASS.asItem())
                .unlockedBy("has_glass", has(Items.GLASS.asItem())).save(output);

        ShapedRecipeBuilder.shaped(holderGetter, RecipeCategory.MISC, ItemRegistry.EMPTY_JAR.get())
                .pattern("GGG")
                .pattern("G G")
                .pattern("GGG")
                .define('G', Items.GLASS.asItem())
                .unlockedBy("has_glass", has(Items.GLASS.asItem())).save(output);

        ShapedRecipeBuilder.shaped(holderGetter, RecipeCategory.MISC, ItemRegistry.EXPLODING_SNOWBALL.get(), 4)
                .pattern("SSS")
                .pattern("STS")
                .pattern("SSS")
                .define('S', Items.SNOWBALL.asItem())
                .define('T', Items.TNT.asItem())
                .unlockedBy("has_snowball", has(Items.SNOWBALL.asItem())).save(output);

        ShapedRecipeBuilder.shaped(holderGetter, RecipeCategory.MISC, ItemRegistry.HARDENED_SNOWBALL.get(), 4)
                .pattern("III")
                .pattern("ISI")
                .pattern("III")
                .define('I', Items.ICE.asItem())
                .define('S', Items.SNOWBALL.asItem())
                .unlockedBy("has_snowball", has(Items.SNOWBALL.asItem())).save(output);

        ShapedRecipeBuilder.shaped(holderGetter, RecipeCategory.MISC, ItemRegistry.PACKED_HARDENED_SNOWBALL.get(), 4)
                .pattern("PPP")
                .pattern("PSP")
                .pattern("PPP")
                .define('P', Items.PACKED_ICE.asItem())
                .define('S', Items.SNOWBALL.asItem())
                .unlockedBy("has_snowball", has(Items.SNOWBALL.asItem())).save(output);

        ShapedRecipeBuilder.shaped(holderGetter, RecipeCategory.MISC, ItemRegistry.BLUE_HARDENED_SNOWBALL.get(), 4)
                .pattern("BBB")
                .pattern("BSB")
                .pattern("BBB")
                .define('B', Items.BLUE_ICE.asItem())
                .define('S', Items.SNOWBALL.asItem())
                .unlockedBy("has_snowball", has(Items.SNOWBALL.asItem())).save(output);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter) {
            return new ModRecipeProvider(registryLookup, exporter);
        }

        @Override
        public String getName() {
            return "Recipes";
        }
    }
}
