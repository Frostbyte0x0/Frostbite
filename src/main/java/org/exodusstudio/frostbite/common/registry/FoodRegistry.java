package org.exodusstudio.frostbite.common.registry;

import net.minecraft.world.food.FoodProperties;

public class FoodRegistry {
    public static final FoodProperties WARMING_BOTTLE =
            (new FoodProperties.Builder()).nutrition(0).saturationModifier(0).alwaysEdible().build();
    public static final FoodProperties RAW_BOAR =
            (new FoodProperties.Builder()).nutrition(4).saturationModifier(0.4f).build();
    public static final FoodProperties COOKED_BOAR =
            (new FoodProperties.Builder()).nutrition(9).saturationModifier(1).build();
    public static final FoodProperties SPICY_VEGETABLE_STEW =
            (new FoodProperties.Builder()).nutrition(4).saturationModifier(0.6f).build();
    public static final FoodProperties SPICY_FISH_SOUP =
            (new FoodProperties.Builder()).nutrition(7).saturationModifier(0.6f).build();
    public static final FoodProperties SPICY_MEAT_STEW =
            (new FoodProperties.Builder()).nutrition(10).saturationModifier(0.6f).build();
    public static final FoodProperties CHILLI =
            (new FoodProperties.Builder()).nutrition(2).saturationModifier(0).build();
}
