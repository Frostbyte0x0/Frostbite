package org.exodusstudio.frostbite.common.contracts;

import net.minecraft.util.StringRepresentable;

public enum PlayerLiteracy implements StringRepresentable {
    ILLITERATE, // Positive/negative
    BASIC, // Title
    PROFICIENT, // Description
    LITERATE; // Numbers

    public static final StringRepresentable.EnumCodec<PlayerLiteracy> CODEC =
            StringRepresentable.fromEnum(PlayerLiteracy::values);

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
