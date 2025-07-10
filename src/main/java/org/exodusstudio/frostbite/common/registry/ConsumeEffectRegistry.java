package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.effect.RaiseTemperatureConsumeEffect;

public class ConsumeEffectRegistry {
    public static final DeferredRegister<ConsumeEffect.Type<?>> CONSUME_EFFECT_TYPES =
            DeferredRegister.create(BuiltInRegistries.CONSUME_EFFECT_TYPE, Frostbite.MOD_ID);

    public static final DeferredHolder<ConsumeEffect.Type<?>, ConsumeEffect.Type<RaiseTemperatureConsumeEffect>>
            RAISE_TEMPERATURE_CONSUME_EFFECT = CONSUME_EFFECT_TYPES.register("raise_temperature",
                    () -> new ConsumeEffect.Type<>(RaiseTemperatureConsumeEffect.CODEC, RaiseTemperatureConsumeEffect.STREAM_CODEC)
    );
}
