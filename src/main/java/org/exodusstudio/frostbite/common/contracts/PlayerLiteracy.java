package org.exodusstudio.frostbite.common.contracts;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum PlayerLiteracy implements StringRepresentable {
    ILLITERATE(0, Component.translatable("contract.player_literacy.illiterate"),
            Component.translatable("contract.player_literacy.illiterate_desc")), // Positive/negative
    BASIC(3, Component.translatable("contract.player_literacy.basic"),
            Component.translatable("contract.player_literacy.basic_desc")), // Title
    PROFICIENT(6, Component.translatable("contract.player_literacy.proficient"),
            Component.translatable("contract.player_literacy.proficient_desc")), // Description
    LITERATE(9, Component.translatable("contract.player_literacy.literate"),
            Component.translatable("contract.player_literacy.literate_desc")); // Numbers

    public final int discoveredNb;
    public final Component title;
    public final Component description;
    PlayerLiteracy(int discoveredNb, Component title, Component description) {
        this.discoveredNb = discoveredNb;
        this.title = title;
        this.description = description;
    }

    public static final StringRepresentable.EnumCodec<PlayerLiteracy> CODEC =
            StringRepresentable.fromEnum(PlayerLiteracy::values);

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }

    public boolean hasNext() {
        return this.ordinal() < PlayerLiteracy.values().length - 1;
    }

    public PlayerLiteracy next() {
        return PlayerLiteracy.values()[this.ordinal() + 1];
    }
}
