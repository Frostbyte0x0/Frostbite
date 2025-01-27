package com.frostbyte0x0.frostbite.entity;

import com.frostbyte0x0.frostbite.Frostbite;
import com.frostbyte0x0.frostbite.entity.custom.*;
import com.frostbyte0x0.frostbite.entity.custom.bullets.SniperBulletEntity;
import com.frostbyte0x0.frostbite.entity.custom.murdershrooms.AgaricMurdershroomEntity;
import com.google.common.base.Suppliers;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Frostbite.MOD_ID);

    public static final Supplier<EntityType<? extends ExplodingSnowballProjectileEntity>> EXPLODING_SNOWBALL_PROJECTILE_ENTITY =
            ENTITY_TYPES.register("exploding_snowball", Suppliers.memoize(() -> EntityType.Builder.
                            <ExplodingSnowballProjectileEntity>of(ExplodingSnowballProjectileEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "exploding_snowball")))));

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
                    .<SniperBulletEntity>of(SniperBulletEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "packed_hardened_snowball"))));

//
//    public static final Supplier<EntityType<SporeCloudEntity>> SPORE_CLOUD =
//            ENTITY_TYPES.register("spore_cloud", () -> EntityType.Builder
//                    .<SporeCloudEntity>of(SporeCloudEntity::new, MobCategory.MISC)
//                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
//                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "spore_cloud"))));


    public static final Supplier<EntityType<AgaricMurdershroomEntity>> AGARIC_MURDERSHROOM =
            ENTITY_TYPES.register("agaric_murdershroom", () -> EntityType.Builder
                    .of(AgaricMurdershroomEntity::new, MobCategory.MONSTER)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE,
                            ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "agaric_murdershroom"))));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
