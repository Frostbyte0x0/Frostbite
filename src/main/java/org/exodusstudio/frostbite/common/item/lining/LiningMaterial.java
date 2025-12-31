package org.exodusstudio.frostbite.common.item.lining;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import org.exodusstudio.frostbite.common.registry.AttributeRegistry;

import java.util.Map;

public record LiningMaterial(int durability, Map<ArmorType, Integer> coldProtection, int enchantmentValue, Holder<SoundEvent> equipSound, TagKey<Item> repairIngredient, ResourceKey<EquipmentAsset> assetId) {
    public Item.Properties humanoidProperties(Item.Properties properties, ArmorType armorType) {
        return properties.durability(armorType.getDurability(this.durability)).attributes(this.createAttributes(armorType)).enchantable(this.enchantmentValue).component(DataComponents.EQUIPPABLE, Equippable.builder(armorType.getSlot()).setEquipSound(this.equipSound).setAsset(this.assetId).build()).repairable(this.repairIngredient);
    }

    private ItemAttributeModifiers createAttributes(ArmorType armorType) {
        int i = this.coldProtection.getOrDefault(armorType, 0);
        ItemAttributeModifiers.Builder itemattributemodifiers$builder = ItemAttributeModifiers.builder();
        EquipmentSlotGroup equipmentslotgroup = EquipmentSlotGroup.bySlot(armorType.getSlot());
        Identifier identifier = Identifier.withDefaultNamespace("armor." + armorType.getName());
        itemattributemodifiers$builder.add(AttributeRegistry.COLD_PROTECTION, new AttributeModifier(identifier, i, AttributeModifier.Operation.ADD_VALUE), equipmentslotgroup);

        return itemattributemodifiers$builder.build();
    }
}
