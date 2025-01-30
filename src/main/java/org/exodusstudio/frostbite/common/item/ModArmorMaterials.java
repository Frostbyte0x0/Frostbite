package org.exodusstudio.frostbite.common.item;


import net.minecraft.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.EnumMap;

import static net.minecraft.world.item.equipment.EquipmentAssets.createId;

public class ModArmorMaterials {
    public static final ArmorMaterial FUR =
            new ArmorMaterial(5,
                    Util.make(new EnumMap<>(ArmorType.class), map -> {
                        map.put(ArmorType.BOOTS, 1);
                        map.put(ArmorType.LEGGINGS, 2);
                        map.put(ArmorType.CHESTPLATE, 3);
                        map.put(ArmorType.HELMET, 1);
                        map.put(ArmorType.BODY, 3);
                    }), 15, SoundEvents.ARMOR_EQUIP_LEATHER,0f, 0f,
                    ItemTags.WOOL,
                    createId("fur_armor"));
}
