package org.exodusstudio.frostbite.common.registry;

import org.exodusstudio.frostbite.Frostbite;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.common.component.ChargeData;
import org.exodusstudio.frostbite.common.component.FillLevelData;
import org.exodusstudio.frostbite.common.component.GunData;
import org.exodusstudio.frostbite.common.component.ModeData;

import java.util.function.Supplier;

public class DataComponentTypeRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Frostbite.MOD_ID);

    public static final Supplier<DataComponentType<ModeData>> MODE = DATA_COMPONENT_TYPES.registerComponentType("mode",
            builder -> builder.persistent(ModeData.CODEC).networkSynchronized(ModeData.STREAM_CODEC));

    public static final Supplier<DataComponentType<ChargeData>> CHARGE = DATA_COMPONENT_TYPES.registerComponentType("charge",
            builder -> builder.persistent(ChargeData.CODEC).networkSynchronized(ChargeData.STREAM_CODEC));

    public static final Supplier<DataComponentType<FillLevelData>> FILL_LEVEL = DATA_COMPONENT_TYPES.registerComponentType("fill_level",
            builder -> builder.persistent(FillLevelData.CODEC).networkSynchronized(FillLevelData.STREAM_CODEC));

//    public static final DeferredHolder<DataComponentType<?>, DataComponentType<JarContentsData>> JAR_CONTENTS = DATA_COMPONENT_TYPES.registerComponentType("jar_contents",
//            builder -> builder.persistent(JarContentsData.CODEC).networkSynchronized(JarContentsData.STREAM_CODEC));

    public static final Supplier<DataComponentType<GunData>> GUN = DATA_COMPONENT_TYPES.registerComponentType("gun",
            builder -> builder.persistent(GunData.CODEC).networkSynchronized(GunData.STREAM_CODEC));
}
