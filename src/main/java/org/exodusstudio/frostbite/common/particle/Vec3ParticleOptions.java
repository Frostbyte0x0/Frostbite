package org.exodusstudio.frostbite.common.particle;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public record Vec3ParticleOptions(ParticleType<Vec3ParticleOptions> type, Vector3f vec3) implements ParticleOptions {

    public static MapCodec<Vec3ParticleOptions> codec(ParticleType<Vec3ParticleOptions> particleType) {
        return ExtraCodecs.VECTOR3F.xmap((p_333828_) -> new Vec3ParticleOptions(particleType, p_333828_),
                Vec3ParticleOptions::vec3).fieldOf("color");
    }

    public static StreamCodec<? super ByteBuf, Vec3ParticleOptions> streamCodec(ParticleType<Vec3ParticleOptions> type) {
        return ByteBufCodecs.VECTOR3F.map((p_333912_) -> new Vec3ParticleOptions(type, p_333912_),
                Vec3ParticleOptions::vec3);
    }
    public ParticleType<Vec3ParticleOptions> getType() {
        return this.type;
    }

    public static Vec3ParticleOptions create(ParticleType<Vec3ParticleOptions> type, Vector3f vec3) {
        return new Vec3ParticleOptions(type, vec3);
    }

    public static Vec3ParticleOptions create(ParticleType<Vec3ParticleOptions> type, Vec3 vec) {
        return create(type, vec.toVector3f());
    }
}
