package org.exodusstudio.frostbite;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.exodusstudio.frostbite.client.FrostbiteClient;
import org.exodusstudio.frostbite.common.entity.client.renderers.IceSpikeRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.RainFrogRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.WoollySheepRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.bullet.RevolverBulletRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.bullet.SniperBulletRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.murdershrooms.AgaricMurdershroomRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.murdershrooms.CrystalMurdershroomRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.murdershrooms.DecayingMurdershroomRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.murdershrooms.FloralMurdershroomRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.murdershrooms.LightMurdershroomRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.murdershrooms.MossyMurdershroomRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.murdershrooms.PineMurdershroomRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.GenericEntityRenderer;
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
            EntityRenderers.register(EntityRegistry.SNIPER_BULLET_ENTITY.get(), SniperBulletRenderer::new);
            EntityRenderers.register(EntityRegistry.REVOLVER_BULLET_ENTITY.get(), RevolverBulletRenderer::new);
            EntityRenderers.register(EntityRegistry.RAIN_FROG.get(), RainFrogRenderer::new);
            EntityRenderers.register(EntityRegistry.WOOLLY_SHEEP.get(), WoollySheepRenderer::new);
            EntityRenderers.register(EntityRegistry.AGARIC_MURDERSHROOM.get(), AgaricMurdershroomRenderer::new);
            EntityRenderers.register(EntityRegistry.CRYSTAL_MURDERSHROOM.get(), CrystalMurdershroomRenderer::new);
            EntityRenderers.register(EntityRegistry.DECAYING_MURDERSHROOM.get(), DecayingMurdershroomRenderer::new);
            EntityRenderers.register(EntityRegistry.FLORAL_MURDERSHROOM.get(), FloralMurdershroomRenderer::new);
            EntityRenderers.register(EntityRegistry.LIGHT_MURDERSHROOM.get(), LightMurdershroomRenderer::new);
            EntityRenderers.register(EntityRegistry.MOSSY_MURDERSHROOM.get(), MossyMurdershroomRenderer::new);
            EntityRenderers.register(EntityRegistry.PINE_MURDERSHROOM.get(), PineMurdershroomRenderer::new);
            EntityRenderers.register(EntityRegistry.ILLUSORY_ZOMBIE.get(), ZombieRenderer::new);
            EntityRenderers.register(EntityRegistry.ILLUSORY_ENDERMAN.get(), EndermanRenderer::new);
            EntityRenderers.register(EntityRegistry.SPORE_CLOUD.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.DRAIN_CIRCLE.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.HAILCOIL.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.LAST_STAND.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.ICE_BLOCK.get(), FallingBlockRenderer::new);
            EntityRenderers.register(EntityRegistry.ICE_SPIKE.get(), IceSpikeRenderer::new);
            EntityRenderers.register(EntityRegistry.EXPLODING_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.BLUE_HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.PACKED_HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            ModItemProperties.addCustomItemProperties();
        }
    }
}
