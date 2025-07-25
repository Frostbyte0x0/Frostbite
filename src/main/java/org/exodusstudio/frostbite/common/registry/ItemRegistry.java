package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DeathProtection;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.component.ChargeData;
import org.exodusstudio.frostbite.common.component.GunData;
import org.exodusstudio.frostbite.common.component.ModeData;
import org.exodusstudio.frostbite.common.item.ModArmorMaterials;
import org.exodusstudio.frostbite.common.item.custom.*;
import org.exodusstudio.frostbite.common.item.custom.last_stand.LastStandItem;
import org.exodusstudio.frostbite.common.item.custom.lining.LiningItem;
import org.exodusstudio.frostbite.common.item.custom.lining.LiningMaterials;
import org.exodusstudio.frostbite.common.item.custom.weapons.ChaincicleItem;
import org.exodusstudio.frostbite.common.item.custom.weapons.GaleFanItem;
import org.exodusstudio.frostbite.common.item.custom.weapons.IceHammerItem;
import org.exodusstudio.frostbite.common.item.custom.weapons.StunningBellItem;
import org.exodusstudio.frostbite.common.item.custom.weapons.elf_weapons.CastingStaffItem;
import org.exodusstudio.frostbite.common.item.custom.weapons.elf_weapons.DrainingStaffItem;
import org.exodusstudio.frostbite.common.item.custom.weapons.gun.RevolverItem;
import org.exodusstudio.frostbite.common.item.custom.weapons.gun.SniperItem;
import org.exodusstudio.frostbite.common.item.custom.weapons.gun.bullet.RevolverBulletItem;
import org.exodusstudio.frostbite.common.item.custom.weapons.gun.bullet.SniperBulletItem;

import java.util.List;

