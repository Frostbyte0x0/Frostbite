package org.exodusstudio.frostbite.common.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.*;
import org.exodusstudio.frostbite.common.entity.client.models.bullet.RevolverBulletModel;
import org.exodusstudio.frostbite.common.entity.client.models.bullet.SniperBulletModel;
import org.exodusstudio.frostbite.common.entity.custom.*;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

@EventBusSubscriber(modid = Frostbite.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.SNIPER_BULLET, SniperBulletModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.REVOLVER_BULLET, RevolverBulletModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.RAIN_FROG, RainFrogModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ICE_SPIKE, IceSpikeModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.WOOLLY_SHEEP, WoollySheepModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.WOOLLY_SHEEP_FUR, WoollySheepFurModel::createFurLayer);
        event.registerLayerDefinition(ModModelLayers.WOOLLY_SHEEP_BABY, () -> WoollySheepModel.createBodyLayer().apply(WoollySheepModel.BABY_TRANSFORMER));
        event.registerLayerDefinition(ModModelLayers.WOOLLY_SHEEP_BABY_FUR, () -> WoollySheepFurModel.createFurLayer().apply(WoollySheepModel.BABY_TRANSFORMER));
        event.registerLayerDefinition(ModModelLayers.LEVITATING_JELLYFISH, LevitatingJellyfishModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.FROZEN_REMNANTS, FrozenRemnantsModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.FERAL_WOLF, FeralWolfModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.WIND_CIRCLE, WindCircleModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.RAIN_FROG.get(), RainFrogEntity.createAttributes().build());
        event.put(EntityRegistry.WOOLLY_SHEEP.get(), WoollySheepEntity.createAttributes().build());
        event.put(EntityRegistry.LEVITATING_JELLYFISH.get(), LevitatingJellyfishEntity.createAttributes().build());
        event.put(EntityRegistry.FERAL_WOLF.get(), FeralWolfEntity.createAttributes().build());
        event.put(EntityRegistry.FROZEN_REMNANTS.get(), FrozenRemnantsEntity.createAttributes().build());
        event.put(EntityRegistry.HAILCOIL.get(), HailcoilEntity.createAttributes().build());
        //event.put(EntityRegistry.WIND_CIRCLE.get(), WindCircleEntity.createAttributes().build());
    }
}
