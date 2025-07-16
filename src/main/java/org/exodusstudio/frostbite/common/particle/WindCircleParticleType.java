package org.exodusstudio.frostbite.common.particle;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

public class WindCircleParticleType extends ParticleType<WindCircleParticleOptions> implements ParticleOptions {
    public WindCircleParticleType(boolean overrideLimiter) {
        super(overrideLimiter);
    }

    @Override
    public MapCodec<WindCircleParticleOptions> codec() {
        return WindCircleParticleOptions.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, WindCircleParticleOptions> streamCodec() {
        return WindCircleParticleOptions.STREAM_CODEC;
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return this;
    }
}
