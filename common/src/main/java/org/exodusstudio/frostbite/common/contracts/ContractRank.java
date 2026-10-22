package org.exodusstudio.frostbite.common.contracts;

import net.minecraft.util.StringRepresentable;

public enum ContractRank implements StringRepresentable {
    BLACK, // Best
    GREY,
    WHITE; // Worst

    public static final StringRepresentable.EnumCodec<ContractRank> CODEC =
            StringRepresentable.fromEnum(ContractRank::values);

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }

    public static ContractRank fromNum(int num) {
        return switch (num) {
            case 3 -> BLACK;
            case 2 -> GREY;
            case 1 -> WHITE;
            default -> throw new IllegalArgumentException("Invalid rank number: " + num);
        };
    }
}
