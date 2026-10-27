package org.exodusstudio.frostbite.common.item.weapons.elf;

import net.minecraft.ChatFormatting;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.component.ModeData;
import org.exodusstudio.frostbite.common.item.weapons.SpellTooltipable;
import org.exodusstudio.frostbite.common.mixinterfaces.TE;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;

import java.util.Arrays;

public abstract class ModeWeapon extends Item implements SpellTooltipable {
    public static final String STAFF_USES_KEY = "staff_uses";
    public static final int USES_PER_DUST = 6;
    public static final int MAX_USES = 24;
    public static final float INNER_TEMPERATURE_COST = 1.0f;

    protected static RandomSource random = RandomSource.create();

    public ModeWeapon(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand interactionHand) {
        return InteractionResult.PASS;
    }

    public void cycleMode(ItemStack stack) {
        setMode(stack, (Arrays.asList(getModes()).indexOf(getMode(stack)) + 1) % getModes().length);
    }

    public final boolean attack(Level level, LivingEntity owner) {
        ItemStack stack = owner.getItemInHand(InteractionHand.MAIN_HAND);
        if (!consumeUse(stack)) return false;

        if (!level.isClientSide()) {
            ((TE) owner).decreaseTemperature(INNER_TEMPERATURE_COST, true);
        }

        performAttack(level, owner);
        return true;
    }

    protected abstract void performAttack(Level level, LivingEntity owner);

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack staff, ItemStack other, Slot slot,
                                            ClickAction action, Player player, SlotAccess access) {
        if (action != ClickAction.SECONDARY || other.isEmpty()) return false;

        int previousUses = getUses(staff);
        if (!loadDust(staff, other)) return false;
        if (!access.set(other.copyWithCount(other.getCount() - 1))) {
            setUses(staff, previousUses);
            return false;
        }

        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
        return true;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack staff, Slot slot, ClickAction action, Player player) {
        if (action != ClickAction.SECONDARY || !slot.allowModification(player)) return false;

        ItemStack dust = slot.getItem();
        int previousUses = getUses(staff);
        if (!loadDust(staff, dust)) return false;
        if (slot.remove(1).isEmpty()) {
            setUses(staff, previousUses);
            return false;
        }

        slot.setChanged();
        player.getInventory().setChanged();
        player.containerMenu.broadcastChanges();
        return true;
    }

    public static int getUses(ItemStack stack) {
        return Math.min(MAX_USES, Math.max(0, DataHelper.getInt(stack, STAFF_USES_KEY)));
    }

    public static void setUses(ItemStack stack, int uses) {
        DataHelper.setData(stack, STAFF_USES_KEY, Math.clamp(uses, 0, MAX_USES));
    }

    public static void loadToMax(ItemStack stack) {
        setUses(stack, MAX_USES);
    }

    private static boolean loadDust(ItemStack staff, ItemStack dust) {
        if (!dust.is(ItemRegistry.MAGIC_DUST.get())) return false;

        int uses = getUses(staff);
        if (uses >= MAX_USES) return false;
        setUses(staff, Math.min(MAX_USES, uses + USES_PER_DUST));
        return true;
    }

    private boolean consumeUse(ItemStack stack) {
        int uses = getUses(stack);
        if (uses <= 0) return false;
        setUses(stack, uses - 1);
        return true;
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
