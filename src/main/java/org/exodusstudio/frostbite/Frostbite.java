package org.exodusstudio.frostbite;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EndermanRenderer;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.exodusstudio.frostbite.client.FrostbiteClient;
import org.exodusstudio.frostbite.common.entity.client.AgaricMurdershroomRenderer;
import org.exodusstudio.frostbite.common.entity.client.GenericEntityRenderer;
import org.exodusstudio.frostbite.common.item.custom.alchemy.Jars;
import org.exodusstudio.frostbite.common.registry.*;
import org.exodusstudio.frostbite.common.temperature.TemperatureValues;
import org.exodusstudio.frostbite.common.util.ModItemProperties;
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
        DataComponentTypeRegistry.DATA_COMPONENT_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        CreativeModeTabRegistry.CREATIVE_MODE_TABS.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        EffectRegistry.MOB_EFFECTS.register(modEventBus);
        SoundRegistry.SOUND_EVENTS.register(modEventBus);
        AttachmentTypeRegistry.ATTACHMENT_TYPES.register(modEventBus);
        ParticleRegistry.PARTICLE_TYPES.register(modEventBus);
        Jars.JARS.register(modEventBus);

        TemperatureValues.addTemperatures();

        //NeoForge.EVENT_BUS.register(this);
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(EntityRegistry.AGARIC_MURDERSHROOM.get(), AgaricMurdershroomRenderer::new);
            EntityRenderers.register(EntityRegistry.ILLUSORY_ZOMBIE.get(), ZombieRenderer::new);
            EntityRenderers.register(EntityRegistry.ILLUSORY_ENDERMAN.get(), EndermanRenderer::new);
            EntityRenderers.register(EntityRegistry.SPORE_CLOUD.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.DRAIN_CIRCLE.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.EXPLODING_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.BLUE_HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.PACKED_HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            ModItemProperties.addCustomItemProperties();
        }
    }
}
