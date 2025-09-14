package org.exodusstudio.frostbite.common.item.lining;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.EnumMap;

public interface LiningMaterials {
    LiningMaterial WOOL = new LiningMaterial(5, Util.make(new EnumMap<>(ArmorType.class), (map) -> {
        map.put(ArmorType.BOOTS,      1);
        map.put(ArmorType.LEGGINGS,   1);
        map.put(ArmorType.CHESTPLATE, 1);
        map.put(ArmorType.HELMET,     1);
        map.put(ArmorType.BODY,       4);
    }), 15, SoundEvents.ARMOR_EQUIP_LEATHER, ItemTags.REPAIRS_LEATHER_ARMOR, EquipmentAssets.LEATHER);
    LiningMaterial WOOLLY_WOOL = new LiningMaterial(10, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.BOOTS,      2);
        map.put(ArmorType.LEGGINGS,   2);
        map.put(ArmorType.CHESTPLATE, 2);
        map.put(ArmorType.HELMET,     2);
        map.put(ArmorType.BODY,       8);
    }), 12, SoundEvents.ARMOR_EQUIP_LEATHER, ItemTags.REPAIRS_CHAIN_ARMOR, EquipmentAssets.CHAINMAIL);
    LiningMaterial FROZEN_FUR = new LiningMaterial(15, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.BOOTS,      3);
        map.put(ArmorType.LEGGINGS,   3);
        map.put(ArmorType.CHESTPLATE, 3);
        map.put(ArmorType.HELMET,     3);
        map.put(ArmorType.BODY,      12);
    }), 9, SoundEvents.ARMOR_EQUIP_LEATHER, ItemTags.REPAIRS_IRON_ARMOR, EquipmentAssets.IRON);
    LiningMaterial INSULATED_CLOTH = new LiningMaterial(20, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.BOOTS,      4);
        map.put(ArmorType.LEGGINGS,   4);
        map.put(ArmorType.CHESTPLATE, 4);
        map.put(ArmorType.HELMET,     4);
        map.put(ArmorType.BODY,      16);
    }), 25, SoundEvents.ARMOR_EQUIP_LEATHER, ItemTags.REPAIRS_GOLD_ARMOR, EquipmentAssets.GOLD);
    LiningMaterial HEATED_COATING = new LiningMaterial(25, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.BOOTS,      5);
        map.put(ArmorType.LEGGINGS,   5);
        map.put(ArmorType.CHESTPLATE, 5);
        map.put(ArmorType.HELMET,     5);
        map.put(ArmorType.BODY,      20);
    }), 10, SoundEvents.ARMOR_EQUIP_DIAMOND, ItemTags.REPAIRS_DIAMOND_ARMOR, EquipmentAssets.DIAMOND);
    LiningMaterial FROZEN_PLATING = new LiningMaterial(30, Util.make(new EnumMap<>(ArmorType.class), map -> {
        map.put(ArmorType.BOOTS,      6);
        map.put(ArmorType.LEGGINGS,   6);
        map.put(ArmorType.CHESTPLATE, 6);
        map.put(ArmorType.HELMET,     6);
        map.put(ArmorType.BODY,      24);
    }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, ItemTags.REPAIRS_NETHERITE_ARMOR, EquipmentAssets.NETHERITE);

}
