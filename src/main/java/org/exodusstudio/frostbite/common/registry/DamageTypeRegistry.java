package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import org.exodusstudio.frostbite.Frostbite;

public class DamageTypeRegistry {
    public DamageTypeRegistry() {
    }

    public static final ResourceKey<DamageType> HUNTERS_CATALYST =
            ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "hunters_catalyst"));
    public static final ResourceKey<DamageType> DELAY =
            ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "delay"));
    public static final ResourceKey<DamageType> FREEZING =
            ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "freezing"));
}
