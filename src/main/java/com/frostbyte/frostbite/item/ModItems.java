package com.frostbyte.frostbite.item;

import com.frostbyte.frostbite.Frostbite;
import com.frostbyte.frostbite.item.custom.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Frostbite.MOD_ID);

    public static final DeferredItem<Item> METAL_COG =
            ITEMS.registerItem("metal_cog", Item::new, new Item.Properties());

    public static final DeferredItem<Item> JAR =
            ITEMS.registerItem("jar", Item::new, new Item.Properties());
    public static final DeferredItem<Item> VIAL =
            ITEMS.registerItem("vial", Item::new, new Item.Properties());

    public static final DeferredItem<Item> ADVANCED_CLOCK =
            ITEMS.registerItem("advanced_clock", AdvancedClockItem::new, new Item.Properties());

    public static final DeferredItem<Item> SPRAYER =
            ITEMS.registerItem("sprayer", SprayerItem::new, new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> EXPLODING_SNOWBALL = ITEMS.register("exploding_snowball",
            () -> new ExplodingSnowballItem(new Item.Properties().stacksTo(16)
                    .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "exploding_snowball")))));
    public static final DeferredItem<Item> HARDENED_SNOWBALL = ITEMS.register("hardened_snowball",
            () -> new HardenedSnowballItem(new Item.Properties().stacksTo(16)
                    .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "hardened_snowball")))));
    public static final DeferredItem<Item> PACKED_HARDENED_SNOWBALL = ITEMS.register("packed_hardened_snowball",
            () -> new PackedHardenedSnowballItem(new Item.Properties().stacksTo(16)
                    .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "packed_hardened_snowball")))));
    public static final DeferredItem<Item> BLUE_HARDENED_SNOWBALL = ITEMS.register("blue_hardened_snowball",
            () -> new BlueHardenedSnowballItem(new Item.Properties().stacksTo(16)
                    .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "blue_hardened_snowball")))));

    public static final DeferredItem<Item> FUR_HELMET = ITEMS.register("fur_helmet",
            () -> new ArmorItem(ModArmorMaterials.FUR, ArmorType.HELMET,
                    new Item.Properties().durability(ArmorType.HELMET.getDurability(5))
                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "fur_helmet")))));
    public static final DeferredItem<Item> FUR_CHESTPLATE = ITEMS.register("fur_chestplate",
            () -> new ArmorItem(ModArmorMaterials.FUR, ArmorType.CHESTPLATE,
                    new Item.Properties().durability(ArmorType.CHESTPLATE.getDurability(5))
                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "fur_chestplate")))));
    public static final DeferredItem<Item> FUR_LEGGINGS = ITEMS.register("fur_leggings",
            () -> new ArmorItem(ModArmorMaterials.FUR, ArmorType.LEGGINGS,
                    new Item.Properties().durability(ArmorType.LEGGINGS.getDurability(5))
                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "fur_leggings")))));
    public static final DeferredItem<Item> FUR_BOOTS = ITEMS.register("fur_boots",
            () -> new ArmorItem(ModArmorMaterials.FUR, ArmorType.BOOTS,
                    new Item.Properties().durability(ArmorType.BOOTS.getDurability(5))
                            .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "fur_boots")))));

    public static final DeferredItem<Item> DRAINING_STAFF =
            ITEMS.registerItem("draining_staff", DrainingStaffItem::new, new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> CHAINCICLE =
            ITEMS.registerItem("chaincicle", ChaincicleItem::new, new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> STUNNING_BELL =
            ITEMS.registerItem("stunning_bell", StunningBellItem::new, new Item.Properties().stacksTo(1));

    public static final DeferredItem<Item> ICE_HAMMER =
            ITEMS.registerItem("ice_hammer", IceHammerItem::new, new Item.Properties()
                    .stacksTo(1)
                    .attributes(IceHammerItem.createAttributes())
                    .component(DataComponents.TOOL, IceHammerItem.createToolProperties())
                    .durability(500));


    public static final DeferredItem<Item> THERMOMETER =
            ITEMS.registerItem("thermometer", ThermometerItem::new, new Item.Properties().stacksTo(1));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}