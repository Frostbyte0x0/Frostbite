package org.exodusstudio.frostbite.common.inventory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.item.lining.LiningItem;

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
        return Frostbite.shouldShowLining && stack.canEquip(this.slot, this.owner) && stack.getItem() instanceof LiningItem;
    }

    public boolean mayPickup(Player player) {
        ItemStack itemstack = this.getItem();
        return Frostbite.shouldShowLining && !itemstack.isEmpty() && itemstack.getItem() instanceof LiningItem;
    }

    @Nullable
    public ResourceLocation getNoItemIcon() {
        return this.emptyIcon;
    }

    public boolean isActive() {
        return Frostbite.shouldShowLining;
    }

    public EquipmentSlot getSlot() {
        return slot;
    }
}
