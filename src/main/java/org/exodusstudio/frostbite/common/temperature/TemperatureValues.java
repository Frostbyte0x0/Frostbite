package org.exodusstudio.frostbite.common.temperature;

import java.util.HashMap;

public class TemperatureValues {
    public static HashMap<String, Float> TEMPERATURE_PER_BIOME = new HashMap<>();
    public static HashMap<String, Float> TEMPERATURE_PER_BLOCK = new HashMap<>();

    public static void addTemperatures() {
        TEMPERATURE_PER_BIOME.put("the_void", -20f);
        TEMPERATURE_PER_BIOME.put("snowy_plains", -10f);
        TEMPERATURE_PER_BIOME.put("ice_spikes", -25f);
        TEMPERATURE_PER_BIOME.put("birch_forest", 15f);
        TEMPERATURE_PER_BIOME.put("old_growth_birch_forest", 15f);
        TEMPERATURE_PER_BIOME.put("old_growth_pine_taiga", 10f);
        TEMPERATURE_PER_BIOME.put("old_growth_spruce_taiga", 10f);
        TEMPERATURE_PER_BIOME.put("taiga", 10f);
        TEMPERATURE_PER_BIOME.put("snowy_taiga", -15f);
        TEMPERATURE_PER_BIOME.put("windswept_hills", 0f);
        TEMPERATURE_PER_BIOME.put("windswept_gravelly_hills", 0f);
        TEMPERATURE_PER_BIOME.put("windswept_forest", 0f);
        TEMPERATURE_PER_BIOME.put("meadow", 20f);
        TEMPERATURE_PER_BIOME.put("cherry_grove", 20f);
        TEMPERATURE_PER_BIOME.put("grove", 20f);
        TEMPERATURE_PER_BIOME.put("snowy_slopes", -10f);
        TEMPERATURE_PER_BIOME.put("frozen_peaks", -10f);
        TEMPERATURE_PER_BIOME.put("jagged_peaks", -20f);
        TEMPERATURE_PER_BIOME.put("stony_peaks", 10f);
        TEMPERATURE_PER_BIOME.put("river", 5f);
        TEMPERATURE_PER_BIOME.put("frozen_river", -10f);
        TEMPERATURE_PER_BIOME.put("snowy_beach", -10f);
        TEMPERATURE_PER_BIOME.put("stony_shore", 10f);
        TEMPERATURE_PER_BIOME.put("ocean", 5f);
        TEMPERATURE_PER_BIOME.put("deep_ocean", 5f);
        TEMPERATURE_PER_BIOME.put("cold_ocean", 0f);
        TEMPERATURE_PER_BIOME.put("deep_cold_ocean", 0f);
        TEMPERATURE_PER_BIOME.put("frozen_ocean", -10f);
        TEMPERATURE_PER_BIOME.put("deep_frozen_ocean", -10f);
        TEMPERATURE_PER_BIOME.put("mushroom_fields", 20f);
        TEMPERATURE_PER_BIOME.put("dripstone_caves", 20f);
        TEMPERATURE_PER_BIOME.put("lush_caves", 20f);
        TEMPERATURE_PER_BIOME.put("deep_dark", 10f);


        TEMPERATURE_PER_BLOCK.put("lava", 10f);
        TEMPERATURE_PER_BLOCK.put("torch", 5f);
        TEMPERATURE_PER_BLOCK.put("wall_torch", 5f);
        TEMPERATURE_PER_BLOCK.put("lantern", 5f);
        TEMPERATURE_PER_BLOCK.put("soul_lantern", 5f);
        TEMPERATURE_PER_BLOCK.put("campfire", 15f);
        TEMPERATURE_PER_BLOCK.put("soul_campfire", 15f);
        TEMPERATURE_PER_BLOCK.put("fire", 10f);
        TEMPERATURE_PER_BLOCK.put("furnace", 10f);
        TEMPERATURE_PER_BLOCK.put("blast_furnace", 10f);
        TEMPERATURE_PER_BLOCK.put("smoker", 10f);
    }
}
