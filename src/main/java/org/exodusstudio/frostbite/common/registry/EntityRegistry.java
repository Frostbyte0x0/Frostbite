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
import org.exodusstudio.frostbite.common.entity.custom.illusory.IllusoryEndermanEntity;
import org.exodusstudio.frostbite.common.entity.custom.murdershrooms.*;
import org.exodusstudio.frostbite.common.entity.custom.illusory.IllusoryZombieEntity;

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


    public static final Supplier<EntityType<SporeCloudEntity>> SPORE_CLOUD =
            ENTITY_TYPES.register("spore_cloud", () -> EntityType.Builder
                    .<SporeCloudEntity>of(SporeCloudEntity::new, MobCategory.MISC)
                    .sized(5f, 5f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "spore_cloud"))));


    public static final Supplier<EntityType<IllusoryZombieEntity>> ILLUSORY_ZOMBIE =
            ENTITY_TYPES.register("illusory_zombie", () -> EntityType.Builder
                    .<IllusoryZombieEntity>of(IllusoryZombieEntity::new, MobCategory.MONSTER)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "illusory_zombie"))));

    public static final Supplier<EntityType<IllusoryEndermanEntity>> ILLUSORY_ENDERMAN =
            ENTITY_TYPES.register("illusory_enderman", () -> EntityType.Builder
                    .of(IllusoryEndermanEntity::new, MobCategory.MONSTER)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "illusory_enderman"))));


    public static final Supplier<EntityType<AgaricMurdershroomEntity>> AGARIC_MURDERSHROOM =
            ENTITY_TYPES.register("agaric_murdershroom", () -> EntityType.Builder
                    .of(AgaricMurdershroomEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "agaric_murdershroom"))));

    public static final Supplier<EntityType<CrystalMurdershroomEntity>> CRYSTAL_MURDERSHROOM =
            ENTITY_TYPES.register("crystal_murdershroom", () -> EntityType.Builder
                    .of(CrystalMurdershroomEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "crystal_murdershroom"))));

    public static final Supplier<EntityType<DecayingMurdershroomEntity>> DECAYING_MURDERSHROOM =
            ENTITY_TYPES.register("decaying_murdershroom", () -> EntityType.Builder
                    .of(DecayingMurdershroomEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "decaying_murdershroom"))));

    public static final Supplier<EntityType<FloralMurdershroomEntity>> FLORAL_MURDERSHROOM =
            ENTITY_TYPES.register("floral_murdershroom", () -> EntityType.Builder
                    .of(FloralMurdershroomEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "floral_murdershroom"))));

    public static final Supplier<EntityType<LightMurdershroomEntity>> LIGHT_MURDERSHROOM =
            ENTITY_TYPES.register("light_murdershroom", () -> EntityType.Builder
                    .of(LightMurdershroomEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "light_murdershroom"))));

    public static final Supplier<EntityType<MossyMurdershroomEntity>> MOSSY_MURDERSHROOM =
            ENTITY_TYPES.register("mossy_murdershroom", () -> EntityType.Builder
                    .of(MossyMurdershroomEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "mossy_murdershroom"))));

    public static final Supplier<EntityType<PineMurdershroomEntity>> PINE_MURDERSHROOM =
            ENTITY_TYPES.register("pine_murdershroom", () -> EntityType.Builder
                    .of(PineMurdershroomEntity::new, MobCategory.MONSTER)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "pine_murdershroom"))));


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
                    .of(FeralWolfEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 0.75f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "feral_wolf"))));

    public static final Supplier<EntityType<FrozenRemnantsEntity>> FROZEN_REMNANTS =
            ENTITY_TYPES.register("frozen_remnants", () -> EntityType.Builder
                    .of(FrozenRemnantsEntity::new, MobCategory.AMBIENT)
                    .sized(0.75f, 2f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "frozen_remnants"))));
}
