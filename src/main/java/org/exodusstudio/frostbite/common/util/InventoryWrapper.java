package org.exodusstudio.frostbite.common.util;

import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public interface InventoryWrapper {
    ItemStack frostbite$getLining(EquipmentSlot slot);
    EntityEquipment frostbite$getEquipment();
}
