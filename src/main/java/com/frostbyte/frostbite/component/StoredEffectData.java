package com.frostbyte.frostbite.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;

import java.io.Serializable;

public record StoredEffectData(MobEffect effect) implements Serializable {
    public static final Codec<StoredEffectData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(MobEffect.CODEC.fieldOf("effect").forGetter(StoredEffectData::effect)).apply(instance, StoredEffectData::new));

    public static final StreamCodec<ByteBuf, StoredEffectData> STREAM_CODEC;

    static {
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.fromCodec(MobEffect.CODEC), StoredEffectData::effect, StoredEffectData::new);
    }
}
