package com.frostbyte.frostbite;

import com.frostbyte.frostbite.block.ModBlocks;
import com.frostbyte.frostbite.entity.ModEntities;
import com.frostbyte.frostbite.item.ModCreativeModeTabs;
import com.frostbyte.frostbite.item.ModItems;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Frostbite.MOD_ID)
public class Frostbite {
    public static final String MOD_ID = "frostbite";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Frostbite(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::addCreative);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModEntities.register(modEventBus);

        //NeoForge.EVENT_BUS.register(this);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {}
}
