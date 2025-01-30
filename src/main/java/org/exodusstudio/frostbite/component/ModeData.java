package org.exodusstudio.frostbite.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.io.Serializable;

public record ModeData(String mode) {
    public static final Codec<ModeData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.STRING.fieldOf("mode").forGetter(ModeData::mode)).apply(instance, ModeData::new));

    public static final StreamCodec<ByteBuf, ModeData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ModeData::mode, ModeData::new);
}
