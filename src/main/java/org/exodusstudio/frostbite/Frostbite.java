package org.exodusstudio.frostbite;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.exodusstudio.frostbite.common.block.ModBlocks;
import org.exodusstudio.frostbite.client.FrostbiteClient;
import org.exodusstudio.frostbite.common.component.ModDataComponentTypes;
import org.exodusstudio.frostbite.common.effect.ModEffects;
import org.exodusstudio.frostbite.common.entity.ModEntities;
import org.exodusstudio.frostbite.common.entity.client.AgaricMurdershroomRenderer;
import org.exodusstudio.frostbite.common.registry.CreativeModeTabRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.sound.ModSounds;
import org.slf4j.Logger;

@Mod(Frostbite.MOD_ID)
public class Frostbite {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(FrostbiteClient::initClient);
    }

    public static final String MOD_ID = "frostbite";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Frostbite(IEventBus modEventBus, ModContainer modContainer) {
        ModDataComponentTypes.DATA_COMPONENT_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        CreativeModeTabRegistry.CREATIVE_MODE_TABS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModEffects.MOB_EFFECTS.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        //Jars.register(modEventBus);

        //NeoForge.EVENT_BUS.register(this);
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.AGARIC_MURDERSHROOM.get(), AgaricMurdershroomRenderer::new);
        }
    }
}
