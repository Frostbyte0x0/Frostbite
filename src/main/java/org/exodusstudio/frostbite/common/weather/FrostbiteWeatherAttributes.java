package org.exodusstudio.frostbite.common.weather;

import com.google.common.collect.Sets;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeMap;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.modifier.ColorModifier;
import net.minecraft.world.attribute.modifier.FloatModifier;
import net.minecraft.world.attribute.modifier.FloatWithAlpha;
import net.minecraft.world.timeline.Timelines;
import org.exodusstudio.frostbite.Frostbite;

import java.util.Set;

public class FrostbiteWeatherAttributes {
    public static final EnvironmentAttributeMap BLIZZARD =
            EnvironmentAttributeMap.builder()
            .modify(EnvironmentAttributes.SKY_COLOR, ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.6F, 0.75F))
            .modify(EnvironmentAttributes.CLOUD_COLOR, ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.24F, 0.5F))
            .modify(EnvironmentAttributes.SKY_LIGHT_LEVEL, FloatModifier.ALPHA_BLEND, new FloatWithAlpha(4.0F, 0.3125F))
            .modify(EnvironmentAttributes.SKY_LIGHT_COLOR, ColorModifier.ALPHA_BLEND, ARGB.color(0.3125F, Timelines.NIGHT_SKY_LIGHT_COLOR))
            .modify(EnvironmentAttributes.SKY_LIGHT_FACTOR, FloatModifier.ALPHA_BLEND, new FloatWithAlpha(0.24F, 0.3125F))
            .set(EnvironmentAttributes.FOG_START_DISTANCE, -50f)
            .set(EnvironmentAttributes.FOG_END_DISTANCE, 50f)
            .set(EnvironmentAttributes.FOG_COLOR, ARGB.color(255, 125, 125, 200))
            .set(EnvironmentAttributes.STAR_BRIGHTNESS, 0.0F)
            .set(EnvironmentAttributes.BEES_STAY_IN_HIVE, true).build();
    public static final EnvironmentAttributeMap WHITEOUT =
            EnvironmentAttributeMap.builder()
            .modify(EnvironmentAttributes.SKY_COLOR, ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.24F, 0.94F))
            .modify(EnvironmentAttributes.CLOUD_COLOR, ColorModifier.BLEND_TO_GRAY, new ColorModifier.BlendToGray(0.095F, 0.94F))
            .modify(EnvironmentAttributes.SKY_LIGHT_LEVEL, FloatModifier.ALPHA_BLEND, new FloatWithAlpha(4.0F, 0.52734375F))
            .modify(EnvironmentAttributes.SKY_LIGHT_COLOR, ColorModifier.ALPHA_BLEND, ARGB.color(0.52734375F, Timelines.NIGHT_SKY_LIGHT_COLOR))
            .modify(EnvironmentAttributes.SKY_LIGHT_FACTOR, FloatModifier.ALPHA_BLEND, new FloatWithAlpha(0.24F, 0.52734375F))
            .set(EnvironmentAttributes.FOG_START_DISTANCE, -50f)
            .set(EnvironmentAttributes.FOG_END_DISTANCE, 25f)
            .set(EnvironmentAttributes.FOG_COLOR, ARGB.color(255, 150, 150, 255))
            .set(EnvironmentAttributes.STAR_BRIGHTNESS, 0.0F)
            .set(EnvironmentAttributes.BEES_STAY_IN_HIVE, true).build();
    private static final Set<EnvironmentAttribute<?>> WEATHER_ATTRIBUTES = Sets.union(BLIZZARD.keySet(), WHITEOUT.keySet());

    public static void addBuiltinLayers(EnvironmentAttributeSystem.Builder builder, WeatherAccess access) {
        for (EnvironmentAttribute<?> environmentattribute : WEATHER_ATTRIBUTES) {
            addLayer(builder, access, environmentattribute);
        }
    }

    private static <Value> void addLayer(EnvironmentAttributeSystem.Builder builder, WeatherAccess access, EnvironmentAttribute<Value> attribute) {
        EnvironmentAttributeMap.Entry<Value, ?> entry = BLIZZARD.get(attribute);
        EnvironmentAttributeMap.Entry<Value, ?> entry1 = WHITEOUT.get(attribute);

        builder.addTimeBasedLayer(attribute, (v, ignored) -> {
            float f = access.whiteoutLevel();
            float f1 = access.blizzardLevel() - f;
            if (entry != null && f1 > 0.0F) {
                Value value = entry.applyModifier(v);
                v = attribute.type().stateChangeLerp().apply(f1, v, value);
            }

            if (entry1 != null && f > 0.0F) {
                Value value1 = entry1.applyModifier(v);
                v = attribute.type().stateChangeLerp().apply(f, v, value1);
            }

            return v;
        });
    }

    public interface WeatherAccess {
        static WeatherAccess from() {
            return new WeatherAccess() {
                public float blizzardLevel() {
                    return Frostbite.weatherInfo.blizzardLevel;
                }

                public float whiteoutLevel() {
                    return Frostbite.weatherInfo.whiteoutLevel;
                }
            };
        }

        float blizzardLevel();

        float whiteoutLevel();
    }
}
