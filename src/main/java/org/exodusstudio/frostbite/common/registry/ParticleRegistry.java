package org.exodusstudio.frostbite.common.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.particle.*;

import java.util.function.Function;
import java.util.function.Supplier;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Frostbite.MOD_ID);

    public static final Supplier<DrainParticleType> DRAIN_PARTICLE =
            PARTICLE_TYPES.register("drain", () -> new DrainParticleType(false));

    public static final Supplier<SimpleParticleType> SHOCKWAVE_PARTICLE =
            PARTICLE_TYPES.register("shockwave", () -> new SimpleParticleType(false));

    public static final Supplier<ColorParticleType> CONFETTI_PARTICLE =
            PARTICLE_TYPES.register("confetti", () -> new ColorParticleType(false));

    public static final Supplier<WindCircleParticleType> WIND_CIRCLE_PARTICLE =
            PARTICLE_TYPES.register("wind_circle", () -> new WindCircleParticleType(false));

    public static final Supplier<SimpleParticleType> BUTTERFLY_PARTICLE =
            PARTICLE_TYPES.register("butterfly", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> SWIRLING_LEAF_PARTICLE =
            PARTICLE_TYPES.register("swirling_leaf", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> SNOWFLAKE_PARTICLE =
            PARTICLE_TYPES.register("snowflake", () -> new SimpleParticleType(false));

    public static final Supplier<ParticleType<Vec3ParticleOptions>> ICY_BREATH_PARTICLE =
            PARTICLE_TYPES.register("icy_breath",
                    makeSupplier(Vec3ParticleOptions::codec, Vec3ParticleOptions::streamCodec));

    public static final Supplier<ColorParticleType> ROAMING_BLIZZARD_PARTICLE =
            PARTICLE_TYPES.register("roaming_blizzard", () -> new ColorParticleType(false));


    private static <T extends ParticleOptions> Supplier<ParticleType<T>> makeSupplier(
            final Function<ParticleType<T>, MapCodec<T>> codecGetter,
            final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodecGetter)
    {
        return () -> new ParticleType<>(false) {
            @Override
            public MapCodec<T> codec() {
                return codecGetter.apply(this);
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodecGetter.apply(this);
            }
        };
    }
}
