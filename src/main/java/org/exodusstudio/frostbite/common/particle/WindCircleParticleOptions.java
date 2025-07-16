package org.exodusstudio.frostbite.common.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;

public record WindCircleParticleOptions(float roll) implements ParticleOptions {
    public static final MapCodec<WindCircleParticleOptions> CODEC = RecordCodecBuilder.mapCodec((p_235920_) -> p_235920_.group(Codec.FLOAT.fieldOf("roll").forGetter((p_235922_) -> p_235922_.roll)).apply(p_235920_, WindCircleParticleOptions::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, WindCircleParticleOptions> STREAM_CODEC;

    @Override
    public ParticleType<WindCircleParticleOptions> getType() {
        return ParticleRegistry.WIND_CIRCLE_PARTICLE.get();
    }

    static {
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, (p_319439_) -> p_319439_.roll, WindCircleParticleOptions::new);
    }
}
