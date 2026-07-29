package org.exodusstudio.frostbite.common.event;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.custom.animals.*;
import org.exodusstudio.frostbite.common.entity.custom.elves.ElfEntity;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.*;
import org.exodusstudio.frostbite.common.entity.custom.guards.ChiefGuardEntity;
import org.exodusstudio.frostbite.common.entity.custom.guards.GuardEntity;
import org.exodusstudio.frostbite.common.entity.custom.guards.HeavyGuardEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.FrozenRemnantsEntity;
import org.exodusstudio.frostbite.common.entity.custom.monk.MonkEntity;
import org.exodusstudio.frostbite.common.entity.custom.shaman.ShamanEntity;
import org.exodusstudio.frostbite.common.network.ServerPayloadHandler;
import org.exodusstudio.frostbite.common.network.StaffPayload;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.KeyMappingRegistry;

@EventBusSubscriber(modid = Frostbite.MOD_ID)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityRegistry.RAIN_FROG.get(), RainFrogEntity.createAttributes().build());
        event.put(EntityRegistry.WOOLLY_SHEEP.get(), WoollySheepEntity.createAttributes().build());
        event.put(EntityRegistry.LEVITATING_JELLYFISH.get(), LevitatingJellyfishEntity.createAttributes().build());
        event.put(EntityRegistry.BIG_LEVITATING_JELLYFISH.get(), BigLevitatingJellyfishEntity.createAttributes().build());
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
        event.put(EntityRegistry.MONK.get(), MonkEntity.createAttributes().build());
        event.put(EntityRegistry.SHAMAN.get(), ShamanEntity.createAttributes().build());
        event.put(EntityRegistry.CHIEF_GUARD.get(), ChiefGuardEntity.createAttributes().build());
        event.put(EntityRegistry.GUARD.get(), GuardEntity.createAttributes().build());
        event.put(EntityRegistry.HEAVY_GUARD.get(), HeavyGuardEntity.createAttributes().build());
        event.put(EntityRegistry.PLAYER_ILLUSION.get(), HeavyGuardEntity.createAttributes().build());
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
    public static void register(RegisterKeyMappingsEvent event) {
        event.register(KeyMappingRegistry.CODEX);
        event.registerCategory(KeyMappingRegistry.FROSTBITE);
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
