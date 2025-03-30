package org.exodusstudio.frostbite.common.event;

import net.minecraft.client.gui.Gui;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerHeartTypeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;

import static org.exodusstudio.frostbite.common.temperature.TemperatureCalculator.updateTemperature;

@EventBusSubscriber(modid = Frostbite.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {
    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Pre event) {
        //updateTemperature(event.getEntity());
    }

    @SubscribeEvent
    public static void playerHeartEvent(PlayerHeartTypeEvent event) {
        if (event.getEntity().hasEffect(EffectRegistry.DECAY)) {
            event.setType(Gui.HeartType.valueOf("frostbite_decaying_heart"));
        }

    }
}
