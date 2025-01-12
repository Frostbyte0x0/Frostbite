package com.frostbyte.frostbite.component;

import com.frostbyte.frostbite.Frostbite;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(ResourceKey.createRegistryKey(ResourceLocation
                    .fromNamespaceAndPath(Frostbite.MOD_ID, "data_component_types")), Frostbite.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ModeData>> MODE = register("mode",
            builder -> builder.persistent(ModeData.CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ChargeData>> CHARGE = register("charge",
            builder -> builder.persistent(ChargeData.CODEC));


    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
