package org.exodusstudio.frostbite.common.temperature;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.exodusstudio.frostbite.common.registry.AttachmentTypeRegistry;

import static org.exodusstudio.frostbite.common.temperature.TemperatureValues.TEMPERATURE_PER_BIOME;
import static org.exodusstudio.frostbite.common.temperature.TemperatureValues.TEMPERATURE_PER_BLOCK;

public class TemperatureCalculator {
    public static void updateTemperature(Player player) {
        float outerTemp = calculateOuterTemperature(player);
        //player.setData(AttachmentTypeRegistry.OUTER_TEMPERATURE, outerTemp);

        //float innerTemp = calculateInnerTemperature(player.getData(AttachmentTypeRegistry.INNER_TEMPERATURE), outerTemp);
        //player.setData(AttachmentTypeRegistry.INNER_TEMPERATURE, innerTemp);
    }

    public static float calculateOuterTemperature(Player player) {
        float temp = 0f;

        temp += calculateBiomeTemperatureModifier(player);
        temp += calculateBlocksTemperatureModifier(player);
        temp += calculateArmourTemperatureModifier(player);

        return temp;
    }

    public static float calculateBiomeTemperatureModifier(Player player) {
        Holder<Biome> biome = player.level().getBiome(player.blockPosition());
        String biome_name = biome.getRegisteredName().replace("minecraft:", "");
        if (TEMPERATURE_PER_BIOME.containsKey(biome_name)) {
            return TEMPERATURE_PER_BIOME.get(biome_name);
        }
        else {
            return biome.value().getBaseTemperature() * 30;
        }
    }

    public static float calculateBlocksTemperatureModifier(Player player) {
        float temp = 0f;

        if (player.isInWater()) {
            temp -= 5f;
        }

        Block[] blocks = (Block[]) player.level().getBlockStates(player.getBoundingBox().inflate(3))
                .filter(TemperatureCalculator::isTemperatureModifyingBlock).toArray();

        float max_heat = 0f;
        float heat;

        for (Object block : blocks) {
            heat = TEMPERATURE_PER_BLOCK.get(block.toString());
            if (heat > max_heat) {
                max_heat = heat;
            }
        }

        return temp;
    }

    public static boolean isTemperatureModifyingBlock(BlockState state) {
        return TEMPERATURE_PER_BLOCK.containsKey(state.getBlock().toString());
    }

    public static float calculateArmourTemperatureModifier(Player player) {


        return 0f;
    }

    public static float calculateInnerTemperature(float innerTemp, float outerTemp) {
        if (innerTemp == outerTemp) {
            return innerTemp;
        }

        float speed = 0.2f;

        if (innerTemp > outerTemp) {
            innerTemp -= (speed / 20);
        }
        else {
            innerTemp += (speed / 10);
        }

        return innerTemp;
    }
}
