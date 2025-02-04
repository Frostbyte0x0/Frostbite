package org.exodusstudio.frostbite.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.exodusstudio.frostbite.Frostbite;

import static org.exodusstudio.frostbite.common.temperature.TemperatureCalculator.updateTemperature;

@EventBusSubscriber(modid = Frostbite.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {
    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Pre event) {
        updateTemperature(event.getEntity());
    }
}
