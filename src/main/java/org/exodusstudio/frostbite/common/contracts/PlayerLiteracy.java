package org.exodusstudio.frostbite.common.contracts;

import net.minecraft.util.StringRepresentable;

public enum PlayerLiteracy implements StringRepresentable {
    ILLITERATE,
    BASIC,
    PROFICIENT,
    LITERATE;

    public static final StringRepresentable.EnumCodec<PlayerLiteracy> CODEC =
            StringRepresentable.fromEnum(PlayerLiteracy::values);

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
