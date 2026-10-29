package org.exodusstudio.frostbite.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.exodusstudio.frostbite.common.effect.RaiseTemperatureConsumeEffect;

import java.util.function.Consumer;

public class BottleItem extends Item {
    public BottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        getDefaultInstance().get(DataComponents.CONSUMABLE).onConsumeEffects().forEach(c -> {
            if (c instanceof RaiseTemperatureConsumeEffect e) {
                float outer = e.temps().getFirst();
                float inner = e.temps().getFirst();
                tooltipAdder.accept(Component.translatable("tooltip.frostbite.bottle_inner.tooltip", String.format("%s", inner)).withStyle(ChatFormatting.DARK_RED));
                tooltipAdder.accept(Component.translatable("tooltip.frostbite.bottle_outer.tooltip", String.format("%s", outer)).withStyle(ChatFormatting.DARK_RED));
            }
        });
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }
}
