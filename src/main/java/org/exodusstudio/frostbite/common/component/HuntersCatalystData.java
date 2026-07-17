package org.exodusstudio.frostbite.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record HuntersCatalystData(int ticksRemaining) {
    public static final Codec<HuntersCatalystData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.INT.fieldOf("ticksRemaining").forGetter(HuntersCatalystData::ticksRemaining)).apply(instance, HuntersCatalystData::new));

    public static final StreamCodec<ByteBuf, HuntersCatalystData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT, HuntersCatalystData::ticksRemaining, HuntersCatalystData::new);
}
