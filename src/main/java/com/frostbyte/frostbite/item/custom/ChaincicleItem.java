package com.frostbyte.frostbite.item.custom;

import com.frostbyte.frostbite.component.ChargeData;
import com.frostbyte.frostbite.component.ModDataComponentTypes;
import com.frostbyte.frostbite.component.ModeData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class ChaincicleItem extends Item {
    private final String[] modes = {"swipe", "spin", "hook", "grapple"};
    private boolean used = false;

    public ChaincicleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        checkComponentDataForNull(player.getItemInHand(interactionHand));

        int modeIndex = ArrayUtils.indexOf(modes, player.getItemInHand(interactionHand).get(ModDataComponentTypes.MODE.get()).mode());

        if (used) {
            if (player.isShiftKeyDown()) {
                modeIndex += 1;
                if (modeIndex == modes.length) modeIndex = 0;
                player.getItemInHand(interactionHand).set(ModDataComponentTypes.MODE.get(),
                        new ModeData(modes[modeIndex]));
            } else {
                switch (player.getItemInHand(interactionHand).get(ModDataComponentTypes.MODE.get()).mode()) {
                    case "swipe":
                        swipeOption(player);
                        break;
                    case "spin":
                        spinOption(player);
                        break;
                    case "hook":
                        hookOption(player);
                        break;
                    case "grapple":
                        grappleOption(player);
                        break;
                }
            }
        }
        used = !used;
        return InteractionResult.SUCCESS;
    }


    public void swipeOption(Player player) {
        player.displayClientMessage(Component.literal("Slashing through enemies like butter"), false);
    }

    public void spinOption(Player player) {
        player.displayClientMessage(Component.literal("BEYBLADE BEYBLADE LET IT RIP"), false);
    }

    public void hookOption(Player player) {
        player.displayClientMessage(Component.literal("Looks like the mob it hooked (lol)"), false);
    }

    public void grappleOption(Player player) {
        player.displayClientMessage(Component.literal("Groping, I mean, grappling"), false);
    }

    public void checkComponentDataForNull(ItemStack stack) {
        if (!stack.has(ModDataComponentTypes.MODE)) {
            stack.set(ModDataComponentTypes.MODE.get(), new ModeData("swipe"));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        checkComponentDataForNull(stack);
        tooltipComponents.add(Component.literal(String.format("Mode: %s",
                stack.get(ModDataComponentTypes.MODE.get()).mode())).withStyle(ChatFormatting.GRAY));
    }
}
