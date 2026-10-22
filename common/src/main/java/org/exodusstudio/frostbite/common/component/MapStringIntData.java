package org.exodusstudio.frostbite.common.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.exodusstudio.frostbite.common.util.helpers.CodecHelper;

import java.util.Map;

public record MapStringIntData(Map<String, Integer> map) {
    public static MapStringIntData EMPTY = new MapStringIntData(Map.of());

    public static final Codec<MapStringIntData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("map").forGetter(MapStringIntData::map)).apply(instance, MapStringIntData::new));

    public static final StreamCodec<ByteBuf, MapStringIntData> STREAM_CODEC =
            StreamCodec.of(
                    (b, d) -> CodecHelper.MAP_STRING_INT_STREAM_CODEC.encode(b, d.map),
                    (b) -> new MapStringIntData(CodecHelper.MAP_STRING_INT_STREAM_CODEC.decode(b))
            );
}
