package org.exodusstudio.frostbite.common.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output,  ExistingFileHelper existingFileHelper) {
        super(output, Frostbite.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        /*
        basicItem(ItemRegistry.ADVANCED_CLOCK.get());
        basicItem(ItemRegistry.METAL_COG.get());
        basicItem(ItemRegistry.EMPTY_JAR.get());
        basicItem(ItemRegistry.JAR.get());
        basicItem(ItemRegistry.VIAL.get());
        basicItem(ItemRegistry.SPRAYER.get());

        basicItem(ItemRegistry.FUR_HELMET.get());
        basicItem(ItemRegistry.FUR_CHESTPLATE.get());
        basicItem(ItemRegistry.FUR_LEGGINGS.get());
        basicItem(ItemRegistry.FUR_BOOTS.get());

        basicItem(ItemRegistry.EXPLODING_SNOWBALL.get());
        basicItem(ItemRegistry.HARDENED_SNOWBALL.get());
        basicItem(ItemRegistry.PACKED_HARDENED_SNOWBALL.get());
        basicItem(ItemRegistry.BLUE_HARDENED_SNOWBALL.get());

        basicItem(ItemRegistry.DRAINING_STAFF.get());
        basicItem(ItemRegistry.CHAINCICLE.get());
        basicItem(ItemRegistry.STUNNING_BELL.get());
        basicItem(ItemRegistry.ICE_HAMMER.get());

        basicItem(ItemRegistry.SNIPER.get());
        basicItem(ItemRegistry.SNIPER_BULLET.get());
         */

        saplingItem(BlockRegistry.MISTY_SAPLING);
        saplingItem(BlockRegistry.DIM_SAPLING);
        saplingItem(BlockRegistry.SILVER_SAPLING);
        saplingItem(BlockRegistry.CHARM_SAPLING);
    }

    private ItemModelBuilder saplingItem(DeferredBlock<Block> item) {
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated")).texture("layer0",
                ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID,"block/" + item.getId().getPath()));
    }
}
