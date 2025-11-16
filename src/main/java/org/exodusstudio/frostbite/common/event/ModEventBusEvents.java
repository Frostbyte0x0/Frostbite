package org.exodusstudio.frostbite.common.event;

import net.minecraft.client.model.CreeperModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.ZombieModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.renderers.LodestarRenderer;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.*;
import org.exodusstudio.frostbite.common.entity.client.models.bullet.RevolverBulletModel;
import org.exodusstudio.frostbite.common.entity.client.models.bullet.SniperBulletModel;
import org.exodusstudio.frostbite.common.entity.custom.*;
import org.exodusstudio.frostbite.common.entity.custom.elves.ElfEntity;
import org.exodusstudio.frostbite.common.network.ServerPayloadHandler;
import org.exodusstudio.frostbite.common.network.StaffPayload;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

@EventBusSubscriber(modid = Frostbite.MOD_ID)
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
        event.registerLayerDefinition(ModModelLayers.BOAR, BoarModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BOREAL_BEAR, BorealBearModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.LODESTAR_CAGE, LodestarRenderer::createCageLayer);
        event.registerLayerDefinition(ModModelLayers.SUMMONER_ELF, SummonerElfModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CASTER_ELF, CasterElfModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HEALER_ELF, HealerElfModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.TANUKI, TanukiModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.FIRE_SLICE, FireSliceModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.TORCH, TorchModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BANDIT, BanditModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.REVENANT, RevenantModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.LODESTAR_EYE, LodestarRenderer::createEyeLayer);
        event.registerLayerDefinition(ModModelLayers.LODESTAR_SHELL, LodestarRenderer::createShellLayer);
        event.registerLayerDefinition(ModModelLayers.ICED_CREEPER, () -> CreeperModel.createBodyLayer(CubeDeformation.NONE));
        event.registerLayerDefinition(ModModelLayers.ICED_ZOMBIE,
                () -> LayerDefinition.create(ZombieModel.createMesh(CubeDeformation.NONE, 0), 64, 64));
        event.registerLayerDefinition(ModModelLayers.ICED_SKELETON, SkeletonModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SPECTER, SpecterModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.RAIN_FROG.get(), RainFrogEntity.createAttributes().build());
        event.put(EntityRegistry.WOOLLY_SHEEP.get(), WoollySheepEntity.createAttributes().build());
        event.put(EntityRegistry.LEVITATING_JELLYFISH.get(), LevitatingJellyfishEntity.createAttributes().build());
        event.put(EntityRegistry.ICED_SKELETON.get(), IcedSkeletonEntity.createAttributes().build());
        event.put(EntityRegistry.ICED_ZOMBIE.get(), IcedZombieEntity.createAttributes().build());
        event.put(EntityRegistry.ICED_CREEPER.get(), IcedCreeperEntity.createAttributes().build());
        event.put(EntityRegistry.FERAL_WOLF.get(), FeralWolfEntity.createAttributes().build());
        event.put(EntityRegistry.FROZEN_REMNANTS.get(), FrozenRemnantsEntity.createAttributes().build());
        event.put(EntityRegistry.HAILCOIL.get(), HailcoilEntity.createAttributes().build());
        event.put(EntityRegistry.BOAR.get(), BoarEntity.createAttributes().build());
        event.put(EntityRegistry.ROAMING_BLIZZARD.get(), RoamingBlizzardEntity.createAttributes().build());
        event.put(EntityRegistry.SPECTER.get(), SpecterEntity.createAttributes().build());
        event.put(EntityRegistry.REVENANT.get(), RevenantEntity.createAttributes().build());
        event.put(EntityRegistry.BANDIT.get(), BanditEntity.createAttributes().build());
        event.put(EntityRegistry.TORCH.get(), TorchEntity.createAttributes().build());
        event.put(EntityRegistry.TANUKI.get(), TanukiEntity.createAttributes().build());
        event.put(EntityRegistry.HEALER_ELF.get(), ElfEntity.createAttributes().build());
        event.put(EntityRegistry.CASTER_ELF.get(), ElfEntity.createAttributes().build());
        event.put(EntityRegistry.SUMMONER_ELF.get(), ElfEntity.createAttributes().build());
        event.put(EntityRegistry.BOREAL_BEAR.get(), BorealBearEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playBidirectional(
                StaffPayload.TYPE,
                StaffPayload.STREAM_CODEC,
                ServerPayloadHandler::handleDataOnMain
        );
    }

    @SubscribeEvent
    public static void register(RegisterClientPayloadHandlersEvent event) {
        event.register(
                StaffPayload.TYPE,
                ServerPayloadHandler::handleDataOnMain
        );
    }

    @SubscribeEvent
    public static void register(RegisterSpawnPlacementsEvent event) {
        event.register(
                EntityRegistry.ICED_SKELETON.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(
                EntityRegistry.ICED_CREEPER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(
                EntityRegistry.ICED_ZOMBIE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(
                EntityRegistry.REVENANT.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
