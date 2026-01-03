package org.exodusstudio.frostbite.common.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;

import java.util.Optional;

public class MemoryModuleTypeRegistry {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES =
            DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, Frostbite.MOD_ID);

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Integer>> ATTACK_COOLDOWN =
            MEMORY_MODULE_TYPES.register("attack_cooldown",
                    () -> new MemoryModuleType<>(Optional.of(Codec.INT)));

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Integer>> SUMMON_COOLDOWN =
            MEMORY_MODULE_TYPES.register("summon_cooldown",
                    () -> new MemoryModuleType<>(Optional.of(Codec.INT)));

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Integer>> DASH_COOLDOWN =
            MEMORY_MODULE_TYPES.register("dash_cooldown",
                    () -> new MemoryModuleType<>(Optional.of(Codec.INT)));

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Integer>> WHIRLPOOL_COOLDOWN =
            MEMORY_MODULE_TYPES.register("whirlpool_cooldown",
                    () -> new MemoryModuleType<>(Optional.of(Codec.INT)));

    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<Integer>> CURSE_COOLDOWN =
            MEMORY_MODULE_TYPES.register("curse_cooldown",
                    () -> new MemoryModuleType<>(Optional.of(Codec.INT)));
}
