package org.exodusstudio.frostbite.common.event;

import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.level.entity.SpawnPlacementsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.exodusstudio.frostbite.common.entity.custom.animals.*;
import org.exodusstudio.frostbite.common.entity.custom.elves.ElfEntity;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.*;
import org.exodusstudio.frostbite.common.entity.custom.guards.ChiefGuardEntity;
import org.exodusstudio.frostbite.common.entity.custom.guards.GuardEntity;
import org.exodusstudio.frostbite.common.entity.custom.guards.HeavyGuardEntity;
import org.exodusstudio.frostbite.common.entity.custom.guards.RangedGuardEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.FrozenRemnantsEntity;
import org.exodusstudio.frostbite.common.entity.custom.monk.MonkEntity;
import org.exodusstudio.frostbite.common.entity.custom.shaman.ShamanEntity;
import org.exodusstudio.frostbite.common.registry.BlockRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class ModEventBusEvents {
    public static void init() {
        registerAttributes();
        registerSpawnPlacements();
    }

    public static void registerAttributes() {
        EntityAttributeRegistry.register(EntityRegistry.WOOLLY_SHEEP, WoollySheepEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.LEVITATING_JELLYFISH, LevitatingJellyfishEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.BIG_LEVITATING_JELLYFISH, BigLevitatingJellyfishEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.ICED_SKELETON, IcedSkeletonEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.ICED_ZOMBIE, IcedZombieEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.ICED_CREEPER, IcedCreeperEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.FERAL_WOLF, FeralWolfEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.FROZEN_REMNANTS, FrozenRemnantsEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.HAILCOIL, HailcoilEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.BOAR, BoarEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.ROAMING_BLIZZARD, RoamingBlizzardEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.SPECTER, SpecterEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.REVENANT, RevenantEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.BANDIT, BanditEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.TORCH, TorchEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.TANUKI, TanukiEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.HEALER_ELF, ElfEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.CASTER_ELF, ElfEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.SUMMONER_ELF, ElfEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.BOREAL_BEAR, BorealBearEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.MONK, MonkEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.SHAMAN, ShamanEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.CHIEF_GUARD, ChiefGuardEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.GUARD, GuardEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.HEAVY_GUARD, HeavyGuardEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.RANGED_GUARD, RangedGuardEntity::createAttributes);
        EntityAttributeRegistry.register(EntityRegistry.PLAYER_ILLUSION, HeavyGuardEntity::createAttributes);
    }

    public static void registerSpawnPlacements() {
        SpawnPlacementsRegistry.register(
                EntityRegistry.ICED_SKELETON,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModEventBusEvents::checkMonsterSpawnRules);
        SpawnPlacementsRegistry.register(
                EntityRegistry.ICED_CREEPER,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModEventBusEvents::checkMonsterSpawnRules);
        SpawnPlacementsRegistry.register(
                EntityRegistry.ICED_ZOMBIE,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModEventBusEvents::checkMonsterSpawnRules);
        SpawnPlacementsRegistry.register(
                EntityRegistry.REVENANT,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModEventBusEvents::checkMonsterSpawnRules);
        SpawnPlacementsRegistry.register(
                EntityRegistry.LEVITATING_JELLYFISH,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModEventBusEvents::checkMonsterSpawnRules);
        SpawnPlacementsRegistry.register(
                EntityRegistry.BIG_LEVITATING_JELLYFISH,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModEventBusEvents::checkMonsterSpawnRules);
        SpawnPlacementsRegistry.register(
                EntityRegistry.SPECTER,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModEventBusEvents::checkMonsterSpawnRules);
        SpawnPlacementsRegistry.register(
                EntityRegistry.TORCH,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModEventBusEvents::checkMonsterSpawnRules);

        SpawnPlacementsRegistry.register(
                EntityRegistry.BANDIT,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModEventBusEvents::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(
                EntityRegistry.BOAR,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModEventBusEvents::checkAnimalSpawnRules);
        SpawnPlacementsRegistry.register(
                EntityRegistry.WOOLLY_SHEEP,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ModEventBusEvents::checkAnimalSpawnRules);
    }

    public static boolean checkMonsterSpawnRules(EntityType<? extends Mob> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        if (pos.getY() < 65) return false;
        return Monster.checkMonsterSpawnRules(type, level, spawnReason, pos, random);
    }

    public static boolean checkAnimalSpawnRules(EntityType<? extends Animal> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
        boolean brightEnoughToSpawn = EntitySpawnReason.ignoresLightRequirements(spawnReason) || isBrightEnoughToSpawn(level, pos);
        BlockState below = level.getBlockState(pos.below());

//        ServerLevel serverLevel = level.getServer().getLevel(Util.FROSTBITE_KEY);
//        int entityCount = DataHelper.getInt(serverLevel, type.getDescriptionId() + "Count");
//        if (Arrays.stream(DataHelper.getString(serverLevel, "tracked_entity_types").split(";")).noneMatch(s -> s.equals(type.getDescriptionId())))
//            DataHelper.setData(serverLevel, "tracked_entity_types", DataHelper.getString(serverLevel, "tracked_entity_types") + type.getDescriptionId() + ";");
//        if (entityCount >= serverLevel.getGameRules().get(GameRuleRegistry.MAX_FROSTBITE_ANIMAL_NATURAL_SPAWN_COUNT.get()) && random.nextFloat() < 0.1) return false;

        return (below.is(BlockTags.ANIMALS_SPAWNABLE_ON)
                || below.is(BlockRegistry.MARBLE)
                || below.is(Blocks.SNOW_BLOCK)
                || below.is(Blocks.ICE)
                || below.is(Blocks.PACKED_ICE)
                || below.is(BlockRegistry.MISTY_GRASS)
                || below.is(BlockRegistry.SNOWY_MISTY_GRASS)
                || below.is(Blocks.PACKED_ICE)
                || below.is(Blocks.BLUE_ICE)
                || below.is(Blocks.SNOW)) && brightEnoughToSpawn;
    }

    protected static boolean isBrightEnoughToSpawn(BlockAndLightGetter level, BlockPos pos) {
        return level.getRawBrightness(pos, 0) > 8;
    }
}
