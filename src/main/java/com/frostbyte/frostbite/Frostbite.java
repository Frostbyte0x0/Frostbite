package com.frostbyte.frostbite;

import com.frostbyte.frostbite.block.ModBlocks;
import com.frostbyte.frostbite.client.FrostbiteClient;
import com.frostbyte.frostbite.component.ModDataComponentTypes;
import com.frostbyte.frostbite.effect.ModEffects;
import com.frostbyte.frostbite.entity.ModEntities;
import com.frostbyte.frostbite.entity.client.AgaricMurdershroomRenderer;
import com.frostbyte.frostbite.item.ModCreativeModeTabs;
import com.frostbyte.frostbite.item.ModItems;
//import com.frostbyte.frostbite.item.custom.alchemy.Jars;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(Frostbite.MOD_ID)
public class Frostbite {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(FrostbiteClient::initClient);
    }

    public static final String MOD_ID = "frostbite";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Frostbite(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::addCreative);

        ModDataComponentTypes.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);
        ModEntities.register(modEventBus);
        ModEffects.register(modEventBus);
        //Jars.register(modEventBus);

        //NeoForge.EVENT_BUS.register(this);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {}


    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.AGARIC_MURDERSHROOM.get(), AgaricMurdershroomRenderer::new);
        }
    }
}
