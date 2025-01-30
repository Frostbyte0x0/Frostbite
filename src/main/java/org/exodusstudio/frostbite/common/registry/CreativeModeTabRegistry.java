package org.exodusstudio.frostbite.common.registry;

import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;

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
                        output.accept(ItemRegistry.METAL_COG);
                        output.accept(ItemRegistry.VIAL);
                        output.accept(ItemRegistry.EMPTY_JAR);
                        output.accept(ItemRegistry.SPRAYER);

                        output.accept(ModBlocks.BLACK_BLOCK);

                        output.accept(ItemRegistry.FUR_HELMET);
                        output.accept(ItemRegistry.FUR_CHESTPLATE);
                        output.accept(ItemRegistry.FUR_LEGGINGS);
                        output.accept(ItemRegistry.FUR_BOOTS);

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

//                        itemDisplayParameters.holders()
//                                .lookup().ifPresent(
//                                        lookup -> generatePotionEffectTypes(
//                                                output,
//                                                lookup,
//                                                ModItems.JAR.asItem(),
//                                                CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS,
//                                                itemDisplayParameters.enabledFeatures()
//                                ));
                    })).build());
}
