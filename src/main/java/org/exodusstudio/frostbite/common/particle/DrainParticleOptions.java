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


public record DrainParticleOptions(float roll) implements ParticleOptions {
    public static final MapCodec<DrainParticleOptions> CODEC = RecordCodecBuilder.mapCodec((p_235920_) -> p_235920_.group(Codec.FLOAT.fieldOf("roll").forGetter((p_235922_) -> p_235922_.roll)).apply(p_235920_, DrainParticleOptions::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, DrainParticleOptions> STREAM_CODEC;

    @Override
    public ParticleType<DrainParticleOptions> getType() {
        return ParticleRegistry.DRAIN_PARTICLE.get();
    }

    static {
        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, (p_319439_) -> p_319439_.roll, DrainParticleOptions::new);
    }
}


//public record DrainParticleOptions(DrainCircle drainCircle) implements ParticleOptions {
//    public static final MapCodec<DrainParticleOptions> CODEC;
//    private static final Codec<DrainParticleOptions> FULL_CODEC;
//    public static final StreamCodec<RegistryFriendlyByteBuf, DrainParticleOptions> STREAM_CODEC;
//
//    @Override
//    public ParticleType<DrainParticleOptions> getType() {
//        return ParticleRegistry.DRAIN_PARTICLE.get();
//    }
//
//    static {
//        FULL_CODEC = RecordCodecBuilder.create((instance) -> instance.group(BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("drainCircle").forGetter(DrainParticleOptions::drainCircle)));
//        CODEC = Codec.withAlternative(FULL_CODEC, BuiltInRegistries.ENTITY_TYPE.byNameCodec(), DrainParticleOptions::new);
//        STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.registry(BuiltInRegistries.ENTITY_TYPE.key()), DrainParticleOptions::drainCircle);
//        //STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, (p_319439_) -> p_319439_.roll, DrainParticleOptions::new);
//    }
//}
