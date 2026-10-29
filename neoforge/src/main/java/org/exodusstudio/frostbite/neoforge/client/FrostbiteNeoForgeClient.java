package org.exodusstudio.frostbite.neoforge.client;

import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.gui.WeavingScreen;
import org.exodusstudio.frostbite.client.gui.scribing.ApplyingScreen;
import org.exodusstudio.frostbite.client.gui.scribing.CombiningScreen;
import org.exodusstudio.frostbite.client.gui.scribing.DecipheringScreen;
import org.exodusstudio.frostbite.client.overlays.*;
import org.exodusstudio.frostbite.common.event.ClientBusEvents;
import org.exodusstudio.frostbite.common.event.ClientEvents;
import org.exodusstudio.frostbite.common.particle.*;
import org.exodusstudio.frostbite.common.registry.KeyMappingRegistry;
import org.exodusstudio.frostbite.common.registry.MenuTypeRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;

@EventBusSubscriber(modid = Frostbite.MOD_ID, value = Dist.CLIENT)
public class FrostbiteNeoForgeClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientBusEvents.init();
            ClientBusEvents.registerRenderers();
        });
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ClientBusEvents.registerLayers(event::registerLayerDefinition);
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(KeyMappingRegistry.CODEX);
        event.register(KeyMappingRegistry.PARRY);
        event.registerCategory(KeyMappingRegistry.FROSTBITE);
    }

    @SubscribeEvent
    public static void registerOverlayEvent(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "thermometer_overlay"),
                ThermometerOverlay::render);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "fire_overlay"),
                FireOverlay::render);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "combo_overlay"),
                ComboOverlay::render);
//        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "gun_overlay"),
//                GunOverlay::render);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "rage_overlay"),
                RageOverlay::render);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "lining_bar"),
                LiningBarOverlay::render);
        event.registerBelowAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "thermal_lens_overlay"),
                ThermalLensOverlay::render);
    }

    @SubscribeEvent
    public static void comboCharge(RenderGuiLayerEvent.Post event) {
        if (event.getName().equals(Identifier.withDefaultNamespace("hotbar"))) {
            ClientEvents.comboCharge(event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.WHIRLPOOL_PARTICLE.get(), WhirlpoolParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.SHOCKWAVE_PARTICLE.get(), ShockwaveParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.CONFETTI_PARTICLE.get(), ConfettiParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.EXPANDING_CIRCLE_PARTICLE.get(), ExpandingCircleParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.BUTTERFLY_PARTICLE.get(), ButterflyParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.SWIRLING_LEAF_PARTICLE.get(), SwirlingLeafParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.SNOWFLAKE_PARTICLE.get(), SnowflakeParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.ICY_BREATH_PARTICLE.get(), IcyBreathParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.ROAMING_BLIZZARD_PARTICLE.get(), RoamingBlizzardParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.DEBUG_PARTICLE.get(), DebugParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.HEAL_PARTICLE.get(), HealParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.AMBIENT_SNOW_PARTICLE.get(), AmbientSnowParticle.Provider::new);
        event.registerSpecial(ParticleRegistry.DAMAGE_PARTICLE.get(), new DamageParticle.Provider());
        event.registerSpriteSet(ParticleRegistry.SLASH_PARTICLE.get(), SlashParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerParticleGroups(RegisterParticleGroupsEvent event) {
        event.register(DamageParticle.RENDER_TYPE, TextParticleGroup::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypeRegistry.WEAVING_MENU.get(), WeavingScreen::new);
        event.register(MenuTypeRegistry.DECIPHERING_MENU.get(), DecipheringScreen::new);
        event.register(MenuTypeRegistry.COMBINING_MENU.get(), CombiningScreen::new);
        event.register(MenuTypeRegistry.APPLYING_MENU.get(), ApplyingScreen::new);
    }
}
