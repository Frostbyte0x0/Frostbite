package org.exodusstudio.frostbite.common.item.weapons.goat;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;

public class DaggersItem extends ComboWeapon {
    public DaggersItem(Properties properties) {
        super(properties,
                new ComboStep(2, 0.75f, 0.15f, 0.025f),
                new ComboStep(3, 0.75f, 0.15f, 0.025f),
                new ComboStep(4, 0.75f, 0.15f, 0.025f));
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 2.0,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }
}