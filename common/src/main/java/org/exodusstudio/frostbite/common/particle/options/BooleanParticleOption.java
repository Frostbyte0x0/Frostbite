package org.exodusstudio.frostbite.common.particle.options;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record BooleanParticleOption(ParticleType<BooleanParticleOption> type, Boolean bool) implements ParticleOptions {
    public static MapCodec<BooleanParticleOption> codec(ParticleType<BooleanParticleOption> particleType) {
        return RecordCodecBuilder.mapCodec((instance) ->
                instance.group(Codec.BOOL.fieldOf("delay")
                                .forGetter((p_235954_) -> p_235954_.bool))
                        .apply(instance, (b) -> new BooleanParticleOption(particleType, b)));
    }


    public static StreamCodec<? super ByteBuf, BooleanParticleOption> streamCodec(ParticleType<BooleanParticleOption> type) {
        return ByteBufCodecs.BOOL.map((b) -> new BooleanParticleOption(type, b),
                BooleanParticleOption::bool);
    }

    public ParticleType<BooleanParticleOption> getType() {
        return type;
    }

    public static BooleanParticleOption create(ParticleType<BooleanParticleOption> type, Boolean vec3) {
        return new BooleanParticleOption(type, vec3);
    }
}
