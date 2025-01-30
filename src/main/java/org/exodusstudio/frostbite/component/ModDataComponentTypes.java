package org.exodusstudio.frostbite.component;

import org.exodusstudio.frostbite.Frostbite;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.UnaryOperator;

public class ModDataComponentTypes {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Frostbite.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ModeData>> MODE = register("mode",
            builder -> builder.persistent(ModeData.CODEC).networkSynchronized(ModeData.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ChargeData>> CHARGE = register("charge",
            builder -> builder.persistent(ChargeData.CODEC).networkSynchronized(ChargeData.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<FillLevelData>> FILL_LEVEL = register("fill_level",
            builder -> builder.persistent(FillLevelData.CODEC).networkSynchronized(FillLevelData.STREAM_CODEC));

//    public static final DeferredHolder<DataComponentType<?>, DataComponentType<JarContentsData>> JAR_CONTENTS = register("jar_contents",
//            builder -> builder.persistent(JarContentsData.CODEC).networkSynchronized(JarContentsData.STREAM_CODEC));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<GunData>> GUN = register("gun",
            builder -> builder.persistent(GunData.CODEC).networkSynchronized(GunData.STREAM_CODEC));


    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

    public static void register(IEventBus eventBus) {
        DATA_COMPONENT_TYPES.register(eventBus);
    }
}
