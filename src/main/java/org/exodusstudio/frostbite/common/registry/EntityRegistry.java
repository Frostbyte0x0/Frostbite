package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.custom.*;
import org.exodusstudio.frostbite.common.entity.custom.bullets.RevolverBulletEntity;
import org.exodusstudio.frostbite.common.entity.custom.bullets.SniperBulletEntity;

import java.util.function.Supplier;

public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Frostbite.MOD_ID);

    public static final Supplier<EntityType<ExplodingSnowballProjectileEntity>> EXPLODING_SNOWBALL_PROJECTILE_ENTITY =
            ENTITY_TYPES.register("exploding_snowball", () -> EntityType.Builder
                    .<ExplodingSnowballProjectileEntity>of(ExplodingSnowballProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "exploding_snowball"))));

    public static final Supplier<EntityType<HardenedSnowballProjectileEntity>> HARDENED_SNOWBALL_PROJECTILE_ENTITY =
            ENTITY_TYPES.register("hardened_snowball", () -> EntityType.Builder
                    .<HardenedSnowballProjectileEntity>of(HardenedSnowballProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "hardened_snowball"))));

    public static final Supplier<EntityType<BlueHardenedSnowballProjectileEntity>> BLUE_HARDENED_SNOWBALL_PROJECTILE_ENTITY =
            ENTITY_TYPES.register("blue_hardened_snowball", () -> EntityType.Builder
                    .<BlueHardenedSnowballProjectileEntity>of(BlueHardenedSnowballProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "blue_hardened_snowball"))));

    public static final Supplier<EntityType<PackedHardenedSnowballProjectileEntity>> PACKED_HARDENED_SNOWBALL_PROJECTILE_ENTITY =
            ENTITY_TYPES.register("packed_hardened_snowball", () -> EntityType.Builder
                    .<PackedHardenedSnowballProjectileEntity>of(PackedHardenedSnowballProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "packed_hardened_snowball"))));


    public static final Supplier<EntityType<SniperBulletEntity>> SNIPER_BULLET_ENTITY =
            ENTITY_TYPES.register("sniper_bullet", () -> EntityType.Builder
                    .of(SniperBulletEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "sniper_bullet"))));
    public static final Supplier<EntityType<RevolverBulletEntity>> REVOLVER_BULLET_ENTITY =
            ENTITY_TYPES.register("revolver_bullet", () -> EntityType.Builder
                    .of(RevolverBulletEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "revolver_bullet"))));


    public static final Supplier<EntityType<HailcoilEntity>> HAILCOIL =
            ENTITY_TYPES.register("hailcoil", () -> EntityType.Builder
                    .of(HailcoilEntity::new, MobCategory.MISC)
                    .sized(0.6f, 0.6f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "hailcoil"))));


    public static final Supplier<EntityType<DrainCircleEntity>> DRAIN_CIRCLE =
            ENTITY_TYPES.register("drain_circle", () -> EntityType.Builder
                    .of(DrainCircleEntity::new, MobCategory.MISC)
                    .sized(5f, 5f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "drain_circle"))));

    public static final Supplier<EntityType<LastStandEntity>> LAST_STAND =
            ENTITY_TYPES.register("last_stand", () -> EntityType.Builder
                    .of(LastStandEntity::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "last_stand"))));


    public static final Supplier<EntityType<IceBlockEntity>> ICE_BLOCK =
            ENTITY_TYPES.register("ice_block", () -> EntityType.Builder
                    .of(IceBlockEntity::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "ice_block"))));
    public static final Supplier<EntityType<IceSpikeEntity>> ICE_SPIKE =
            ENTITY_TYPES.register("ice_spike", () -> EntityType.Builder
                    .<IceSpikeEntity>of(IceSpikeEntity::new, MobCategory.MISC)
                    .sized(1f, 1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "ice_spike"))));

    public static final Supplier<EntityType<RainFrogEntity>> RAIN_FROG =
            ENTITY_TYPES.register("rain_frog", () -> EntityType.Builder
                    .of(RainFrogEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "rain_frog"))));

    public static final Supplier<EntityType<WoollySheepEntity>> WOOLLY_SHEEP =
            ENTITY_TYPES.register("woolly_sheep", () -> EntityType.Builder
                    .of(WoollySheepEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "woolly_sheep"))));

    public static final Supplier<EntityType<LevitatingJellyfishEntity>> LEVITATING_JELLYFISH =
            ENTITY_TYPES.register("levitating_jellyfish", () -> EntityType.Builder
                    .of(LevitatingJellyfishEntity::new, MobCategory.CREATURE)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "levitating_jellyfish"))));

    public static final Supplier<EntityType<FeralWolfEntity>> FERAL_WOLF =
            ENTITY_TYPES.register("feral_wolf", () -> EntityType.Builder
                    .of(FeralWolfEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "feral_wolf"))));

    public static final Supplier<EntityType<FrozenRemnantsEntity>> FROZEN_REMNANTS =
            ENTITY_TYPES.register("frozen_remnants", () -> EntityType.Builder
                    .of(FrozenRemnantsEntity::new, MobCategory.CREATURE)
                    .sized(0.75f, 2f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "frozen_remnants"))));

    public static final Supplier<EntityType<WindCircleEntity>> WIND_CIRCLE =
            ENTITY_TYPES.register("wind_circle", () -> EntityType.Builder
                    .of(WindCircleEntity::new, MobCategory.MISC)
                    .sized(2.5f, 1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "wind_circle"))));

    public static final Supplier<EntityType<BoarEntity>> BOAR =
            ENTITY_TYPES.register("boar", () -> EntityType.Builder
                    .of(BoarEntity::new, MobCategory.AMBIENT)
                    .sized(1f, 1f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "boar"))));

    public static final Supplier<EntityType<RoamingBlizzardEntity>> ROAMING_BLIZZARD =
            ENTITY_TYPES.register("roaming_blizzard", () -> EntityType.Builder
                    .of(RoamingBlizzardEntity::new, MobCategory.MISC)
                    .sized(2.5f, 2.5f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "roaming_blizzard"))));

    public static final Supplier<EntityType<HealingCircleEntity>> HEALING_CIRCLE =
            ENTITY_TYPES.register("healing_circle", () -> EntityType.Builder
                    .of(HealingCircleEntity::new, MobCategory.MISC)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "healing_circle"))));

    public static final Supplier<EntityType<IcedSkeletonEntity>> ICED_SKELETON =
            ENTITY_TYPES.register("iced_skeleton", () -> EntityType.Builder
                    .of(IcedSkeletonEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.99F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "iced_skeleton"))));
    public static final Supplier<EntityType<IcedZombieEntity>> ICED_ZOMBIE =
            ENTITY_TYPES.register("iced_zombie", () -> EntityType.Builder
                    .of(IcedZombieEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "iced_zombie"))));
    public static final Supplier<EntityType<IcedCreeperEntity>> ICED_CREEPER =
            ENTITY_TYPES.register("iced_creeper", () -> EntityType.Builder
                    .of(IcedCreeperEntity::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.7F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "iced_creeper"))));

    public static final Supplier<EntityType<FrozenArrow>> FROZEN_ARROW =
            ENTITY_TYPES.register("frozen_arrow", () -> EntityType.Builder
                    .<FrozenArrow>of(FrozenArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "frozen_arrow"))));

    public static final Supplier<EntityType<SpecterEntity>> SPECTER =
            ENTITY_TYPES.register("specter", () -> EntityType.Builder
                    .of(SpecterEntity::new, MobCategory.AMBIENT)
                    .sized(0.8f, 2.3f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "specter"))));

    public static final Supplier<EntityType<RevenantEntity>> REVENANT =
            ENTITY_TYPES.register("revenant", () -> EntityType.Builder
                    .of(RevenantEntity::new, MobCategory.AMBIENT)
                    .sized(0.6F, 1.95F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "revenant"))));

    public static final Supplier<EntityType<BanditEntity>> BANDIT =
            ENTITY_TYPES.register("bandit", () -> EntityType.Builder
                    .of(BanditEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "bandit"))));

    public static final Supplier<EntityType<TorchEntity>> TORCH =
            ENTITY_TYPES.register("torch", () -> EntityType.Builder
                    .of(TorchEntity::new, MobCategory.AMBIENT)
                    .sized(0.6F, 1.95F)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "torch"))));

    public static final Supplier<EntityType<FireSliceEntity>> FIRE_SLICE =
            ENTITY_TYPES.register("fire_slice", () -> EntityType.Builder
                    .<FireSliceEntity>of(FireSliceEntity::new, MobCategory.AMBIENT)
                    .sized(1.5f, 0.25f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "fire_slice"))));
}
