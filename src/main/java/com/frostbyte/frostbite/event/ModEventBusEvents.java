package com.frostbyte.frostbite.event;

import com.frostbyte.frostbite.Frostbite;
import com.frostbyte.frostbite.entity.ModEntities;
import com.frostbyte.frostbite.entity.client.AgaricMurdershroomModel;
import com.frostbyte.frostbite.entity.client.ModModelLayers;
import com.frostbyte.frostbite.entity.custom.murdershrooms.AgaricMurdershroomEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = Frostbite.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.AGARIC_MURDERSHROOM, AgaricMurdershroomModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.AGARIC_MURDERSHROOM.get(), AgaricMurdershroomEntity.createAttributes().build());
    }
}
