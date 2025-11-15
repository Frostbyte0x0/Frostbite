package org.exodusstudio.frostbite.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;

import java.util.function.Consumer;

public class SpicyStewItem extends Item {
    public SpicyStewItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("tooltip.frostbite.spicy_stew.tooltip")
                .append(": " + stack.get(DataComponentTypeRegistry.CHARGE.get()).charge()).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }
}
