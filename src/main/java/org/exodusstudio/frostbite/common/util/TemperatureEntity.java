package org.exodusstudio.frostbite.common.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.exodusstudio.frostbite.Frostbite;

import java.util.Map;

public interface TemperatureEntity {
    default int getBaseOuterTempIncrease() {
        return 0;
    }

    default boolean scalesWithCold() {
        return true;
    }

    default float getStrengthModifier() {
        float d = (Frostbite.temperatureStorage.getTemperature(getInstance(), true) + TemperatureStorage.MIN_INNER_TEMP) /
                (TemperatureStorage.MAX_TEMP - TemperatureStorage.MIN_INNER_TEMP);
        if (scalesWithCold()) {
            return 1 - d;
        }
        return d;
    }

    default float lerpStrengthModifier(float min, float max) {
        return Mth.lerp(getStrengthModifier(), min, max);
    }

    default double lerpStrengthModifier(double min, double max) {
        return Mth.lerp(getStrengthModifier(), min, max);
    }

    default Map<Holder<Attribute>, AttributeModifier> getAttributeModifiers() {
        return Map.of(Attributes.MOVEMENT_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "cold_speed_modifier"),
                lerpStrengthModifier(-0.15f, 0.15f), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    }

    LivingEntity getInstance();
}
