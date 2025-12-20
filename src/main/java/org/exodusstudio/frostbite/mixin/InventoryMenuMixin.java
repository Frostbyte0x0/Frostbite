package org.exodusstudio.frostbite.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import org.exodusstudio.frostbite.Frostbite;
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
    private static final Identifier EMPTY_ARMOR_SLOT_HELMET =
            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/gui/empty_helmet_slot.png");
    @Unique
    private static final Identifier EMPTY_ARMOR_SLOT_CHESTPLATE =
            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/gui/empty_chestplate_slot.png");
    @Unique
    private static final Identifier EMPTY_ARMOR_SLOT_LEGGINGS =
            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/gui/empty_leggings_slot.png");
    @Unique
    private static final Identifier EMPTY_ARMOR_SLOT_BOOTS =
            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/gui/empty_boots_slot.png");
    @Unique
    private static final Map<EquipmentSlot, Identifier> TEXTURE_EMPTY_SLOTS = Map.of(
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
    private void constructor(Inventory playerInventory, boolean active, final Player owner, CallbackInfo ci) {
        for (int i = 0; i < 4; i++) {
            EquipmentSlot equipmentslot = SLOT_IDS[i];
            Identifier Identifier = TEXTURE_EMPTY_SLOTS.get(equipmentslot);
            frostbite$inventoryMenu.addSlot(new LiningSlot(playerInventory, owner,
                    equipmentslot, 43 + i, 8, 8 + i * 18, Identifier));
        }
    }
}
