package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.custom.animals.*;
import org.exodusstudio.frostbite.common.entity.custom.shaman.ShamanEntity;
import org.exodusstudio.frostbite.common.entity.custom.bullets.RevolverBulletEntity;
import org.exodusstudio.frostbite.common.entity.custom.bullets.SniperBulletEntity;
import org.exodusstudio.frostbite.common.entity.custom.elves.CasterElfEntity;
import org.exodusstudio.frostbite.common.entity.custom.elves.HealerElfEntity;
import org.exodusstudio.frostbite.common.entity.custom.elves.SummonerElfEntity;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.*;
import org.exodusstudio.frostbite.common.entity.custom.guards.ChiefGuardEntity;
import org.exodusstudio.frostbite.common.entity.custom.guards.GuardEntity;
import org.exodusstudio.frostbite.common.entity.custom.guards.HeavyGuardEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.*;
import org.exodusstudio.frostbite.common.entity.custom.monk.MonkEntity;
import org.exodusstudio.frostbite.common.entity.goals.TargetEntitySensor;
import org.exodusstudio.frostbite.common.entity.custom.projectiles.*;

import java.util.function.Supplier;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Frostbite.MOD_ID);

    public static final Supplier<EntityType<ExplodingSnowballProjectileEntity>> EXPLODING_SNOWBALL_PROJECTILE_ENTITY =
            ENTITY_TYPES.register("exploding_snowball", () -> EntityType.Builder
                    .<ExplodingSnowballProjectileEntity>of(ExplodingSnowballProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "exploding_snowball"))));

    public static final Supplier<EntityType<HardenedSnowballProjectileEntity>> HARDENED_SNOWBALL_PROJECTILE_ENTITY =
            ENTITY_TYPES.register("hardened_snowball", () -> EntityType.Builder
                    .<HardenedSnowballProjectileEntity>of(HardenedSnowballProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "hardened_snowball"))));

    public static final Supplier<EntityType<BlueHardenedSnowballProjectileEntity>> BLUE_HARDENED_SNOWBALL_PROJECTILE_ENTITY =
            ENTITY_TYPES.register("blue_hardened_snowball", () -> EntityType.Builder
                    .<BlueHardenedSnowballProjectileEntity>of(BlueHardenedSnowballProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "blue_hardened_snowball"))));

    public static final Supplier<EntityType<PackedHardenedSnowballProjectileEntity>> PACKED_HARDENED_SNOWBALL_PROJECTILE_ENTITY =
            ENTITY_TYPES.register("packed_hardened_snowball", () -> EntityType.Builder
                    .<PackedHardenedSnowballProjectileEntity>of(PackedHardenedSnowballProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "packed_hardened_snowball"))));


    public static final Supplier<EntityType<SniperBulletEntity>> SNIPER_BULLET_ENTITY =
            ENTITY_TYPES.register("sniper_bullet", () -> EntityType.Builder
                    .of(SniperBulletEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "sniper_bullet"))));
    public static final Supplier<EntityType<RevolverBulletEntity>> REVOLVER_BULLET_ENTITY =
            ENTITY_TYPES.register("revolver_bullet", () -> EntityType.Builder
                    .of(RevolverBulletEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "revolver_bullet"))));


    public static final Supplier<EntityType<HailcoilEntity>> HAILCOIL =
            ENTITY_TYPES.register("hailcoil", () -> EntityType.Builder
                    .of(HailcoilEntity::new, MobCategory.MISC)
                    .sized(0.6f, 0.6f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "hailcoil"))));


    public static final Supplier<EntityType<WhirlpoolEntity>> WHIRLPOOL =
            ENTITY_TYPES.register("whirlpool", () -> EntityType.Builder
                    .of(WhirlpoolEntity::new, MobCategory.MISC)
                    .sized(5f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "whirlpool"))));

    public static final Supplier<EntityType<LastStandEntity>> LAST_STAND =
            ENTITY_TYPES.register("last_stand", () -> EntityType.Builder
                    .of(LastStandEntity::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "last_stand"))));


    public static final Supplier<EntityType<IceBlockEntity>> ICE_BLOCK =
            ENTITY_TYPES.register("ice_block", () -> EntityType.Builder
                    .of(IceBlockEntity::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "ice_block"))));
    public static final Supplier<EntityType<IceSpikeEntity>> ICE_SPIKE =
            ENTITY_TYPES.register("ice_spike", () -> EntityType.Builder
                    .<IceSpikeEntity>of(IceSpikeEntity::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "ice_spike"))));

    public static final Supplier<EntityType<RainFrogEntity>> RAIN_FROG =
            ENTITY_TYPES.register("rain_frog", () -> EntityType.Builder
                    .of(RainFrogEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "rain_frog"))));

    public static final Supplier<EntityType<WoollySheepEntity>> WOOLLY_SHEEP =
            ENTITY_TYPES.register("woolly_sheep", () -> EntityType.Builder
                    .of(WoollySheepEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "woolly_sheep"))));

    public static final Supplier<EntityType<LevitatingJellyfishEntity>> LEVITATING_JELLYFISH =
            ENTITY_TYPES.register("levitating_jellyfish", () -> EntityType.Builder
                    .of(LevitatingJellyfishEntity::new, MobCategory.CREATURE)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "levitating_jellyfish"))));

    public static final Supplier<EntityType<FeralWolfEntity>> FERAL_WOLF =
            ENTITY_TYPES.register("feral_wolf", () -> EntityType.Builder
                    .of(FeralWolfEntity::new, MobCategory.MONSTER)
                    .sized(1.25f, 1.25f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "feral_wolf"))));

    public static final Supplier<EntityType<FrozenRemnantsEntity>> FROZEN_REMNANTS =
            ENTITY_TYPES.register("frozen_remnants", () -> EntityType.Builder
                    .of(FrozenRemnantsEntity::new, MobCategory.CREATURE)
                    .sized(0.75f, 2f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "frozen_remnants"))));

    public static final Supplier<EntityType<WindCircleEntity>> WIND_CIRCLE =
            ENTITY_TYPES.register("wind_circle", () -> EntityType.Builder
                    .of(WindCircleEntity::new, MobCategory.MISC)
                    .sized(2.5f, 1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "wind_circle"))));

    public static final Supplier<EntityType<BoarEntity>> BOAR =
            ENTITY_TYPES.register("boar", () -> EntityType.Builder
                    .of(BoarEntity::new, MobCategory.AMBIENT)
                    .sized(1f, 1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "boar"))));

    public static final Supplier<EntityType<RoamingBlizzardEntity>> ROAMING_BLIZZARD =
            ENTITY_TYPES.register("roaming_blizzard", () -> EntityType.Builder
                    .of(RoamingBlizzardEntity::new, MobCategory.MISC)
                    .sized(2.5f, 2.5f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "roaming_blizzard"))));

    public static final Supplier<EntityType<HealingCircleEntity>> HEALING_CIRCLE =
            ENTITY_TYPES.register("healing_circle", () -> EntityType.Builder
                    .of(HealingCircleEntity::new, MobCategory.MISC)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "healing_circle"))));

    public static final Supplier<EntityType<IcedSkeletonEntity>> ICED_SKELETON =
            ENTITY_TYPES.register("iced_skeleton", () -> EntityType.Builder
                    .of(IcedSkeletonEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "iced_skeleton"))));
    public static final Supplier<EntityType<IcedZombieEntity>> ICED_ZOMBIE =
            ENTITY_TYPES.register("iced_zombie", () -> EntityType.Builder
                    .of(IcedZombieEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "iced_zombie"))));
    public static final Supplier<EntityType<IcedCreeperEntity>> ICED_CREEPER =
            ENTITY_TYPES.register("iced_creeper", () -> EntityType.Builder
                    .of(IcedCreeperEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.7F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "iced_creeper"))));

    public static final Supplier<EntityType<FrozenArrow>> FROZEN_ARROW =
            ENTITY_TYPES.register("frozen_arrow", () -> EntityType.Builder
                    .<FrozenArrow>of(FrozenArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "frozen_arrow"))));

    public static final Supplier<EntityType<SpecterEntity>> SPECTER =
            ENTITY_TYPES.register("specter", () -> EntityType.Builder
                    .of(SpecterEntity::new, MobCategory.MONSTER)
                    .sized(0.8f, 2.3f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "specter"))));

    public static final Supplier<EntityType<RevenantEntity>> REVENANT =
            ENTITY_TYPES.register("revenant", () -> EntityType.Builder
                    .of(RevenantEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "revenant"))));

    public static final Supplier<EntityType<BanditEntity>> BANDIT =
            ENTITY_TYPES.register("bandit", () -> EntityType.Builder
                    .of(BanditEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "bandit"))));

    public static final Supplier<EntityType<TorchEntity>> TORCH =
            ENTITY_TYPES.register("torch", () -> EntityType.Builder
                    .of(TorchEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "torch"))));

    public static final Supplier<EntityType<FireSliceEntity>> FIRE_SLICE =
            ENTITY_TYPES.register("fire_slice", () -> EntityType.Builder
                    .<FireSliceEntity>of(FireSliceEntity::new, MobCategory.MISC)
                    .sized(1.5f, 0.25f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "fire_slice"))));

    public static final Supplier<EntityType<TanukiEntity>> TANUKI =
            ENTITY_TYPES.register("tanuki", () -> EntityType.Builder
                    .of(TanukiEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "tanuki"))));

    public static final Supplier<EntityType<HealerElfEntity>> HEALER_ELF =
            ENTITY_TYPES.register("healer_elf", () -> EntityType.Builder
                    .of(HealerElfEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "healer_elf"))));

    public static final Supplier<EntityType<CasterElfEntity>> CASTER_ELF =
            ENTITY_TYPES.register("caster_elf", () -> EntityType.Builder
                    .of(CasterElfEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "caster_elf"))));

    public static final Supplier<EntityType<SummonerElfEntity>> SUMMONER_ELF =
            ENTITY_TYPES.register("summoner_elf", () -> EntityType.Builder
                    .of(SummonerElfEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "summoner_elf"))));

    public static final Supplier<EntityType<BorealBearEntity>> BOREAL_BEAR =
            ENTITY_TYPES.register("boreal_bear", () -> EntityType.Builder
                    .of(BorealBearEntity::new, MobCategory.MONSTER)
                    .sized(1.2f, 1.2f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "boreal_bear"))));

    public static final Supplier<EntityType<MonkEntity>> MONK =
            ENTITY_TYPES.register("monk", () -> EntityType.Builder
                    .of(MonkEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 2f)
                    .clientTrackingRange(3)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "monk"))));

    public static final Supplier<EntityType<ShamanEntity>> SHAMAN =
            ENTITY_TYPES.register("shaman", () -> EntityType.Builder
                    .of(ShamanEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 2f)
                    .clientTrackingRange(3)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "shaman"))));

    public static final Supplier<EntityType<BigLevitatingJellyfishEntity>> BIG_LEVITATING_JELLYFISH =
            ENTITY_TYPES.register("big_levitating_jellyfish", () -> EntityType.Builder
                    .of(BigLevitatingJellyfishEntity::new, MobCategory.AMBIENT)
                    .sized(1.8f, 2.4f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "big_levitating_jellyfish"))));

    public static final Supplier<EntityType<EtherealSwordEntity>> ETHEREAL_SWORD =
            ENTITY_TYPES.register("ethereal_sword", () -> EntityType.Builder
                    .of(EtherealSwordEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "ethereal_sword"))));
    public static final Supplier<EntityType<EtherealHandsEntity>> ETHEREAL_HANDS =
            ENTITY_TYPES.register("ethereal_hands", () -> EntityType.Builder
                    .of(EtherealHandsEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "ethereal_hands"))));
    public static final Supplier<EntityType<EtherealHammerEntity>> ETHEREAL_HAMMER =
            ENTITY_TYPES.register("ethereal_hammer", () -> EntityType.Builder
                    .of(EtherealHammerEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "ethereal_hammer"))));


    public static final Supplier<EntityType<ChiefGuardEntity>> CHIEF_GUARD =
            ENTITY_TYPES.register("chief_guard", () -> EntityType.Builder
                    .of(ChiefGuardEntity::new, MobCategory.AMBIENT)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(3)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "chief_guard"))));
    public static final Supplier<EntityType<GuardEntity>> GUARD =
            ENTITY_TYPES.register("guard", () -> EntityType.Builder
                    .of(GuardEntity::new, MobCategory.AMBIENT)
                    .sized(0.6F, 1.95F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "guard"))));
    public static final Supplier<EntityType<HeavyGuardEntity>> HEAVY_GUARD =
            ENTITY_TYPES.register("heavy_guard", () -> EntityType.Builder
                    .of(HeavyGuardEntity::new, MobCategory.AMBIENT)
                    .sized(0.75F, 2.34F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "heavy_guard"))));

    // Entity Sensors
    public static final DeferredRegister<SensorType<?>> SENSOR =
            DeferredRegister.create(Registries.SENSOR_TYPE, Frostbite.MOD_ID);
    public static final DeferredHolder<SensorType<?>, SensorType<TargetEntitySensor>> TARGET_ENTITY_SENSOR =
            SENSOR.register("monk_sensor", () -> new SensorType<>(TargetEntitySensor::new));

    public static final Supplier<EntityType<CurseBallEntity>> CURSE_BALL =
            ENTITY_TYPES.register("curse_ball", () -> EntityType.Builder
                    .of(CurseBallEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "curse_ball"))));
}
