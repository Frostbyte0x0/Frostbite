package org.exodusstudio.frostbite.common.item;

import net.minecraft.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.EnumMap;

import static net.minecraft.world.item.equipment.EquipmentAssets.createId;

public class ModArmorMaterials {
    public static final ArmorMaterial BLACK_ICE =
            new ArmorMaterial(40,
                    Util.make(new EnumMap<>(ArmorType.class), map -> {
                        map.put(ArmorType.BOOTS, 4);
                        map.put(ArmorType.LEGGINGS, 7);
                        map.put(ArmorType.CHESTPLATE, 9);
                        map.put(ArmorType.HELMET, 4);
                        map.put(ArmorType.BODY, 12);
                    }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE,3.0F, 0.1F,
                    ItemTags.WOOL,
                    createId("black_ice_armor"));

    public static final ArmorMaterial FROSTBITTEN =
            new ArmorMaterial(40,
                    Util.make(new EnumMap<>(ArmorType.class), map -> {
                        map.put(ArmorType.BOOTS, 4);
                        map.put(ArmorType.LEGGINGS, 7);
                        map.put(ArmorType.CHESTPLATE, 9);
                        map.put(ArmorType.HELMET, 4);
                        map.put(ArmorType.BODY, 12);
                    }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE,3.0F, 0.1F,
                    ItemTags.WOOL,
                    createId("frostbitten_armor"));
}
