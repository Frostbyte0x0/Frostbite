package org.exodusstudio.frostbite.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.AgaricMurdershroomModel;
import org.exodusstudio.frostbite.common.entity.client.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.custom.illusory.IllusoryEndermanEntity;
import org.exodusstudio.frostbite.common.entity.custom.illusory.IllusoryZombieEntity;
import org.exodusstudio.frostbite.common.entity.custom.murdershrooms.AgaricMurdershroomEntity;
import org.exodusstudio.frostbite.common.network.PlayerHeartDataHandler;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

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

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                PlayerHeartDataHandler.TYPE,
                PlayerHeartDataHandler.STREAM_CODEC,
                new DirectionalPayloadHandler<>(
                        ModEventBusEvents::handleHeartData,
                        ModEventBusEvents::handleHeartData
                )
        );
    }

    public static void handleHeartData(final PlayerHeartDataHandler data, final IPayloadContext context) {
        context.enqueueWork(() -> PlayerHeartDataHandler.handle(data, context.player()));
    }
}
