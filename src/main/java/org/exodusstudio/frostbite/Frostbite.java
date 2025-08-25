package org.exodusstudio.frostbite;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.exodusstudio.frostbite.client.FrostbiteClient;
import org.exodusstudio.frostbite.common.block.HeaterStorage;
import org.exodusstudio.frostbite.common.block.renderers.LodestarRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.*;
import org.exodusstudio.frostbite.common.entity.client.renderers.bullet.RevolverBulletRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.bullet.SniperBulletRenderer;
import org.exodusstudio.frostbite.common.item.lining.SavedLinings;
import org.exodusstudio.frostbite.common.registry.*;
import org.exodusstudio.frostbite.common.temperature.SavedTemperatures;
import org.exodusstudio.frostbite.common.util.ModItemProperties;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(Frostbite.MOD_ID)
public class Frostbite {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(FrostbiteClient::initClient);
    }

    public static final String MOD_ID = "frostbite";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static SavedTemperatures savedTemperatures = SavedTemperatures.init();
    public static List<HeaterStorage> savedHeaters = new ArrayList<>();
    public static List<HeaterStorage> heatersToRemove = new ArrayList<>();
    public static SavedLinings savedLinings = new SavedLinings();
    public static boolean shouldShowLining = false;
    public static BlockPos frostbiteSpawnPoint = BlockPos.ZERO;
    public static BlockPos overworldSpawnPoint = BlockPos.ZERO;

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
        MenuTypeRegistry.MENU_TYPES.register(modEventBus);
        ConsumeEffectRegistry.CONSUME_EFFECT_TYPES.register(modEventBus);
        FoliagePlacerRegistry.FOLIAGE_PLACER_TYPES.register(modEventBus);
        StructureRegistry.STRUCTURES.register(modEventBus);
        BlockEntityRegistry.BLOCK_ENTITY_TYPES.register(modEventBus);
        GameRuleRegistry.register();

        // TemperatureValues.addTemperatures();

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
            EntityRenderers.register(EntityRegistry.DRAIN_CIRCLE.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.HAILCOIL.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.LAST_STAND.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.ICE_BLOCK.get(), FallingBlockRenderer::new);
            EntityRenderers.register(EntityRegistry.ICE_SPIKE.get(), IceSpikeRenderer::new);
            EntityRenderers.register(EntityRegistry.EXPLODING_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.BLUE_HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.PACKED_HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.LEVITATING_JELLYFISH.get(), LevitatingJellyfishRenderer::new);
            EntityRenderers.register(EntityRegistry.FERAL_WOLF.get(), FeralWolfRenderer::new);
            EntityRenderers.register(EntityRegistry.FROZEN_REMNANTS.get(), FrozenRemnantsRenderer::new);
            EntityRenderers.register(EntityRegistry.WIND_CIRCLE.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.BOAR.get(), BoarRenderer::new);

            BlockEntityRenderers.register(BlockEntityRegistry.LODESTAR.get(), LodestarRenderer::new);
            ModItemProperties.addCustomItemProperties();
        }
    }
}
