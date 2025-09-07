package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;

import java.util.function.Supplier;

public class CreativeModeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Frostbite.MOD_ID);

    public static final Supplier<CreativeModeTab> FROSTBITE_TAB =
            CREATIVE_MODE_TABS.register("frostbite_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.frostbite.frostbite_tab"))
                    .icon(() -> new ItemStack(ItemRegistry.FROSTBITTEN_GEM.get()))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ItemRegistry.ADVANCED_CLOCK);
                        output.accept(ItemRegistry.LAST_STAND);
                        output.accept(ItemRegistry.METAL_COG);

                        output.accept(BlockRegistry.REINFORCED_BLACK_ICE);
                        output.accept(BlockRegistry.REINFORCED_BLACK_ICE_RECEPTACLE);
                        output.accept(ItemRegistry.FROSTBITTEN_GEM);
                        output.accept(ItemRegistry.STARSEEKING_COMPASS);

                        output.accept(BlockRegistry.PERMAFROZEN_DIRT);
                        output.accept(BlockRegistry.SLATED_ICE);
                        output.accept(BlockRegistry.CHISELED_SLATED_ICE);
                        output.accept(BlockRegistry.COBBLED_SLATED_ICE);
                        output.accept(BlockRegistry.CRACKED_SLATED_ICE_TILES);
                        output.accept(BlockRegistry.POLISHED_SLATED_ICE);
                        output.accept(BlockRegistry.SLATED_ICE_BRICKS);
                        output.accept(BlockRegistry.SLATED_ICE_TILES);

                        output.accept(BlockRegistry.MARBLE);
                        output.accept(BlockRegistry.MARBLE_BRICKS);
                        output.accept(BlockRegistry.CHISELED_MARBLE);
                        output.accept(BlockRegistry.MARBLE_PILLAR);

                        output.accept(BlockRegistry.LODESTAR);

                        output.accept(ItemRegistry.BLACK_ICE_HELMET);
                        output.accept(ItemRegistry.BLACK_ICE_CHESTPLATE);
                        output.accept(ItemRegistry.BLACK_ICE_LEGGINGS);
                        output.accept(ItemRegistry.BLACK_ICE_BOOTS);

                        output.accept(ItemRegistry.FROSTBITTEN_HELMET);
                        output.accept(ItemRegistry.FROSTBITTEN_CHESTPLATE);
                        output.accept(ItemRegistry.FROSTBITTEN_LEGGINGS);
                        output.accept(ItemRegistry.FROSTBITTEN_BOOTS);

                        output.accept(ItemRegistry.WOOL_LINING_HELMET);
                        output.accept(ItemRegistry.WOOL_LINING_CHESTPLATE);
                        output.accept(ItemRegistry.WOOL_LINING_LEGGINGS);
                        output.accept(ItemRegistry.WOOL_LINING_BOOTS);

                        output.accept(ItemRegistry.WOOLLY_WOOL_LINING_HELMET);
                        output.accept(ItemRegistry.WOOLLY_WOOL_LINING_CHESTPLATE);
                        output.accept(ItemRegistry.WOOLLY_WOOL_LINING_LEGGINGS);
                        output.accept(ItemRegistry.WOOLLY_WOOL_LINING_BOOTS);

                        output.accept(ItemRegistry.FROZEN_FUR_LINING_HELMET);
                        output.accept(ItemRegistry.FROZEN_FUR_LINING_CHESTPLATE);
                        output.accept(ItemRegistry.FROZEN_FUR_LINING_LEGGINGS);
                        output.accept(ItemRegistry.FROZEN_FUR_LINING_BOOTS);

                        output.accept(ItemRegistry.INSULATED_CLOTH_LINING_HELMET);
                        output.accept(ItemRegistry.INSULATED_CLOTH_LINING_CHESTPLATE);
                        output.accept(ItemRegistry.INSULATED_CLOTH_LINING_LEGGINGS);
                        output.accept(ItemRegistry.INSULATED_CLOTH_LINING_BOOTS);

                        output.accept(ItemRegistry.HEATED_COATING_LINING_HELMET);
                        output.accept(ItemRegistry.HEATED_COATING_LINING_CHESTPLATE);
                        output.accept(ItemRegistry.HEATED_COATING_LINING_LEGGINGS);
                        output.accept(ItemRegistry.HEATED_COATING_LINING_BOOTS);

                        output.accept(ItemRegistry.FROZEN_PLATING_LINING_HELMET);
                        output.accept(ItemRegistry.FROZEN_PLATING_LINING_CHESTPLATE);
                        output.accept(ItemRegistry.FROZEN_PLATING_LINING_LEGGINGS);
                        output.accept(ItemRegistry.FROZEN_PLATING_LINING_BOOTS);

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
                        output.accept(BlockRegistry.FLOWERING_CHARM_LEAVES);
                        output.accept(BlockRegistry.CHARM_SAPLING);

                        output.accept(BlockRegistry.GIANT_REED);

                        // MOSSY CHARM WOOD
                        output.accept(BlockRegistry.MOSSY_CHARM_LOG);
                        output.accept(BlockRegistry.MOSSY_CHARM_WOOD);
                        output.accept(BlockRegistry.MOSSY_STRIPPED_CHARM_LOG);
                        output.accept(BlockRegistry.MOSSY_STRIPPED_CHARM_WOOD);
                        output.accept(BlockRegistry.MOSSY_CHARM_PLANKS);

                        // LAVENDER WOOD
                        output.accept(BlockRegistry.LAVENDER_LOG);
                        output.accept(BlockRegistry.LAVENDER_WOOD);
                        output.accept(BlockRegistry.STRIPPED_LAVENDER_LOG);
                        output.accept(BlockRegistry.STRIPPED_LAVENDER_WOOD);
                        output.accept(BlockRegistry.LAVENDER_PLANKS);
                        output.accept(BlockRegistry.LAVENDER_LEAVES);
                        output.accept(BlockRegistry.LAVENDER_SAPLING);

                        // SHINNING CEDAR WOOD
                        output.accept(BlockRegistry.SHINNING_CEDAR_LOG);
                        output.accept(BlockRegistry.SHINNING_CEDAR_WOOD);
                        output.accept(BlockRegistry.STRIPPED_SHINNING_CEDAR_LOG);
                        output.accept(BlockRegistry.STRIPPED_SHINNING_CEDAR_WOOD);
                        output.accept(BlockRegistry.SHINNING_CEDAR_PLANKS);
                        output.accept(BlockRegistry.SHINNING_CEDAR_LEAVES);
                        output.accept(BlockRegistry.SHINNING_CEDAR_SAPLING);

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
                        output.accept(ItemRegistry.GALE_FAN);

                        output.accept(BlockRegistry.SMALL_HEATER_BLOCK);
                        output.accept(BlockRegistry.MEDIUM_HEATER_BLOCK);
                        output.accept(BlockRegistry.BIG_HEATER_BLOCK);
                    })).build());
}
