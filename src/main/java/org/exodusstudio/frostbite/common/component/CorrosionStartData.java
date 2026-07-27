package org.exodusstudio.frostbite.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record CorrosionStartData(long start) {
    public static CorrosionStartData EMPTY = new CorrosionStartData(0);

    public static final Codec<CorrosionStartData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.LONG.fieldOf("start").forGetter(CorrosionStartData::start)).apply(instance, CorrosionStartData::new));

    public static final StreamCodec<ByteBuf, CorrosionStartData> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.LONG, CorrosionStartData::start, CorrosionStartData::new);
}
