package org.exodusstudio.frostbite.event;

import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.entity.ModEntities;
import org.exodusstudio.frostbite.entity.client.AgaricMurdershroomModel;
import org.exodusstudio.frostbite.entity.client.ModModelLayers;
import org.exodusstudio.frostbite.entity.custom.murdershrooms.AgaricMurdershroomEntity;
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
