package org.exodusstudio.frostbite.common.particle.options;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public record Vec3ParticleOption(ParticleType<Vec3ParticleOption> type, Vector3fc vec3f) implements ParticleOptions {
    public static MapCodec<Vec3ParticleOption> codec(ParticleType<Vec3ParticleOption> particleType) {
        return ExtraCodecs.VECTOR3F.xmap((p_333828_) -> new Vec3ParticleOption(particleType, p_333828_),
                Vec3ParticleOption::vec3f).fieldOf("vec3f");
    }

    public static StreamCodec<? super ByteBuf, Vec3ParticleOption> streamCodec(ParticleType<Vec3ParticleOption> type) {
        return ByteBufCodecs.VECTOR3F.map((p_333912_) -> new Vec3ParticleOption(type, p_333912_),
                Vec3ParticleOption::vec3f);
    }
    public ParticleType<Vec3ParticleOption> getType() {
        return this.type;
    }

    public static Vec3ParticleOption create(ParticleType<Vec3ParticleOption> type, Vector3f vec3) {
        return new Vec3ParticleOption(type, vec3);
    }

    public static Vec3ParticleOption create(ParticleType<Vec3ParticleOption> type, Vec3 vec) {
        return create(type, vec.toVector3f());
    }
}
