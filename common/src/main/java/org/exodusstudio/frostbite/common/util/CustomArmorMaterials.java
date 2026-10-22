package org.exodusstudio.frostbite.common.util;

import net.minecraft.util.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.EnumMap;

import static net.minecraft.world.item.equipment.EquipmentAssets.createId;

public class CustomArmorMaterials {
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
