package org.exodusstudio.frostbite.common.item.custom.weapons;

import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.component.ModeData;
import org.exodusstudio.frostbite.common.registry.SoundRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
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
    private final String[] modes = {"swipe", "hook", "grapple"};
    private boolean used = false;

    public ChaincicleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        checkComponentDataForNull(player.getItemInHand(interactionHand));

        int modeIndex = ArrayUtils.indexOf(modes, player.getItemInHand(interactionHand).get(DataComponentTypeRegistry.MODE.get()).mode());

        if (used) {
            if (player.isShiftKeyDown()) {
                modeIndex += 1;
                if (modeIndex == modes.length) modeIndex = 0;
                player.getItemInHand(interactionHand).set(DataComponentTypeRegistry.MODE.get(),
                        new ModeData(modes[modeIndex]));
            } else {
                switch (player.getItemInHand(interactionHand).get(DataComponentTypeRegistry.MODE.get()).mode()) {
                    case "swipe":
                        swipeOption(player);
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
        player.level().playSound(null, player.blockPosition(), SoundRegistry.CHAINCICLE_SWIPE.get(), SoundSource.PLAYERS, 1f, 1f);
    }

    public void hookOption(Player player) {
        player.displayClientMessage(Component.literal("Looks like the mob is hooked (lol)"), false);
        player.level().playSound(null, player.blockPosition(), SoundRegistry.CHAINCICLE_HOOK_LAUNCH.get(), SoundSource.PLAYERS, 1f, 1f);
    }

    public void grappleOption(Player player) {
        player.displayClientMessage(Component.literal("Groping, I mean, grappling"), false);
        player.level().playSound(null, player.blockPosition(), SoundRegistry.CHAINCICLE_GRAPPLE_LAUNCH.get(), SoundSource.PLAYERS, 1f, 1f);
    }

    public void checkComponentDataForNull(ItemStack stack) {
        if (!stack.has(DataComponentTypeRegistry.MODE)) {
            stack.set(DataComponentTypeRegistry.MODE.get(), new ModeData("swipe"));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        checkComponentDataForNull(stack);
        tooltipComponents.add(Component.literal(String.format("Mode: %s",
                stack.get(DataComponentTypeRegistry.MODE.get()).mode())).withStyle(ChatFormatting.GRAY));
    }
}
