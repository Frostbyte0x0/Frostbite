package com.frostbyte.frostbite.datagen;

import com.frostbyte.frostbite.Frostbite;
import com.frostbyte.frostbite.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output,  ExistingFileHelper existingFileHelper) {
        super(output, Frostbite.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.ADVANCED_CLOCK.get());
        basicItem(ModItems.METAL_COG.get());
        basicItem(ModItems.EMPTY_JAR.get());
        //basicItem(ModItems.JAR.get());
        basicItem(ModItems.VIAL.get());
        basicItem(ModItems.SPRAYER.get());

        basicItem(ModItems.FUR_HELMET.get());
        basicItem(ModItems.FUR_CHESTPLATE.get());
        basicItem(ModItems.FUR_LEGGINGS.get());
        basicItem(ModItems.FUR_BOOTS.get());

        basicItem(ModItems.EXPLODING_SNOWBALL.get());
        basicItem(ModItems.HARDENED_SNOWBALL.get());
        basicItem(ModItems.PACKED_HARDENED_SNOWBALL.get());
        basicItem(ModItems.BLUE_HARDENED_SNOWBALL.get());

        basicItem(ModItems.DRAINING_STAFF.get());
        basicItem(ModItems.CHAINCICLE.get());
        basicItem(ModItems.STUNNING_BELL.get());
        basicItem(ModItems.ICE_HAMMER.get());

        basicItem(ModItems.SNIPER.get());
        basicItem(ModItems.SNIPER_BULLET.get());
    }
}
