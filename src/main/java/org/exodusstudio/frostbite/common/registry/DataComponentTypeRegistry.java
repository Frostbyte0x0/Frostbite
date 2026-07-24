package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.component.*;

import java.util.function.Supplier;

public class DataComponentTypeRegistry {
    public static final DeferredRegister.DataComponents DATA_COMPONENT_TYPES =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Frostbite.MOD_ID);

    public static final Supplier<DataComponentType<ModeData>> MODE = DATA_COMPONENT_TYPES.registerComponentType("mode",
            builder -> builder.persistent(ModeData.CODEC).networkSynchronized(ModeData.STREAM_CODEC));

    public static final Supplier<DataComponentType<ChargeData>> CHARGE = DATA_COMPONENT_TYPES.registerComponentType("charge",
            builder -> builder.persistent(ChargeData.CODEC).networkSynchronized(ChargeData.STREAM_CODEC));

    public static final Supplier<DataComponentType<ContractData>> CONTRACT = DATA_COMPONENT_TYPES.registerComponentType("contract",
            builder -> builder.persistent(ContractData.CODEC).networkSynchronized(ContractData.STREAM_CODEC));

    public static final Supplier<DataComponentType<ContractAttributeData>> CONTRACT_ATTRIBUTE = DATA_COMPONENT_TYPES.registerComponentType("contract_attribute",
            builder -> builder.persistent(ContractAttributeData.CODEC).networkSynchronized(ContractAttributeData.STREAM_CODEC));

    public static final Supplier<DataComponentType<CooldownData>> COOLDOWN = DATA_COMPONENT_TYPES.registerComponentType("cooldown",
            builder -> builder.persistent(CooldownData.CODEC).networkSynchronized(CooldownData.STREAM_CODEC));

    public static final Supplier<DataComponentType<GunData>> GUN = DATA_COMPONENT_TYPES.registerComponentType("gun",
            builder -> builder.persistent(GunData.CODEC).networkSynchronized(GunData.STREAM_CODEC));

    public static final Supplier<DataComponentType<HuntersCatalystData>> HUNTERS_CATALYST = DATA_COMPONENT_TYPES.registerComponentType("hunters_catalyst",
            builder -> builder.persistent(HuntersCatalystData.CODEC).networkSynchronized(HuntersCatalystData.STREAM_CODEC));
}
