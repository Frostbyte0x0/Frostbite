package org.exodusstudio.frostbite.client.gui;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.component.ArmourSetData;
import org.exodusstudio.frostbite.common.component.ArmourStatsData;
import org.exodusstudio.frostbite.common.item.armour.ArmourCleanliness;
import org.exodusstudio.frostbite.common.item.armour.ArmourSet;
import org.exodusstudio.frostbite.common.item.armour.ArmourSets;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.registry.MenuTypeRegistry;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class WeavingMenu extends ItemCombinerMenu {
    static Item[] liningItems = new Item[] {
            ItemRegistry.WOOLLY_WOOL.asItem(),
            ItemRegistry.FROZEN_FUR.asItem(),
            ItemRegistry.INSULATED_CLOTH.asItem(),
            ItemRegistry.HEATED_COATING.asItem(),
            ItemRegistry.FROZEN_PLATING.asItem(),
    };
    static Map<Item, String> weavingPatterns = Map.of(
            ItemRegistry.HELMET_WEAVING_PATTERN.asItem(), "helmet",
            ItemRegistry.CHESTPLATE_WEAVING_PATTERN.asItem(), "chestplate",
            ItemRegistry.LEGGINGS_WEAVING_PATTERN.asItem(), "leggings",
            ItemRegistry.BOOTS_WEAVING_PATTERN.asItem(),  "boots"
    );

    public static HashMap<Item, Item> cutouts = new HashMap<>() {{
        put(ItemRegistry.HELMET_WEAVING_PATTERN.asItem(),     ItemRegistry.HELMET_CUTOUT.asItem());
        put(ItemRegistry.CHESTPLATE_WEAVING_PATTERN.asItem(), ItemRegistry.CHESTPLATE_CUTOUT.asItem());
        put(ItemRegistry.LEGGINGS_WEAVING_PATTERN.asItem(),   ItemRegistry.LEGGINGS_CUTOUT.asItem());
        put(ItemRegistry.BOOTS_WEAVING_PATTERN.asItem(),      ItemRegistry.BOOTS_CUTOUT.asItem());
    }};

    public static HashMap<Item, HashMap<Item, Item>> linings = new HashMap<>() {{
        put(Items.WOOL.white().asItem(), new HashMap<>() {{
            put(ItemRegistry.HELMET_CUTOUT.asItem(),     ItemRegistry.WOOL_LINING_HELMET.asItem());
            put(ItemRegistry.CHESTPLATE_CUTOUT.asItem(), ItemRegistry.WOOL_LINING_CHESTPLATE.asItem());
            put(ItemRegistry.LEGGINGS_CUTOUT.asItem(),   ItemRegistry.WOOL_LINING_LEGGINGS.asItem());
            put(ItemRegistry.BOOTS_CUTOUT.asItem(),      ItemRegistry.WOOL_LINING_BOOTS.asItem());
        }});
        put(ItemRegistry.WOOLLY_WOOL.asItem(), new HashMap<>() {{
            put(ItemRegistry.HELMET_CUTOUT.asItem(),     ItemRegistry.WOOLLY_WOOL_LINING_HELMET.asItem());
            put(ItemRegistry.CHESTPLATE_CUTOUT.asItem(), ItemRegistry.WOOLLY_WOOL_LINING_CHESTPLATE.asItem());
            put(ItemRegistry.LEGGINGS_CUTOUT.asItem(),   ItemRegistry.WOOLLY_WOOL_LINING_LEGGINGS.asItem());
            put(ItemRegistry.BOOTS_CUTOUT.asItem(),      ItemRegistry.WOOLLY_WOOL_LINING_BOOTS.asItem());
        }});
        put(ItemRegistry.FROZEN_FUR.asItem(), new HashMap<>() {{
            put(ItemRegistry.HELMET_CUTOUT.asItem(),     ItemRegistry.FROZEN_FUR_LINING_HELMET.asItem());
            put(ItemRegistry.CHESTPLATE_CUTOUT.asItem(), ItemRegistry.FROZEN_FUR_LINING_CHESTPLATE.asItem());
            put(ItemRegistry.LEGGINGS_CUTOUT.asItem(),   ItemRegistry.FROZEN_FUR_LINING_LEGGINGS.asItem());
            put(ItemRegistry.BOOTS_CUTOUT.asItem(),      ItemRegistry.FROZEN_FUR_LINING_BOOTS.asItem());
        }});
        put(ItemRegistry.INSULATED_CLOTH.asItem(), new HashMap<>() {{
            put(ItemRegistry.HELMET_CUTOUT.asItem(),     ItemRegistry.INSULATED_CLOTH_LINING_HELMET.asItem());
            put(ItemRegistry.CHESTPLATE_CUTOUT.asItem(), ItemRegistry.INSULATED_CLOTH_LINING_CHESTPLATE.asItem());
            put(ItemRegistry.LEGGINGS_CUTOUT.asItem(),   ItemRegistry.INSULATED_CLOTH_LINING_LEGGINGS.asItem());
            put(ItemRegistry.BOOTS_CUTOUT.asItem(),      ItemRegistry.INSULATED_CLOTH_LINING_BOOTS.asItem());
        }});
        put(ItemRegistry.HEATED_COATING.asItem(), new HashMap<>() {{
            put(ItemRegistry.HELMET_CUTOUT.asItem(),     ItemRegistry.HEATED_COATING_LINING_HELMET.asItem());
            put(ItemRegistry.CHESTPLATE_CUTOUT.asItem(), ItemRegistry.HEATED_COATING_LINING_CHESTPLATE.asItem());
            put(ItemRegistry.LEGGINGS_CUTOUT.asItem(),   ItemRegistry.HEATED_COATING_LINING_LEGGINGS.asItem());
            put(ItemRegistry.BOOTS_CUTOUT.asItem(),      ItemRegistry.HEATED_COATING_LINING_BOOTS.asItem());
        }});
        put(ItemRegistry.FROZEN_PLATING.asItem(), new HashMap<>() {{
            put(ItemRegistry.HELMET_CUTOUT.asItem(),     ItemRegistry.FROZEN_PLATING_LINING_HELMET.asItem());
            put(ItemRegistry.CHESTPLATE_CUTOUT.asItem(), ItemRegistry.FROZEN_PLATING_LINING_CHESTPLATE.asItem());
            put(ItemRegistry.LEGGINGS_CUTOUT.asItem(),   ItemRegistry.FROZEN_PLATING_LINING_LEGGINGS.asItem());
            put(ItemRegistry.BOOTS_CUTOUT.asItem(),      ItemRegistry.FROZEN_PLATING_LINING_BOOTS.asItem());
        }});
    }};

    public static boolean isLiningMaterial(ItemStack item) {
        return Arrays.asList(liningItems).contains(item.getItem()) || item.is(ItemTags.WOOL);
    }

    public static boolean isWeavingPattern(Item item) {
        return weavingPatterns.containsKey(item);
    }

    public static boolean isCutout(Item item) {
        return cutouts.containsValue(item);
    }

    public WeavingMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public WeavingMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(MenuTypeRegistry.WEAVING_MENU.get(), containerId, playerInventory, access, createInputSlotDefinitions());
    }

    private static ItemCombinerMenuSlotDefinition createInputSlotDefinitions() {
        return ItemCombinerMenuSlotDefinition.create()
                .withSlot(0, 27, 47, _ -> true)
                .withSlot(1, 76, 47, _ -> true)
                .withResultSlot(2, 134, 47)
                .build();
    }

    protected boolean isValidBlock(BlockState state) {
        return state.is(BlockTags.ANVIL);
    }

    protected boolean mayPickup(Player player, boolean p_39024_) {
        return true;
    }

    protected void onTake(Player player, ItemStack stack) {
        resultSlots.setItem(0, ItemStack.EMPTY);
        inputSlots.getItem(0).shrink(1);
        inputSlots.getItem(1).setDamageValue(inputSlots.getItem(1).getDamageValue() + 1);
        if (inputSlots.getItem(1).isBroken()) inputSlots.getItem(1).shrink(1);
        player.level().playLocalSound(
                player.getX(), player.getY(), player.getZ(), SoundEvents.SHEARS_SNIP, SoundSource.BLOCKS,
                1.0F, 0.8F + 0.4F * player.getRandom().nextFloat(), false
        );

        for (String equipment : weavingPatterns.values()) {
            DataHelper.setData(player, "craft_stats_" + equipment, "");
        }
    }

    public void createResult() {
        Item slot1 = inputSlots.getItem(0).getItem();
        Item slot2 = inputSlots.getItem(1).getItem();
        if (isLiningMaterial(inputSlots.getItem(0)) && isCutout(slot2)) {
            if (inputSlots.getItem(0).is(ItemTags.WOOL)) {
                slot1 = Items.WOOL.white();
            }

            resultSlots.setItem(0, new ItemStack(linings.get(slot1).get(slot2)));
        } else if (slot1.equals(ItemRegistry.HIDE_SHEET.asItem()) && isWeavingPattern(slot2)) {
            resultSlots.setItem(0, new ItemStack(cutouts.get(slot2)));
        } else if (ArmourSets.CRAFTS.containsKey(slot1) && isWeavingPattern(slot2)) {
            ArmourSet set = ArmourSets.SETS.get(ArmourSets.CRAFTS.get(slot1));
            String equipment = weavingPatterns.get(slot2);
            ItemStack result = new ItemStack(BuiltInRegistries.ITEM.getValue(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, set.id() + "_" + equipment)));
            result.set(DataComponentTypeRegistry.ARMOUR_SET.get(), new ArmourSetData(set));

            String equipmentCraftStats = DataHelper.getString(player, "craft_stats_" + equipment);
            if (equipmentCraftStats.isEmpty()) {
                ArmourStatsData data = ArmourStatsData.addRandomStats(set, result);
                String s = "";
                for (Map.Entry<String, String> e : data.attributes().attributesInfo().entrySet()) {
                    s += e.getKey();
                    s += "##";
                    s += e.getValue();
                    s += "##";
                }
                s += "///";
                s += data.cleanliness().name().toLowerCase();

                DataHelper.setData(player, "craft_stats_" + equipment, s);
            } else {
                String[] parts = equipmentCraftStats.split("///");
                String[] entries = parts[0].split("##");
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < entries.length; i += 2) {
                    map.put(entries[i], entries[i+1]);
                }

                ArmourStatsData data = new ArmourStatsData(new ArmourStatsData.AttributesInfo(map), ArmourCleanliness.valueOf(parts[1].toUpperCase()));
                result.set(DataComponentTypeRegistry.ARMOUR_STATS, data);
            }

            resultSlots.setItem(0, result);
        } else {
            resultSlots.setItem(0, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.isLocalPlayer();
    }
}
