package org.exodusstudio.frostbite.common.particle.types;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.exodusstudio.frostbite.common.particle.options.DrainParticleOption;

public class DrainParticleType extends ParticleType<DrainParticleOption> {
    public DrainParticleType(boolean overrideLimiter) {
        super(overrideLimiter);
    }

    @Override
    public MapCodec<DrainParticleOption> codec() {
        return DrainParticleOption.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, DrainParticleOption> streamCodec() {
        return DrainParticleOption.STREAM_CODEC;
    }
}
