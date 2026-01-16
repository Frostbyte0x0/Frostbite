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
        float d = (Frostbite.temperatureStorage.getTemperature(getInstance(), true) - TemperatureStorage.MIN_INNER_TEMP) /
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
        return Map.of(
                Attributes.MOVEMENT_SPEED,
                new AttributeModifier(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "cold_speed_modifier"),
                lerpStrengthModifier(-0.3f, 0.3f), AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "cold_attack_modifier"),
                lerpStrengthModifier(-0.5f, 0.5f), AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                Attributes.ARMOR,
                new AttributeModifier(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "cold_armor_modifier"),
                lerpStrengthModifier(-0.3f, 0.3f), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    default void tickAttributes() {
        getAttributeModifiers().forEach((attribute, modifier) -> {
            if (getInstance().getAttribute(attribute) != null) getInstance().getAttribute(attribute).addOrUpdateTransientModifier(modifier);
        });
    }

    default float getSpawnTemperature() {
        return scalesWithCold() ? TemperatureStorage.MIN_TEMP : TemperatureStorage.MAX_TEMP;
    }

    LivingEntity getInstance();
}
