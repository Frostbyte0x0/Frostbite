package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.item.custom.alchemy.Jar;

public class RegistryRegistry {
    public static final ResourceKey<Registry<Jar>> JAR_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "jars"));
    public static final Registry<Jar> JAR_REGISTRY = new RegistryBuilder<>(JAR_REGISTRY_KEY)
            // If you want to enable integer id syncing, for networking.
            // These should only be used in networking contexts, for example in packets or purely networking-related NBT data.
            .sync(true)
            .defaultKey(ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "empty"))
            .maxId(256)
            .create();
}
