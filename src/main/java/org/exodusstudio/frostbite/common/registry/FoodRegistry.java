package org.exodusstudio.frostbite.common.registry;

import net.minecraft.world.food.FoodProperties;

public class FoodRegistry {
    public static final FoodProperties WARMING_BOTTLE = (new FoodProperties.Builder()).nutrition(0).saturationModifier(0).alwaysEdible().build();
}
