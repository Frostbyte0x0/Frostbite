package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.component.ChargeData;
import org.exodusstudio.frostbite.common.component.GunData;
import org.exodusstudio.frostbite.common.component.JarContentsData;
import org.exodusstudio.frostbite.common.component.ModeData;
import org.exodusstudio.frostbite.common.item.ModArmorMaterials;
import org.exodusstudio.frostbite.common.item.custom.*;
import org.exodusstudio.frostbite.common.item.custom.alchemy.EmptyJarItem;
import org.exodusstudio.frostbite.common.item.custom.alchemy.JarItem;
import org.exodusstudio.frostbite.common.item.custom.alchemy.SprayerItem;
import org.exodusstudio.frostbite.common.item.custom.gun.RevolverItem;
import org.exodusstudio.frostbite.common.item.custom.gun.SniperItem;
import org.exodusstudio.frostbite.common.item.custom.gun.bullet.RevolverBulletItem;
import org.exodusstudio.frostbite.common.item.custom.gun.bullet.SniperBulletItem;
import org.exodusstudio.frostbite.common.item.custom.last_stand.LastStandItem;

import java.util.List;

public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Frostbite.MOD_ID);

    public static final DeferredItem<Item> METAL_COG =
            ITEMS.register("metal_cog", (id) -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> EMPTY_JAR =
            ITEMS.register("empty_jar", (id) -> new EmptyJarItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> JAR =
            ITEMS.register("jar", (id) -> new JarItem(new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponentTypeRegistry.JAR_CONTENTS, JarContentsData.EMPTY)
                    .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> VIAL =
            ITEMS.register("vial", (id) -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> ADVANCED_CLOCK =
            ITEMS.register("advanced_clock", (id) -> new AdvancedClockItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> SPRAYER =
            ITEMS.register("sprayer", (id) -> new SprayerItem(new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY)
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> EXPLODING_SNOWBALL = ITEMS.register("exploding_snowball",
            (id) -> new ExplodingSnowballItem(new Item.Properties().stacksTo(16)
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> HARDENED_SNOWBALL = ITEMS.register("hardened_snowball",
            (id) -> new HardenedSnowballItem(new Item.Properties().stacksTo(16)
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> PACKED_HARDENED_SNOWBALL = ITEMS.register("packed_hardened_snowball",
            (id) -> new PackedHardenedSnowballItem(new Item.Properties().stacksTo(16)
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> BLUE_HARDENED_SNOWBALL = ITEMS.register("blue_hardened_snowball",
            (id) -> new BlueHardenedSnowballItem(new Item.Properties().stacksTo(16)
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> FUR_HELMET = ITEMS.register("fur_helmet",
            (id) -> new ArmorItem(ModArmorMaterials.FUR, ArmorType.HELMET,
                    new Item.Properties().durability(ArmorType.HELMET.getDurability(5))
                            .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> FUR_CHESTPLATE = ITEMS.register("fur_chestplate",
            (id) -> new ArmorItem(ModArmorMaterials.FUR, ArmorType.CHESTPLATE,
                    new Item.Properties().durability(ArmorType.CHESTPLATE.getDurability(5))
                            .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> FUR_LEGGINGS = ITEMS.register("fur_leggings",
            (id) -> new ArmorItem(ModArmorMaterials.FUR, ArmorType.LEGGINGS,
                    new Item.Properties().durability(ArmorType.LEGGINGS.getDurability(5))
                            .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> FUR_BOOTS = ITEMS.register("fur_boots",
            (id) -> new ArmorItem(ModArmorMaterials.FUR, ArmorType.BOOTS,
                    new Item.Properties().durability(ArmorType.BOOTS.getDurability(5))
                            .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> DRAINING_STAFF =
            ITEMS.register("draining_staff", (id) -> new DrainingStaffItem(new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponentTypeRegistry.MODE, new ModeData("drain"))
                    .component(DataComponentTypeRegistry.CHARGE, ChargeData.EMPTY)
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> CHAINCICLE =
            ITEMS.register("chaincicle", (id) -> new ChaincicleItem(new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponentTypeRegistry.MODE, new ModeData("swipe"))
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> STUNNING_BELL =
            ITEMS.register("stunning_bell", (id) -> new StunningBellItem(new Item.Properties()
                    .stacksTo(1)
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> ICE_HAMMER =
            ITEMS.register("ice_hammer", (id) -> new IceHammerItem(new Item.Properties()
                    .stacksTo(1)
                    .attributes(IceHammerItem.createAttributes())
                    .component(DataComponents.TOOL, IceHammerItem.createToolProperties())
                    .component(DataComponentTypeRegistry.CHARGE, new ChargeData(0))
                    .durability(500)
                    .setId(ResourceKey.create(Registries.ITEM, id))));


    public static final DeferredItem<Item> SNIPER_BULLET =
            ITEMS.register("sniper_bullet", (id) -> new SniperBulletItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> SNIPER =
            ITEMS.register("sniper", (id) -> new SniperItem(new Item.Properties().stacksTo(1)
                            .component(DataComponentTypeRegistry.GUN, GunData.EMPTY)
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> REVOLVER_BULLET =
            ITEMS.register("revolver_bullet", (id) -> new RevolverBulletItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> REVOLVER =
            ITEMS.register("revolver", (id) -> new RevolverItem(new Item.Properties().stacksTo(1)
                            .component(DataComponentTypeRegistry.GUN, GunData.EMPTY)
                    .setId(ResourceKey.create(Registries.ITEM, id))));


    public static final DeferredItem<Item> THERMOMETER =
            ITEMS.register("thermometer", (id) -> new ThermometerItem(new Item.Properties()
                    .stacksTo(1)
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> LAST_STAND =
            ITEMS.register("last_stand", (id) -> new LastStandItem(new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponents.DEATH_PROTECTION, new DeathProtection(
                            List.of(new ApplyStatusEffectsConsumeEffect(List.of(new MobEffectInstance(EffectRegistry.RAGE, 300, 1))))))
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> CONFETTI_POPPER =
            ITEMS.register("confetti_popper", (id) -> new ConfettiPopperItem(new Item.Properties()
                    .stacksTo(1)
                    .durability(10)
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> THERMAL_LENS =
            ITEMS.register("thermal_lens", (id) -> new ThermalLensItem(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> HELMET_WEAVING_PATTERN =
            ITEMS.register("helmet_weaving_pattern", (id) -> new Item(new Item.Properties()
                    .stacksTo(1)
                    .durability(3)
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> CHESTPLATE_WEAVING_PATTERN =
            ITEMS.register("chestplate_weaving_pattern", (id) -> new Item(new Item.Properties()
                    .stacksTo(1)
                    .durability(3)
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> LEGGINGS_WEAVING_PATTERN =
            ITEMS.register("leggings_weaving_pattern", (id) -> new Item(new Item.Properties()
                    .stacksTo(1)
                    .durability(3)
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> BOOTS_WEAVING_PATTERN =
            ITEMS.register("boots_weaving_pattern", (id) -> new Item(new Item.Properties()
                    .stacksTo(1)
                    .durability(3)
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> JELLY =
            ITEMS.register("jelly", (id) -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> INSULATING_JELLY =
            ITEMS.register("insulating_jelly", (id) -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));
}