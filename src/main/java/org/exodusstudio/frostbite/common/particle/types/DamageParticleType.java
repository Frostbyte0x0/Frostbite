package org.exodusstudio.frostbite.common.particle.types;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.exodusstudio.frostbite.common.particle.options.StringParticleOption;
import org.jetbrains.annotations.NotNull;

public class DamageParticleType extends ParticleType<StringParticleOption> implements ParticleOptions {
    public DamageParticleType(boolean overrideLimiter) {
        super(overrideLimiter);
    }

    @Override
    public MapCodec<StringParticleOption> codec() {
        return StringParticleOption.codec(this);
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, StringParticleOption> streamCodec() {
        return StringParticleOption.streamCodec(this);
    }

    @Override
    public @NotNull ParticleType<?> getType() {
        return this;
    }
}
