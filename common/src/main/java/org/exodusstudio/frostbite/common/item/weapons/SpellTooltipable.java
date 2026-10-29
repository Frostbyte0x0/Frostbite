package org.exodusstudio.frostbite.common.item.weapons;

import net.minecraft.ChatFormatting;

public interface SpellTooltipable {
    String[] getModes();
    ChatFormatting regularColour();
    ChatFormatting selectedColour();
}
