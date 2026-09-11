package org.exodusstudio.frostbite.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.component.ArmourStatsData;
import org.exodusstudio.frostbite.common.item.armour.ArmourSet;
import org.exodusstudio.frostbite.common.item.armour.ArmourSets;
import org.exodusstudio.frostbite.common.mixinterfaces.InventoryWrapper;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Inventory.class)
public class InventoryMixin implements InventoryWrapper {
    @Unique
    private Inventory frostbite$inventory;
    @Unique
    private EntityEquipment frostbite$lining;
    @Unique
    private Int2ObjectMap<EquipmentSlot> EQUIPMENT_SLOT_MAPPING = new Int2ObjectArrayMap<>(Map.of(
            43, EquipmentSlot.FEET,
            44, EquipmentSlot.LEGS,
            45, EquipmentSlot.CHEST,
            46, EquipmentSlot.HEAD));

    @Override
    public EntityEquipment frostbite$getEquipment() {
        return frostbite$lining;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void constructor(Player player, EntityEquipment equipment, CallbackInfo ci) {
        frostbite$lining = new EntityEquipment();
        frostbite$inventory = (Inventory) ((Object) this);
    }

    @Inject(at = @At("HEAD"), method = "removeItem(II)Lnet/minecraft/world/item/ItemStack;", cancellable = true)
    public void removeItem(int slot, int count, CallbackInfoReturnable<ItemStack> cir) {
        EquipmentSlot equipmentslot = EQUIPMENT_SLOT_MAPPING.get(slot);
        if (equipmentslot != null) {
            ItemStack itemstack = frostbite$lining.get(equipmentslot);
            if (!itemstack.isEmpty()) {
                cir.setReturnValue(itemstack.split(count));
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "removeItem(Lnet/minecraft/world/item/ItemStack;)V", cancellable = true)
    public void removeItem(ItemStack itemStack, CallbackInfo ci) {
        for (EquipmentSlot equipmentslot : EQUIPMENT_SLOT_MAPPING.values()) {
            ItemStack itemstack = frostbite$lining.get(equipmentslot);
            if (itemstack == itemStack) {
                frostbite$lining.set(equipmentslot, ItemStack.EMPTY);
                ci.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "removeItemNoUpdate", cancellable = true)
    public void removeItemNoUpdate(int slot, CallbackInfoReturnable<ItemStack> cir) {
        EquipmentSlot equipmentslot = EQUIPMENT_SLOT_MAPPING.get(slot);
        if (equipmentslot != null) {
            cir.setReturnValue(frostbite$lining.set(equipmentslot, ItemStack.EMPTY));
        }
    }

    @Inject(at = @At("HEAD"), method = "setItem")
    public void setItem(int slot, ItemStack itemStack, CallbackInfo ci) {
        EquipmentSlot equipmentslot = EQUIPMENT_SLOT_MAPPING.get(slot);
        if (equipmentslot != null) {
            frostbite$lining.set(equipmentslot, itemStack);
        }
    }

    @Inject(at = @At("TAIL"), method = "setItem")
    public void setItemTail(int slot, ItemStack itemStack, CallbackInfo ci) {
        EquipmentSlot equipmentSlot = Inventory.EQUIPMENT_SLOT_MAPPING.get(slot);
        if (equipmentSlot != null &&
                equipmentSlot != EquipmentSlot.OFFHAND &&
                equipmentSlot != EquipmentSlot.SADDLE &&
                equipmentSlot != EquipmentSlot.BODY) {
            ArmourStatsData.tryRemoveStatBonuses(frostbite$inventory.player, equipmentSlot.getName());

            frostbite$applyFullSet(frostbite$inventory);
            if (itemStack.has(DataComponents.EQUIPPABLE)) {
                String equipmentName = itemStack.get(DataComponents.EQUIPPABLE).slot().getName().toLowerCase();
                if (itemStack.has(DataComponentTypeRegistry.ARMOUR_STATS)) {
                    ArmourStatsData.addStatBonuses(frostbite$inventory.player, equipmentName, itemStack.get(DataComponentTypeRegistry.ARMOUR_STATS));
                }
            }
        }
    }

    @Unique
    private static void frostbite$applyFullSet(Inventory inventory) {
        ArmourSet set = ArmourSet.getFullSet(inventory.equipment);
        if (set != null) {
            set.addSetBonuses(inventory.player.getAttributes());
            DataHelper.setData(inventory.player, "current_set", set.id());
        } else {
            ArmourSet wornSet = ArmourSets.SETS.get(DataHelper.getString(inventory.player, "current_set"));
            if (wornSet != null) {
                DataHelper.setData(inventory.player, "current_set", "");
                wornSet.removeSetBonuses(inventory.player.getAttributes());
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "isEmpty", cancellable = true)
    public void isEmpty(CallbackInfoReturnable<Boolean> cir) {
        for (EquipmentSlot equipmentslot : EQUIPMENT_SLOT_MAPPING.values()) {
            if (!frostbite$lining.get(equipmentslot).isEmpty()) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getItem", cancellable = true)
    public void getItem(int slot, CallbackInfoReturnable<ItemStack> cir) {
        EquipmentSlot equipmentslot = EQUIPMENT_SLOT_MAPPING.get(slot);
        if (equipmentslot != null) {
            cir.setReturnValue(frostbite$lining.get(equipmentslot));
        }
    }

    @Inject(at = @At("HEAD"), method = "dropAll")
    public void dropAll(CallbackInfo ci) {
        frostbite$lining.dropAll(frostbite$inventory.player);
    }

    @Inject(at = @At("HEAD"), method = "clearContent")
    public void clearContent(CallbackInfo ci) {
        frostbite$lining.clear();
    }
}
