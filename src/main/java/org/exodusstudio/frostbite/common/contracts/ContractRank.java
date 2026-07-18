package org.exodusstudio.frostbite.common.contracts;

import net.minecraft.util.StringRepresentable;

public enum ContractRank implements StringRepresentable {
    BLACK, // Best
    DARK,
    LIGHT,
    WHITE; // Worst

    public static final StringRepresentable.EnumCodec<ContractRank> CODEC =
            StringRepresentable.fromEnum(ContractRank::values);

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }

    public static ContractRank fromNum(int num) {
        return switch (num) {
            case 4 -> BLACK;
            case 3 -> DARK;
            case 2 -> LIGHT;
            case 1 -> WHITE;
            default -> throw new IllegalArgumentException("Invalid rank number: " + num);
        };
    }
}
