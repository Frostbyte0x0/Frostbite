package org.exodusstudio.frostbite.common.particle.options;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;

public record BooleanParticleOption(ParticleType<BooleanParticleOption> type, Boolean bool) implements ParticleOptions {
    public static MapCodec<BooleanParticleOption> codec(ParticleType<BooleanParticleOption> particleType) {
        return ExtraCodecs.VECTOR3F.xmap((p_333828_) -> new BooleanParticleOption(particleType, p_333828_),
                BooleanParticleOption::bool).fieldOf("bool");
    }

    public static StreamCodec<? super ByteBuf, BooleanParticleOption> streamCodec(ParticleType<BooleanParticleOption> type) {
        return ByteBufCodecs.BOOL.map((p_333912_) -> new BooleanParticleOption(type, p_333912_),
                BooleanParticleOption::bool);
    }

    public ParticleType<BooleanParticleOption> getType() {
        return type;
    }

    public static BooleanParticleOption create(ParticleType<BooleanParticleOption> type, Boolean vec3) {
        return new BooleanParticleOption(type, vec3);
    }
}
