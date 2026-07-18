package org.exodusstudio.frostbite.common.contracts;

import net.minecraft.util.StringRepresentable;

public enum Polarity implements StringRepresentable {
    POSITIVE,
    NEGATIVE;

    public static final StringRepresentable.EnumCodec<Polarity> CODEC =
            StringRepresentable.fromEnum(Polarity::values);

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