public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Frostbite.MOD_ID);

    public static final DeferredItem<Item> METAL_COG =
            ITEMS.register("metal_cog", (id) -> new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> ADVANCED_CLOCK =
            ITEMS.register("advanced_clock", (id) -> new AdvancedClockItem(new Item.Properties()
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

    public static final DeferredItem<Item> BLACK_ICE_HELMET = ITEMS.register("black_ice_helmet",
            (id) -> new ArmorItem(ModArmorMaterials.BLACK_ICE, ArmorType.HELMET,
                    new Item.Properties()//.durability(ArmorType.HELMET.getDurability(15))
                            .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> BLACK_ICE_CHESTPLATE = ITEMS.register("black_ice_chestplate",
            (id) -> new ArmorItem(ModArmorMaterials.BLACK_ICE, ArmorType.CHESTPLATE,
                    new Item.Properties()//.durability(ArmorType.CHESTPLATE.getDurability(15))
                            .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> BLACK_ICE_LEGGINGS = ITEMS.register("black_ice_leggings",
            (id) -> new ArmorItem(ModArmorMaterials.BLACK_ICE, ArmorType.LEGGINGS,
                    new Item.Properties()//.durability(ArmorType.LEGGINGS.getDurability(15))
                            .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> BLACK_ICE_BOOTS = ITEMS.register("black_ice_boots",
            (id) -> new ArmorItem(ModArmorMaterials.BLACK_ICE, ArmorType.BOOTS,
                    new Item.Properties()//.durability(ArmorType.BOOTS.getDurability(15))
                            .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> FROSTBITTEN_HELMET = ITEMS.register("frostbitten_helmet",
            (id) -> new ArmorItem(ModArmorMaterials.FROSTBITTEN, ArmorType.HELMET,
                    new Item.Properties()//.durability(ArmorType.HELMET.getDurability(15))
                            .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> FROSTBITTEN_CHESTPLATE = ITEMS.register("frostbitten_chestplate",
            (id) -> new ArmorItem(ModArmorMaterials.FROSTBITTEN, ArmorType.CHESTPLATE,
                    new Item.Properties()//.durability(ArmorType.CHESTPLATE.getDurability(15))
                            .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> FROSTBITTEN_LEGGINGS = ITEMS.register("frostbitten_leggings",
            (id) -> new ArmorItem(ModArmorMaterials.FROSTBITTEN, ArmorType.LEGGINGS,
                    new Item.Properties()//.durability(ArmorType.LEGGINGS.getDurability(15))
                            .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> FROSTBITTEN_BOOTS = ITEMS.register("frostbitten_boots",
            (id) -> new ArmorItem(ModArmorMaterials.FROSTBITTEN, ArmorType.BOOTS,
                    new Item.Properties()//.durability(ArmorType.BOOTS.getDurability(15))
                            .setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> WOOL_LINING_HELMET = ITEMS.register("wool_lining_helmet",
            (id) -> new LiningItem(LiningMaterials.WOOL, ArmorType.HELMET,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> WOOL_LINING_CHESTPLATE = ITEMS.register("wool_lining_chestplate",
            (id) -> new LiningItem(LiningMaterials.WOOL, ArmorType.CHESTPLATE,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> WOOL_LINING_LEGGINGS = ITEMS.register("wool_lining_leggings",
            (id) -> new LiningItem(LiningMaterials.WOOL, ArmorType.LEGGINGS,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> WOOL_LINING_BOOTS = ITEMS.register("wool_lining_boots",
            (id) -> new LiningItem(LiningMaterials.WOOL, ArmorType.BOOTS,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> WOOLLY_WOOL_LINING_HELMET = ITEMS.register("woolly_wool_lining_helmet",
            (id) -> new LiningItem(LiningMaterials.WOOLLY_WOOL, ArmorType.HELMET,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> WOOLLY_WOOL_LINING_CHESTPLATE = ITEMS.register("woolly_wool_lining_chestplate",
            (id) -> new LiningItem(LiningMaterials.WOOLLY_WOOL, ArmorType.CHESTPLATE,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> WOOLLY_WOOL_LINING_LEGGINGS = ITEMS.register("woolly_wool_lining_leggings",
            (id) -> new LiningItem(LiningMaterials.WOOLLY_WOOL, ArmorType.LEGGINGS,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> WOOLLY_WOOL_LINING_BOOTS = ITEMS.register("woolly_wool_lining_boots",
            (id) -> new LiningItem(LiningMaterials.WOOLLY_WOOL, ArmorType.BOOTS,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> FROZEN_FUR_LINING_HELMET = ITEMS.register("frozen_fur_lining_helmet",
            (id) -> new LiningItem(LiningMaterials.FROZEN_FUR, ArmorType.HELMET,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> FROZEN_FUR_LINING_CHESTPLATE = ITEMS.register("frozen_fur_lining_chestplate",
            (id) -> new LiningItem(LiningMaterials.FROZEN_FUR, ArmorType.CHESTPLATE,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> FROZEN_FUR_LINING_LEGGINGS = ITEMS.register("frozen_fur_lining_leggings",
            (id) -> new LiningItem(LiningMaterials.FROZEN_FUR, ArmorType.LEGGINGS,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> FROZEN_FUR_LINING_BOOTS = ITEMS.register("frozen_fur_lining_boots",
            (id) -> new LiningItem(LiningMaterials.FROZEN_FUR, ArmorType.BOOTS,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> INSULATED_CLOTH_LINING_HELMET = ITEMS.register("insulated_cloth_lining_helmet",
            (id) -> new LiningItem(LiningMaterials.INSULATED_CLOTH, ArmorType.HELMET,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> INSULATED_CLOTH_LINING_CHESTPLATE = ITEMS.register("insulated_cloth_lining_chestplate",
            (id) -> new LiningItem(LiningMaterials.INSULATED_CLOTH, ArmorType.CHESTPLATE,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> INSULATED_CLOTH_LINING_LEGGINGS = ITEMS.register("insulated_cloth_lining_leggings",
            (id) -> new LiningItem(LiningMaterials.INSULATED_CLOTH, ArmorType.LEGGINGS,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> INSULATED_CLOTH_LINING_BOOTS = ITEMS.register("insulated_cloth_lining_boots",
            (id) -> new LiningItem(LiningMaterials.INSULATED_CLOTH, ArmorType.BOOTS,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> HEATED_COATING_LINING_HELMET = ITEMS.register("heated_coating_lining_helmet",
            (id) -> new LiningItem(LiningMaterials.HEATED_COATING, ArmorType.HELMET,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> HEATED_COATING_LINING_CHESTPLATE = ITEMS.register("heated_coating_lining_chestplate",
            (id) -> new LiningItem(LiningMaterials.HEATED_COATING, ArmorType.CHESTPLATE,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> HEATED_COATING_LINING_LEGGINGS = ITEMS.register("heated_coating_lining_leggings",
            (id) -> new LiningItem(LiningMaterials.HEATED_COATING, ArmorType.LEGGINGS,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> HEATED_COATING_LINING_BOOTS = ITEMS.register("heated_coating_lining_boots",
            (id) -> new LiningItem(LiningMaterials.HEATED_COATING, ArmorType.BOOTS,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));

    public static final DeferredItem<Item> FROZEN_PLATING_LINING_HELMET = ITEMS.register("frozen_plating_lining_helmet",
            (id) -> new LiningItem(LiningMaterials.FROZEN_PLATING, ArmorType.HELMET,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> FROZEN_PLATING_LINING_CHESTPLATE = ITEMS.register("frozen_plating_lining_chestplate",
            (id) -> new LiningItem(LiningMaterials.FROZEN_PLATING, ArmorType.CHESTPLATE,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> FROZEN_PLATING_LINING_LEGGINGS = ITEMS.register("frozen_plating_lining_leggings",
            (id) -> new LiningItem(LiningMaterials.FROZEN_PLATING, ArmorType.LEGGINGS,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> FROZEN_PLATING_LINING_BOOTS = ITEMS.register("frozen_plating_lining_boots",
            (id) -> new LiningItem(LiningMaterials.FROZEN_PLATING, ArmorType.BOOTS,
                    new Item.Properties().setId(ResourceKey.create(Registries.ITEM, id))));


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

    public static final DeferredItem<Item> BOTTLE_OF_WARMTH =
            ITEMS.register("bottle_of_warmth", (id) -> new Item(new Item.Properties()
                    .craftRemainder(Items.GLASS_BOTTLE)
                    .food(FoodRegistry.WARMING_BOTTLE, ConsumableRegistry.WARMTH_BOTTLE)
                    .usingConvertsTo(Items.GLASS_BOTTLE)
                    .stacksTo(16)
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> BOTTLE_OF_HEAT =
            ITEMS.register("bottle_of_heat", (id) -> new Item(new Item.Properties()
                    .craftRemainder(Items.GLASS_BOTTLE)
                    .food(FoodRegistry.WARMING_BOTTLE, ConsumableRegistry.HEAT_BOTTLE)
                    .usingConvertsTo(Items.GLASS_BOTTLE)
                    .stacksTo(16)
                    .setId(ResourceKey.create(Registries.ITEM, id))));


    public static final DeferredItem<Item> CASTING_STAFF =
            ITEMS.register("casting_staff", (id) -> new CastingStaffItem(new Item.Properties()
                    .stacksTo(1)
                    .useCooldown(6)
                    .setId(ResourceKey.create(Registries.ITEM, id))));
    public static final DeferredItem<Item> GALE_FAN =
            ITEMS.register("gale_fan", (id) -> new GaleFanItem(new Item.Properties()
                    .component(DataComponentTypeRegistry.MODE, new ModeData("firstAttack"))
                    .component(DataComponentTypeRegistry.CHARGE, new ChargeData(0))
                    .stacksTo(1)
                    .setId(ResourceKey.create(Registries.ITEM, id))));
}