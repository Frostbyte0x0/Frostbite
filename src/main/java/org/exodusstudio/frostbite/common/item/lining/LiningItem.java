package org.exodusstudio.frostbite.common.item.lining;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;

public class LiningItem extends Item {
    LiningMaterial material;
    ArmorType armorType;

    public LiningItem(LiningMaterial material, ArmorType armorType, Item.Properties properties) {
        super(material.humanoidProperties(properties, armorType));
        this.material = material;
        this.armorType = armorType;
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
        return entity.getEquipmentSlotForItem(stack) == armorType;
    }

    public int getLiningLevel() {
        return material.coldProtection().get(armorType);
    }
}
