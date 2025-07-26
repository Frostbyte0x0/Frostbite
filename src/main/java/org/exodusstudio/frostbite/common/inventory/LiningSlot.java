package org.exodusstudio.frostbite.common.inventory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.exodusstudio.frostbite.Frostbite;

import javax.annotation.Nullable;

public class LiningSlot extends Slot {
    private final LivingEntity owner;
    private final EquipmentSlot slot;
    @Nullable
    private final ResourceLocation emptyIcon;

    public LiningSlot(Container container, LivingEntity owner, EquipmentSlot slot, int slotIndex, int x, int y, @Nullable ResourceLocation emptyIcon) {
        super(container, slotIndex, x, y);
        this.owner = owner;
        this.slot = slot;
        this.emptyIcon = emptyIcon;
    }

    public void setByPlayer(ItemStack newStack, ItemStack oldStack) {
        this.owner.onEquipItem(this.slot, oldStack, newStack);
        this.set(newStack);
    }

    public int getMaxStackSize() {
        return 1;
    }

    public boolean mayPlace(ItemStack stack) {
        return Frostbite.shouldShowLining && stack.canEquip(this.slot, this.owner);
    }

    public boolean mayPickup(Player p_345575_) {
        ItemStack itemstack = this.getItem();
        return Frostbite.shouldShowLining && (itemstack.isEmpty() || p_345575_.isCreative() || !EnchantmentHelper.has(itemstack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) && super.mayPickup(p_345575_);
    }

    @Nullable
    public ResourceLocation getNoItemIcon() {
        return this.emptyIcon;
    }

    public boolean isActive() {
        return Frostbite.shouldShowLining;
    }

    public ItemStack getItem() {
        return Frostbite.savedLinings.getSpecificLiningForPlayer(this.owner.getStringUUID(), slot);
    }

    public void set(ItemStack stack) {
        Frostbite.savedLinings.setSpecificLiningForPlayer(this.owner.getStringUUID(), slot, stack);
        this.setChanged();
    }
}
