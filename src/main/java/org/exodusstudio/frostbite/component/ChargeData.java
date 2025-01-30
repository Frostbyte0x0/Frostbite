package org.exodusstudio.frostbite.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.io.Serializable;

public record ChargeData(int charge) {
    public static ChargeData EMPTY = new ChargeData(0);

    public static final Codec<ChargeData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.INT.fieldOf("level").forGetter(ChargeData::charge)).apply(instance, ChargeData::new));

    public static final StreamCodec<ByteBuf, ChargeData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, ChargeData::charge, ChargeData::new);
}
