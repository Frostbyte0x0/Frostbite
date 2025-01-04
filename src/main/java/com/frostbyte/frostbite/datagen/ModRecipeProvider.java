package com.frostbyte.frostbite.datagen;

import com.frostbyte.frostbite.block.ModBlocks;
import com.frostbyte.frostbite.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        final HolderLookup.RegistryLookup<Item> holderGetter = registries.lookupOrThrow(Registries.ITEM);

        ShapedRecipeBuilder.shaped(holderGetter, RecipeCategory.MISC, ModBlocks.BLACK_BLOCK.get())
                .pattern("MMM")
                .pattern("MMM")
                .pattern("MMM")
                .define('M', ModItems.METAL_COG.get())
                .unlockedBy("has_metal_cog", has(ModItems.METAL_COG.get())).save(output);
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
