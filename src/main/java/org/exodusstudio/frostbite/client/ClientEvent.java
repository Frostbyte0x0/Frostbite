package org.exodusstudio.frostbite.client;

import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.gui.WeavingScreen;
import org.exodusstudio.frostbite.client.overlays.*;
import org.exodusstudio.frostbite.common.particle.*;
import org.exodusstudio.frostbite.common.registry.MenuTypeRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;

@EventBusSubscriber(modid = Frostbite.MOD_ID)
public class ClientEvent {
    @SubscribeEvent
    public static void registerOverlayEvent(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "thermometer_overlay"),
                ThermometerOverlay::render);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "fire_overlay"),
                FireOverlay::render);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "gun_overlay"),
                GunOverlay::render);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "rage_overlay"),
                RageOverlay::render);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "lining_bar"),
                LiningBarOverlay::render);
        event.registerBelowAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "thermal_lens_overlay"),
                ThermalLensOverlay::render);
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
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypeRegistry.WEAVING_MENU.get(), WeavingScreen::new);
    }
}
