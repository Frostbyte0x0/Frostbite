package org.exodusstudio.frostbite.common.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class DrainParticleType extends ParticleType<DrainParticleOptions> {
    public DrainParticleType(boolean overrideLimiter) {
        super(overrideLimiter);
    }

    @Override
    public MapCodec<DrainParticleOptions> codec() {
        return DrainParticleOptions.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, DrainParticleOptions> streamCodec() {
        return DrainParticleOptions.STREAM_CODEC;
    }
}
