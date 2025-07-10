package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import org.exodusstudio.frostbite.Frostbite;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.common.component.JarContentsData;
import org.exodusstudio.frostbite.common.item.custom.alchemy.Jar;

import java.util.function.Supplier;

public class CreativeModeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Frostbite.MOD_ID);

    public static final Supplier<CreativeModeTab> FROSTBITE_TAB =
            CREATIVE_MODE_TABS.register("frostbite_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.frostbite.frostbite_tab"))
                    .icon(() -> new ItemStack(ItemRegistry.BLUE_HARDENED_SNOWBALL.get()))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ItemRegistry.ADVANCED_CLOCK);
                        output.accept(ItemRegistry.LAST_STAND);
                        output.accept(ItemRegistry.METAL_COG);
                        output.accept(ItemRegistry.VIAL);
                        output.accept(ItemRegistry.EMPTY_JAR);
                        output.accept(ItemRegistry.SPRAYER);

                        output.accept(BlockRegistry.PERMAFROZEN_DIRT);
                        output.accept(BlockRegistry.PERMAFROZEN_GRASS);
                        output.accept(BlockRegistry.PETRIFIED_ICE);

                        output.accept(ItemRegistry.BLACK_ICE_HELMET);
                        output.accept(ItemRegistry.BLACK_ICE_CHESTPLATE);
                        output.accept(ItemRegistry.BLACK_ICE_LEGGINGS);
                        output.accept(ItemRegistry.BLACK_ICE_BOOTS);

                        output.accept(ItemRegistry.FROSTBITTEN_HELMET);
                        output.accept(ItemRegistry.FROSTBITTEN_CHESTPLATE);
                        output.accept(ItemRegistry.FROSTBITTEN_LEGGINGS);
                        output.accept(ItemRegistry.FROSTBITTEN_BOOTS);

                        output.accept(ItemRegistry.EXPLODING_SNOWBALL);
                        output.accept(ItemRegistry.HARDENED_SNOWBALL);
                        output.accept(ItemRegistry.PACKED_HARDENED_SNOWBALL);
                        output.accept(ItemRegistry.BLUE_HARDENED_SNOWBALL);

                        output.accept(ItemRegistry.DRAINING_STAFF);
                        output.accept(ItemRegistry.CHAINCICLE);
                        output.accept(ItemRegistry.STUNNING_BELL);
                        output.accept(ItemRegistry.ICE_HAMMER);

                        output.accept(ItemRegistry.SNIPER);
                        output.accept(ItemRegistry.SNIPER_BULLET);
                        output.accept(ItemRegistry.REVOLVER);
                        output.accept(ItemRegistry.REVOLVER_BULLET);

                        // MISTY WOOD
                        output.accept(BlockRegistry.MISTY_LOG);
                        output.accept(BlockRegistry.MISTY_WOOD);
                        output.accept(BlockRegistry.STRIPPED_MISTY_LOG);
                        output.accept(BlockRegistry.STRIPPED_MISTY_WOOD);
                        output.accept(BlockRegistry.MISTY_PLANKS);
                        output.accept(BlockRegistry.MISTY_LEAVES);
                        output.accept(BlockRegistry.MISTY_SAPLING);

                        // CHARM WOOD
                        output.accept(BlockRegistry.CHARM_LOG);
                        output.accept(BlockRegistry.CHARM_WOOD);
                        output.accept(BlockRegistry.STRIPPED_CHARM_LOG);
                        output.accept(BlockRegistry.STRIPPED_CHARM_WOOD);
                        output.accept(BlockRegistry.CHARM_PLANKS);
                        output.accept(BlockRegistry.CHARM_LEAVES);
                        output.accept(BlockRegistry.CHARM_SAPLING);

                        // DIM WOOD
                        output.accept(BlockRegistry.DIM_LOG);
                        output.accept(BlockRegistry.DIM_WOOD);
                        output.accept(BlockRegistry.STRIPPED_DIM_LOG);
                        output.accept(BlockRegistry.STRIPPED_DIM_WOOD);
                        output.accept(BlockRegistry.DIM_PLANKS);
                        output.accept(BlockRegistry.DIM_LEAVES);
                        output.accept(BlockRegistry.DIM_SAPLING);

                        // SILVER WOOD
                        output.accept(BlockRegistry.SILVER_LOG);
                        output.accept(BlockRegistry.SILVER_WOOD);
                        output.accept(BlockRegistry.STRIPPED_SILVER_LOG);
                        output.accept(BlockRegistry.STRIPPED_SILVER_WOOD);
                        output.accept(BlockRegistry.SILVER_PLANKS);
                        output.accept(BlockRegistry.SILVER_LEAVES);
                        output.accept(BlockRegistry.SILVER_SAPLING);


                        itemDisplayParameters.holders()
                                .lookup(RegistryRegistry.JAR_REGISTRY_KEY).ifPresent(
                                        lookup -> generatePotionEffectTypes(
                                                output,
                                                lookup,
                                                ItemRegistry.JAR.asItem(),
                                                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
                                                itemDisplayParameters.enabledFeatures()
                                ));

                        output.accept(ItemRegistry.CONFETTI_POPPER);
                        output.accept(ItemRegistry.THERMAL_LENS);
                        output.accept(ItemRegistry.HELMET_WEAVING_PATTERN);
                        output.accept(ItemRegistry.CHESTPLATE_WEAVING_PATTERN);
                        output.accept(ItemRegistry.LEGGINGS_WEAVING_PATTERN);
                        output.accept(ItemRegistry.BOOTS_WEAVING_PATTERN);
                        output.accept(ItemRegistry.BOTTLE_OF_WARMTH);
                        output.accept(ItemRegistry.BOTTLE_OF_HEAT);
                        output.accept(ItemRegistry.JELLY);
                        output.accept(ItemRegistry.INSULATING_JELLY);
                        output.accept(ItemRegistry.CASTING_STAFF);
                    })).build());

    private static void generatePotionEffectTypes(
            CreativeModeTab.Output output,
            HolderLookup<Jar> jars,
            Item item,
            CreativeModeTab.TabVisibility tabVisibility,
            FeatureFlagSet requiredFeatures
    ) {
        jars.listElements()
                .filter(p_337926_ -> p_337926_.value().isEnabled(requiredFeatures))
                .map(p_330083_ -> JarContentsData.createItemStack(item, p_330083_))
                .forEach(p_270000_ -> output.accept(p_270000_, tabVisibility));
    }
}
