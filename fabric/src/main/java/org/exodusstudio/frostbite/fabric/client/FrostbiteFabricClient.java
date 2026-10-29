package org.exodusstudio.frostbite.fabric.client;

import dev.architectury.registry.client.gui.MenuScreenRegistry;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.particle.ParticleProviderRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleGroupRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.gui.WeavingScreen;
import org.exodusstudio.frostbite.client.gui.scribing.ApplyingScreen;
import org.exodusstudio.frostbite.client.gui.scribing.CombiningScreen;
import org.exodusstudio.frostbite.client.gui.scribing.DecipheringScreen;
import org.exodusstudio.frostbite.client.overlays.*;
import org.exodusstudio.frostbite.common.event.ClientBusEvents;
import org.exodusstudio.frostbite.common.event.ClientEvents;
import org.exodusstudio.frostbite.common.particle.*;
import org.exodusstudio.frostbite.common.registry.MenuTypeRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;

public final class FrostbiteFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientBusEvents.init();
        ClientBusEvents.registerRenderers();
        ClientBusEvents.registerLayers(EntityModelLayerRegistry::register);

        registerKeyMappings();
        registerOverlays();
        registerParticles();
        registerScreens();
    }

    private void registerKeyMappings() {
        KeyMapping.Category.register(org.exodusstudio.frostbite.common.registry.KeyMappingRegistry.FROSTBITE.id());
        KeyMappingRegistry.register(org.exodusstudio.frostbite.common.registry.KeyMappingRegistry.CODEX);
        KeyMappingRegistry.register(org.exodusstudio.frostbite.common.registry.KeyMappingRegistry.PARRY);
    }

    private void registerOverlays() {
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "thermometer_overlay"),
                ThermometerOverlay::render);
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "fire_overlay"),
                FireOverlay::render);
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "combo_overlay"),
                ComboOverlay::render);
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "rage_overlay"),
                RageOverlay::render);
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "lining_bar"),
                LiningBarOverlay::render);
        HudElementRegistry.addFirst(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "thermal_lens_overlay"),
                ThermalLensOverlay::render);
        HudElementRegistry.attachElementAfter(VanillaHudElements.HOTBAR, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "combo_charge"),
                (graphics, deltaTracker) -> ClientEvents.comboCharge(graphics));
    }

    private void registerParticles() {
        ParticleProviderRegistry.register(ParticleRegistry.WHIRLPOOL_PARTICLE.get(), WhirlpoolParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleRegistry.SHOCKWAVE_PARTICLE.get(), ShockwaveParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleRegistry.CONFETTI_PARTICLE.get(), ConfettiParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleRegistry.EXPANDING_CIRCLE_PARTICLE.get(), ExpandingCircleParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleRegistry.BUTTERFLY_PARTICLE.get(), ButterflyParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleRegistry.SWIRLING_LEAF_PARTICLE.get(), SwirlingLeafParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleRegistry.SNOWFLAKE_PARTICLE.get(), SnowflakeParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleRegistry.ICY_BREATH_PARTICLE.get(), IcyBreathParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleRegistry.ROAMING_BLIZZARD_PARTICLE.get(), RoamingBlizzardParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleRegistry.DEBUG_PARTICLE.get(), DebugParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleRegistry.HEAL_PARTICLE.get(), HealParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleRegistry.AMBIENT_SNOW_PARTICLE.get(), AmbientSnowParticle.Provider::new);
        ParticleProviderRegistry.register(ParticleRegistry.DAMAGE_PARTICLE.get(), new DamageParticle.Provider());
        ParticleProviderRegistry.register(ParticleRegistry.SLASH_PARTICLE.get(), SlashParticle.Provider::new);

        ParticleGroupRegistry.register(DamageParticle.RENDER_TYPE, TextParticleGroup::new);
    }

    private void registerScreens() {
        MenuScreenRegistry.registerScreenFactory(MenuTypeRegistry.WEAVING_MENU.get(), WeavingScreen::new);
        MenuScreenRegistry.registerScreenFactory(MenuTypeRegistry.DECIPHERING_MENU.get(), DecipheringScreen::new);
        MenuScreenRegistry.registerScreenFactory(MenuTypeRegistry.COMBINING_MENU.get(), CombiningScreen::new);
        MenuScreenRegistry.registerScreenFactory(MenuTypeRegistry.APPLYING_MENU.get(), ApplyingScreen::new);
    }
}
