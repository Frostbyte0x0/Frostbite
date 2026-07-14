package org.exodusstudio.frostbite.common.particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record StringParticleOption(ParticleType<StringParticleOption> type, String text) implements ParticleOptions {
    public static MapCodec<StringParticleOption> codec(ParticleType<StringParticleOption> particleType) {
        return RecordCodecBuilder.mapCodec((instance) ->
                instance.group(Codec.STRING.fieldOf("text")
                                .forGetter(StringParticleOption::text))
                        .apply(instance, (text) -> new StringParticleOption(particleType, text)));
    }

    public static StreamCodec<? super ByteBuf, StringParticleOption> streamCodec(ParticleType<StringParticleOption> type) {
        return ByteBufCodecs.STRING_UTF8.map(
                s -> new StringParticleOption(type, s),
                StringParticleOption::text);
    }

    public ParticleType<StringParticleOption> getType() {
        return this.type;
    }

    public static StringParticleOption create(ParticleType<StringParticleOption> type, String text) {
        return new StringParticleOption(type, text);
    }
}
