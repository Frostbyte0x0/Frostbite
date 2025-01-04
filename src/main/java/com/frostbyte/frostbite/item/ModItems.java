package com.frostbyte.frostbite.item;

import com.frostbyte.frostbite.item.custom.AdvancedClockItem;
import com.frostbyte.frostbite.Frostbite;
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

    // To keep track of the time I'm wasting on debugging
    public static final DeferredItem<Item> ADVANCED_CLOCK =
            ITEMS.registerItem("advanced_clock", AdvancedClockItem::new, new Item.Properties());

    // To keep track of the time I'm wasting on debugging
    public static final DeferredItem<Item> EXPLODING_SNOWBALL = ITEMS.register("exploding_snowball",
            () -> new Item(new Item.Properties().stacksTo(16)
                    .setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "exploding_snowball")))));

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


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}