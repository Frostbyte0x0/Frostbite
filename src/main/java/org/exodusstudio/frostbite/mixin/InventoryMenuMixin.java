package org.exodusstudio.frostbite.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import org.exodusstudio.frostbite.common.inventory.LiningSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(InventoryMenu.class)
public class InventoryMenuMixin {
    @Unique
    private InventoryMenu frostbite$inventoryMenu = (InventoryMenu) ((Object) this);
    @Unique
    private static final EquipmentSlot[] SLOT_IDS =
            new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    @Unique
    private static final ResourceLocation EMPTY_ARMOR_SLOT_HELMET =
            ResourceLocation.withDefaultNamespace("container/slot/helmet");
    @Unique
    private static final ResourceLocation EMPTY_ARMOR_SLOT_CHESTPLATE =
            ResourceLocation.withDefaultNamespace("container/slot/chestplate");
    @Unique
    private static final ResourceLocation EMPTY_ARMOR_SLOT_LEGGINGS =
            ResourceLocation.withDefaultNamespace("container/slot/leggings");
    @Unique
    private static final ResourceLocation EMPTY_ARMOR_SLOT_BOOTS =
            ResourceLocation.withDefaultNamespace("container/slot/boots");
    @Unique
    private static final Map<EquipmentSlot, ResourceLocation> TEXTURE_EMPTY_SLOTS = Map.of(
            EquipmentSlot.FEET,
            EMPTY_ARMOR_SLOT_BOOTS,
            EquipmentSlot.LEGS,
            EMPTY_ARMOR_SLOT_LEGGINGS,
            EquipmentSlot.CHEST,
            EMPTY_ARMOR_SLOT_CHESTPLATE,
            EquipmentSlot.HEAD,
            EMPTY_ARMOR_SLOT_HELMET
    );

    @Inject(method = "<init>", at = @At("TAIL"))
    private void doEntityOutline(Inventory playerInventory, boolean active, final Player owner, CallbackInfo ci) {
        for (int i = 0; i < 4; i++) {
            EquipmentSlot equipmentslot = SLOT_IDS[i];
            ResourceLocation resourcelocation = TEXTURE_EMPTY_SLOTS.get(equipmentslot);
            frostbite$inventoryMenu.addSlot(new LiningSlot(playerInventory, owner,
                    equipmentslot, 54 + i, 8, 8 + i * 18, resourcelocation));
        }
    }
}
