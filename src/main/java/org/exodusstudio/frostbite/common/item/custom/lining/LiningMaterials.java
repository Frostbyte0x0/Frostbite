package org.exodusstudio.frostbite.common.item.custom.lining;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.EnumMap;

public interface LiningMaterials {
    // Wool
    // - Sheep
    // Woolly Wool
    // - Woolly Sheep
    // Fur
    // - Wild Wolf
    // Frozen Fur
    // - Frozen Wild Wolf
    // Insulated Cloth
    // - Need 4 jelly from Levitating Jellyfish transformed into insulating jelly
    // - Combine with woven wool to make Insulated Cloth
    // Enchantments:
    // Freezing protection I-IV, take less damage from cold attacks
    // Insulation I-IV, better cold resistance
    // Softness,

    LiningMaterial WOOL = new LiningMaterial(5, Util.make(new EnumMap<>(ArmorType.class), (map) -> {
        map.put(ArmorType.BOOTS, 1);
        map.put(ArmorType.LEGGINGS, 2);
        map.put(ArmorType.CHESTPLATE, 3);
        map.put(ArmorType.HELMET, 1);
        map.put(ArmorType.BODY, 3);
    }), 15, SoundEvents.ARMOR_EQUIP_LEATHER, ItemTags.REPAIRS_LEATHER_ARMOR, EquipmentAssets.LEATHER);
}
