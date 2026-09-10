package org.exodusstudio.frostbite.common.particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record TextParticleOption(ParticleType<TextParticleOption> type, String text, int color) implements ParticleOptions {
    public static MapCodec<TextParticleOption> codec(ParticleType<TextParticleOption> particleType) {
        return RecordCodecBuilder.mapCodec((instance) ->
                instance.group(
                                Codec.STRING.fieldOf("text")
                                        .forGetter(TextParticleOption::text),
                                Codec.INT.fieldOf("color")
                                        .forGetter(TextParticleOption::color))
                        .apply(instance, (text, color) -> new TextParticleOption(particleType, text, color)));
    }

    public static StreamCodec<? super ByteBuf, TextParticleOption> streamCodec(ParticleType<TextParticleOption> type) {
        return StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, TextParticleOption::text,
                ByteBufCodecs.INT, TextParticleOption::color,
                (text, color) -> new TextParticleOption(type, text, color));
    }

    public ParticleType<TextParticleOption> getType() {
        return this.type;
    }

    public static TextParticleOption create(ParticleType<TextParticleOption> type, String text, int color) {
        return new TextParticleOption(type, text, color);
    }
}
