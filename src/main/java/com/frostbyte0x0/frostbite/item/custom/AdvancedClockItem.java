package com.frostbyte0x0.frostbite.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

import static net.minecraft.util.Mth.floor;

public class AdvancedClockItem extends Item {
    private boolean used;

    public AdvancedClockItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand usedHand) {
        ItemUtils.startUsingInstantly(level, player, usedHand);
        if (used) {
            float t = (level.getTimeOfDay(1.0f) * 24) - 12f;
            if (t < 0) t += 24;
            String mShown = Integer.toString(floor((t - floor(t)) * 60));
            String hShown = Integer.toString(floor(t));
            if (mShown.length() == 1) mShown = "0" + mShown;
            if (hShown.length() == 1) hShown = "0" + hShown;
            player.displayClientMessage(Component.literal(String.format("It is currently: %sh%s", hShown, mShown)), false);
        }
        used = !used;
        return super.use(level, player, usedHand);
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        pTooltipComponents.add(Component.translatable("tooltip.frostbite.advanced_clock.tooltip").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    }
}