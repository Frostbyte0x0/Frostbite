package com.frostbyte.frostbite.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class DrainingStaffItem extends Item {
    public int charge = 0;
    public String mode = "drain";
    public String[] modes = {"drain", "blast", "ray", "shield", "heal"};
    public int mode_index = 0;

    public DrainingStaffItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        if (player.isShiftKeyDown()) {
            this.mode_index += 1;
            if (this.mode_index == modes.length) {
                this.mode_index = 0;
            }
            this.mode = modes[mode_index];
        }
        else {
            switch (mode) {
                case "drain":
                    this.charge += 10;
                case "blast":
                    player.displayClientMessage(Component.literal("Blast away!"), false);
                case "ray":
                    player.displayClientMessage(Component.literal("Pew pew"), false);
                case "shield":
                    player.displayClientMessage(Component.literal("Blast away!"), false);
                case "heal":
                    player.displayClientMessage(Component.literal("Blast away!"), false);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.literal(String.format("Charge: %s\nMode: %s", this.charge, this.mode)).withStyle(ChatFormatting.GRAY));
    }
}
