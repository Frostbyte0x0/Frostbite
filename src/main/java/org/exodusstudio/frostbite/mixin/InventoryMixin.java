package org.exodusstudio.frostbite.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.common.inventory.LiningSlot;
import org.exodusstudio.frostbite.common.util.InventoryWrapper;
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
    public ItemStack frostbite$getLining(EquipmentSlot slot) {
        return frostbite$lining.get(slot);
    }

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
    public void removeItem(int index, int count, CallbackInfoReturnable<ItemStack> cir) {
        EquipmentSlot equipmentslot = EQUIPMENT_SLOT_MAPPING.get(index);
        if (equipmentslot != null) {
            ItemStack itemstack = frostbite$lining.get(equipmentslot);
            if (!itemstack.isEmpty()) {
                cir.setReturnValue(itemstack.split(count));
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "removeItem(Lnet/minecraft/world/item/ItemStack;)V", cancellable = true)
    public void removeItem(ItemStack stack, CallbackInfo ci) {
        for (EquipmentSlot equipmentslot : EQUIPMENT_SLOT_MAPPING.values()) {
            ItemStack itemstack = frostbite$lining.get(equipmentslot);
            if (itemstack == stack) {
                frostbite$lining.set(equipmentslot, ItemStack.EMPTY);
                ci.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "removeItemNoUpdate", cancellable = true)
    public void removeItemNoUpdate(int index, CallbackInfoReturnable<ItemStack> cir) {
        EquipmentSlot equipmentslot = EQUIPMENT_SLOT_MAPPING.get(index);
        if (equipmentslot != null) {
            cir.setReturnValue(frostbite$lining.set(equipmentslot, ItemStack.EMPTY));
        }
    }

    @Inject(at = @At("HEAD"), method = "setItem")
    public void setItem(int index, ItemStack stack, CallbackInfo ci) {
        EquipmentSlot equipmentslot = EQUIPMENT_SLOT_MAPPING.get(index);
        if (equipmentslot != null) {
            frostbite$lining.set(equipmentslot, stack);
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
    public void getItem(int index, CallbackInfoReturnable<ItemStack> cir) {
        EquipmentSlot equipmentslot = EQUIPMENT_SLOT_MAPPING.get(index);
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
