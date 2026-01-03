package org.exodusstudio.frostbite.common.item.weapons.elf;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.function.Consumer;

public abstract class ModeWeapon extends Item {
    protected final String[] modes;
    public String mode;
    protected static RandomSource random = RandomSource.create();
    private final ChatFormatting primaryColour;
    private final ChatFormatting secondaryColour;

    public ModeWeapon(Properties properties, String[] modes, ChatFormatting primaryColour, ChatFormatting secondaryColour) {
        super(properties);
        this.modes = modes;
        this.mode = modes[0];
        this.primaryColour = primaryColour;
        this.secondaryColour = secondaryColour;
    }

    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        if (level.isClientSide()) {
            mode = modes[(Arrays.asList(modes).indexOf(mode) + 1) % modes.length];
        }
        return InteractionResult.SUCCESS;
    }

    public void attack(Level level, LivingEntity owner) {
    }

    public void setFirstMode() {
        this.mode = modes[0];
    }

    public void setSecondMode() {
        this.mode = modes[1];
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.frostbite.mode_weapon.mode").withStyle(primaryColour)
                .append(Component.literal(mode).withStyle(secondaryColour)));
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }
}
