package com.frostbyte0x0.frostbite.event;

import com.frostbyte0x0.frostbite.Frostbite;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = Frostbite.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {
    @SubscribeEvent
    public static void inputEvent(InputEvent.MouseButton.Post event) {
        //Frostbite.LOGGER.debug("Button pressed: {}", event.getButton());
    }
}
