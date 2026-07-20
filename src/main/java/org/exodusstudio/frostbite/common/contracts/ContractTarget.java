package org.exodusstudio.frostbite.common.contracts;

import net.minecraft.util.StringRepresentable;

public enum ContractTarget implements StringRepresentable {
    PLAYER,
    LIVING,
    WEAPON,
    LOCATION;

    public static final StringRepresentable.EnumCodec<ContractTarget> CODEC =
            StringRepresentable.fromEnum(ContractTarget::values);

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
