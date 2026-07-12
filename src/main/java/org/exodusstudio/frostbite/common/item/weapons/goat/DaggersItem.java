package org.exodusstudio.frostbite.common.item.weapons.goat;

import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;

public class DaggersItem extends ComboWeapon {
    public DaggersItem(Properties properties) {
        super(properties,
                new ComboStep(3, 0.75f, 1.75f, 3),
                new ComboStep(4, 0.75f, 1.75f, 3),
                new ComboStep(5, 0.75f, 1.75f, 3));
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 3.0,
                        AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build();
    }
}