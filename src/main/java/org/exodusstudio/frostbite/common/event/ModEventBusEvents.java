package org.exodusstudio.frostbite.common.event;

import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.custom.illusory.IllusoryEndermanEntity;
import org.exodusstudio.frostbite.common.entity.custom.illusory.IllusoryZombieEntity;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.entity.client.AgaricMurdershroomModel;
import org.exodusstudio.frostbite.common.entity.client.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.custom.murdershrooms.AgaricMurdershroomEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import static org.exodusstudio.frostbite.common.registry.RegistryRegistry.JAR_REGISTRY;

@EventBusSubscriber(modid = Frostbite.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.AGARIC_MURDERSHROOM, AgaricMurdershroomModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.AGARIC_MURDERSHROOM.get(), AgaricMurdershroomEntity.createAttributes().build());
        event.put(EntityRegistry.ILLUSORY_ZOMBIE.get(), IllusoryZombieEntity.createAttributes().build());
        event.put(EntityRegistry.ILLUSORY_ENDERMAN.get(), IllusoryEndermanEntity.createAttributes().build());
    }

    @SubscribeEvent
    static void registerRegistries(NewRegistryEvent event) {
        event.register(JAR_REGISTRY);
    }
}
