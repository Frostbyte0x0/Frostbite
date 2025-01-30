package org.exodusstudio.frostbite.common.datagen;

import org.exodusstudio.frostbite.Frostbite;
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
        basicItem(ItemRegistry.ADVANCED_CLOCK.get());
        basicItem(ItemRegistry.METAL_COG.get());
        basicItem(ItemRegistry.EMPTY_JAR.get());
        //basicItem(ModItems.JAR.get());
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
    }
}
