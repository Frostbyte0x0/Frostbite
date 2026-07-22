package org.exodusstudio.frostbite.common.item.lining;

import net.minecraft.core.component.DataComponents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;

import java.util.Map;

public class LiningItem extends Item {
    LiningMaterial material;
    ArmorType armorType;

    public LiningItem(LiningMaterial material, ArmorType armorType, Item.Properties properties) {
        super(material.humanoidProperties(properties, armorType));
        this.material = material;
        this.armorType = armorType;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable != null && equippable.swappable()) {
            return swapWithEquipmentSlot(stack, player);
        }
        return InteractionResult.PASS;
    }

    public InteractionResult swapWithEquipmentSlot(ItemStack inHand, Player player) {
        int slot = EQUIPMENT_SLOT_MAPPING.get(this.armorType.getSlot());
        ItemStack inEquipmentSlot = player.getInventory().getItem(slot);
        if ((!EnchantmentHelper.has(inEquipmentSlot, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) || player.isCreative()) &&
                !ItemStack.isSameItemSameComponents(inHand, inEquipmentSlot)) {
            if (!player.level().isClientSide()) {
                player.awardStat(Stats.ITEM_USED.get(inHand.getItem()));
            }

            if (inHand.getCount() <= 1) {
                ItemStack swappedToHand = inEquipmentSlot.isEmpty() ? inHand : inEquipmentSlot.copyAndClear();
                ItemStack swappedToEquipment = player.isCreative() ? inHand.copy() : inHand.copyAndClear();
                player.getInventory().setItem(slot, swappedToEquipment);
                return InteractionResult.SUCCESS.heldItemTransformedTo(swappedToHand);
            } else {
                ItemStack swappedToInventory = inEquipmentSlot.copyAndClear();
                ItemStack swappedToEquipment = inHand.consumeAndReturn(1, player);
                player.getInventory().setItem(slot, swappedToEquipment);
                if (!player.getInventory().add(swappedToInventory)) {
                    player.drop(swappedToInventory, false);
                }

                return InteractionResult.SUCCESS.heldItemTransformedTo(inHand);
            }
        }

        return InteractionResult.FAIL;
    }

    private Map<EquipmentSlot, Integer> EQUIPMENT_SLOT_MAPPING = Map.of(
            EquipmentSlot.FEET, 46,
            EquipmentSlot.LEGS, 45,
            EquipmentSlot.CHEST, 44,
            EquipmentSlot.HEAD, 43);

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot armorType, LivingEntity entity) {
        return entity.getEquipmentSlotForItem(stack) == armorType;
    }

    public int getLiningLevel() {
        return material.coldProtection().get(armorType);
    }
}
