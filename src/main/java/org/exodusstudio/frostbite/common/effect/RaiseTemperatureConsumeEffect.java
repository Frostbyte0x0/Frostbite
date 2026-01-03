package org.exodusstudio.frostbite.common.effect;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.ConsumeEffectRegistry;

import java.util.List;

public record RaiseTemperatureConsumeEffect(List<Float> temps) implements ConsumeEffect {
    public static final MapCodec<RaiseTemperatureConsumeEffect> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, RaiseTemperatureConsumeEffect> STREAM_CODEC;

    public ConsumeEffect.Type<RaiseTemperatureConsumeEffect> getType() {
        return ConsumeEffectRegistry.RAISE_TEMPERATURE_CONSUME_EFFECT.get();
    }

    public boolean apply(Level level, ItemStack stack, LivingEntity entity) {
        Frostbite.temperatureStorage.increaseTemperature(entity, temps.get(0), false);
        Frostbite.temperatureStorage.increaseTemperature(entity, temps.get(1), true);

        return true;
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec((instance) ->
                instance.group(Codec.list(Codec.FLOAT)
                        .fieldOf("temps").forGetter(RaiseTemperatureConsumeEffect::temps))
                        .apply(instance, RaiseTemperatureConsumeEffect::new));

        STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistriesTrusted(CODEC.codec());
    }
}
