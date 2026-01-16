package org.exodusstudio.frostbite.common.util;

import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;

import static org.exodusstudio.frostbite.common.util.TemperatureManager.*;

public interface TE {
    float getInnerTemp();
    void setInnerTemp(float temp);
    float getOuterTemp();
    void setOuterTemp(float temp);

    default void decreaseTemperature(float decrease, boolean inner) {
        if (instance().hasEffect(EffectRegistry.COLD_WEAKNESS)) {
            decrease *= (1 + 0.2f * instance().getEffect(EffectRegistry.COLD_WEAKNESS).getAmplifier());
        }

        if (inner) {
            ((TE) instance()).setInnerTemp(Math.max(decrease + ((TE) instance()).getInnerTemp(), MIN_INNER_TEMP));
        } else {
            ((TE) instance()).setOuterTemp(Math.min(decrease + ((TE) instance()).getOuterTemp(), MIN_TEMP));
        }
    }

    default void increaseTemperature(float temperature, boolean inner) {
        if (inner) {
            ((TE) instance()).setInnerTemp(Math.min(temperature + ((TE) instance()).getInnerTemp(), MAX_TEMP));
        } else {
            ((TE) instance()).setOuterTemp(Math.min(temperature + ((TE) instance()).getOuterTemp(), MAX_TEMP));
        }
    }

    LivingEntity instance();
}
