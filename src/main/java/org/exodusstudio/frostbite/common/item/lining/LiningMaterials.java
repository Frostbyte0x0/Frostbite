package org.exodusstudio.frostbite.common.item.lining;

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
    LiningMaterial WOOLLY_WOOL = new LiningMaterial(15, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.BOOTS, 1);
        map.put(ArmorType.LEGGINGS, 4);
        map.put(ArmorType.CHESTPLATE, 5);
        map.put(ArmorType.HELMET, 2);
        map.put(ArmorType.BODY, 4);
    }), 12, SoundEvents.ARMOR_EQUIP_LEATHER, ItemTags.REPAIRS_CHAIN_ARMOR, EquipmentAssets.CHAINMAIL);
    LiningMaterial FROZEN_FUR = new LiningMaterial(15, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.BOOTS, 2);
        map.put(ArmorType.LEGGINGS, 5);
        map.put(ArmorType.CHESTPLATE, 6);
        map.put(ArmorType.HELMET, 2);
        map.put(ArmorType.BODY, 5);
    }), 9, SoundEvents.ARMOR_EQUIP_LEATHER, ItemTags.REPAIRS_IRON_ARMOR, EquipmentAssets.IRON);
    LiningMaterial INSULATED_CLOTH = new LiningMaterial(7, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.BOOTS, 1);
        map.put(ArmorType.LEGGINGS, 3);
        map.put(ArmorType.CHESTPLATE, 5);
        map.put(ArmorType.HELMET, 2);
        map.put(ArmorType.BODY, 7);
    }), 25, SoundEvents.ARMOR_EQUIP_LEATHER, ItemTags.REPAIRS_GOLD_ARMOR, EquipmentAssets.GOLD);
    LiningMaterial HEATED_COATING = new LiningMaterial(33, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.BOOTS, 3);
        map.put(ArmorType.LEGGINGS, 6);
        map.put(ArmorType.CHESTPLATE, 8);
        map.put(ArmorType.HELMET, 3);
        map.put(ArmorType.BODY, 11);
    }), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, ItemTags.REPAIRS_DIAMOND_ARMOR, EquipmentAssets.DIAMOND);
    LiningMaterial FROZEN_PLATING = new LiningMaterial(37, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.BOOTS, 3);
        map.put(ArmorType.LEGGINGS, 6);
        map.put(ArmorType.CHESTPLATE, 8);
        map.put(ArmorType.HELMET, 3);
        map.put(ArmorType.BODY, 11);
    }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, ItemTags.REPAIRS_NETHERITE_ARMOR, EquipmentAssets.NETHERITE);

}
