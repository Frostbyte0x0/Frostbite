package org.exodusstudio.frostbite.client.gui.scribing;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.display.RecipeDisplay;

import java.util.List;

public class BSRecipeBookComponent extends RecipeBookComponent<DecipheringMenu> {
    private static final WidgetSprites FILTER_SPRITES = new WidgetSprites(
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled"),
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled"),
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_enabled_highlighted"),
            Identifier.withDefaultNamespace("recipe_book/furnace_filter_disabled_highlighted"));

    public BSRecipeBookComponent(DecipheringMenu menu) {
        super(menu, List.of());
    }

    @Override
    protected WidgetSprites getFilterButtonTextures() {
        return FILTER_SPRITES;
    }

    @Override
    protected boolean isCraftingSlot(Slot slot) {
        return false;
    }

    @Override
    protected void selectMatchingRecipes(RecipeCollection recipeCollection, StackedItemContents stackedItemContents) {}

    @Override
    protected Component getRecipeFilterName() {
        return Component.empty();
    }

    @Override
    protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {}
}
