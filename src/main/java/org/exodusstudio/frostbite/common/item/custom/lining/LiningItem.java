package org.exodusstudio.frostbite.common.item.custom.lining;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

public class LiningItem extends Item {
    public LiningItem(LiningMaterial material, ArmorType armorType, Item.Properties properties) {
        super(material.humanoidProperties(properties, armorType));
    }
}
