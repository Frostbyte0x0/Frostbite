package org.exodusstudio.frostbite.item;

import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Frostbite.MOD_ID);

    public static final Supplier<CreativeModeTab> FROSTBITE_TAB =
            CREATIVE_MODE_TABS.register("frostbite_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.frostbite.frostbite_tab"))
                    .icon(() -> new ItemStack(ModItems.BLUE_HARDENED_SNOWBALL.get()))
                    .displayItems(((itemDisplayParameters, output) -> {
                        output.accept(ModItems.ADVANCED_CLOCK);
                        output.accept(ModItems.METAL_COG);
                        output.accept(ModItems.VIAL);
                        output.accept(ModItems.EMPTY_JAR);
                        output.accept(ModItems.SPRAYER);

                        output.accept(ModBlocks.BLACK_BLOCK);

                        output.accept(ModItems.FUR_HELMET);
                        output.accept(ModItems.FUR_CHESTPLATE);
                        output.accept(ModItems.FUR_LEGGINGS);
                        output.accept(ModItems.FUR_BOOTS);

                        output.accept(ModItems.EXPLODING_SNOWBALL);
                        output.accept(ModItems.HARDENED_SNOWBALL);
                        output.accept(ModItems.PACKED_HARDENED_SNOWBALL);
                        output.accept(ModItems.BLUE_HARDENED_SNOWBALL);

                        output.accept(ModItems.DRAINING_STAFF);
                        output.accept(ModItems.CHAINCICLE);
                        output.accept(ModItems.STUNNING_BELL);
                        output.accept(ModItems.ICE_HAMMER);

                        output.accept(ModItems.SNIPER);
                        output.accept(ModItems.SNIPER_BULLET);

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


    private static void generatePotionEffectTypes(CreativeModeTab.Output output, HolderLookup<Potion> potions, Item item, CreativeModeTab.TabVisibility tabVisibility, FeatureFlagSet requiredFeatures) {
        potions.listElements()
                .filter(potionReference -> potionReference.value().isEnabled(requiredFeatures))
                .map(potionReference -> PotionContents.createItemStack(item, potionReference))
                .forEach(itemStack -> output.accept(itemStack, tabVisibility));
    }
}
