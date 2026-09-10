package org.exodusstudio.frostbite.common.particle.types;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.exodusstudio.frostbite.common.particle.options.TextParticleOption;

public class DamageParticleType extends ParticleType<TextParticleOption> {
    public DamageParticleType(boolean overrideLimiter) {
        super(overrideLimiter);
    }

    @Override
    public MapCodec<TextParticleOption> codec() {
        return TextParticleOption.codec(this);
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, TextParticleOption> streamCodec() {
        return TextParticleOption.streamCodec(this);
    }
}
