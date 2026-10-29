package org.exodusstudio.frostbite.common.util;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class PercentageAttribute extends RangedAttribute {
    public PercentageAttribute(String descriptionId, double defaultValue, double min, double max) {
        super(descriptionId, defaultValue, min, max);
    }
}
