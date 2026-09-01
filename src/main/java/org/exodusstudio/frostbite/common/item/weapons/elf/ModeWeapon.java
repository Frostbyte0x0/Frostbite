package org.exodusstudio.frostbite.common.item.weapons.elf;

import net.minecraft.ChatFormatting;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.component.ModeData;
import org.exodusstudio.frostbite.common.item.weapons.SpellTooltipable;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;

import java.util.Arrays;

public abstract class ModeWeapon extends Item implements SpellTooltipable {
    protected static RandomSource random = RandomSource.create();

    public ModeWeapon(Properties properties) {
        super(properties);
    }

    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() instanceof ModeWeapon)
            setMode(stack, (Arrays.asList(getModes()).indexOf(getMode(stack)) + 1) % getModes().length);

        return InteractionResult.SUCCESS;
    }

    public void attack(Level level, LivingEntity owner) {
    }

    @Override
    public abstract String[] getModes();

    public String getMode(ItemStack stack) {
        return stack.get(DataComponentTypeRegistry.MODE).mode();
    }

    public String getMode(int index) {
        return getModes()[index];
    }

    public void setMode(ItemStack stack, int index) {
        stack.set(DataComponentTypeRegistry.MODE.get(), new ModeData(getMode(index)));
    }

    public abstract ChatFormatting regularColour();

    public abstract ChatFormatting selectedColour();
}
