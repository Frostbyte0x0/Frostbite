package org.exodusstudio.frostbite.common.item.armour;

import net.minecraft.ChatFormatting;
import net.minecraft.util.StringRepresentable;
import org.exodusstudio.frostbite.common.util.Util;

public enum ArmourCleanliness implements StringRepresentable {
    RUSTY(ChatFormatting.BLUE),
    REGULAR(ChatFormatting.DARK_AQUA),
    SPARKLING(ChatFormatting.AQUA);

    public static final StringRepresentable.EnumCodec<ArmourCleanliness> CODEC =
            StringRepresentable.fromEnum(ArmourCleanliness::values);

    public final ChatFormatting formatting;
    ArmourCleanliness(ChatFormatting formatting) {
        this.formatting = formatting;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }

    public static ArmourCleanliness random() {
        return values()[Util.random.nextIntBetweenInclusive(0, 2)];
    }
}
