package org.exodusstudio.frostbite.client;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;

@EventBusSubscriber(modid = Frostbite.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ClientEvent {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerOverlayEvent(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "thermometer_overlay"),
                ThermometerOverlay::render);
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "fire_overlay"),
                FireOverlay::render);
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "decay_overlay"),
                DecayOverlay::render);
    }
}
