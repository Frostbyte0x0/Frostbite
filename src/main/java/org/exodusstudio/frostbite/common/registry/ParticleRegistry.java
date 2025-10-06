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
import org.exodusstudio.frostbite.common.particle.options.BooleanParticleOption;
import org.exodusstudio.frostbite.common.particle.options.Vec3ParticleOption;
import org.exodusstudio.frostbite.common.particle.types.ColorParticleType;
import org.exodusstudio.frostbite.common.particle.types.DrainParticleType;

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

    public static final Supplier<ColorParticleType> EXPANDING_CIRCLE_PARTICLE =
            PARTICLE_TYPES.register("wind_circle", () -> new ColorParticleType(false));

    public static final Supplier<SimpleParticleType> BUTTERFLY_PARTICLE =
            PARTICLE_TYPES.register("butterfly", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> SWIRLING_LEAF_PARTICLE =
            PARTICLE_TYPES.register("swirling_leaf", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> SNOWFLAKE_PARTICLE =
            PARTICLE_TYPES.register("snowflake", () -> new SimpleParticleType(false));

    public static final Supplier<ParticleType<Vec3ParticleOption>> ICY_BREATH_PARTICLE =
            PARTICLE_TYPES.register("icy_breath",
                    makeSupplier(Vec3ParticleOption::codec, Vec3ParticleOption::streamCodec));

    public static final Supplier<ColorParticleType> ROAMING_BLIZZARD_PARTICLE =
            PARTICLE_TYPES.register("roaming_blizzard", () -> new ColorParticleType(false));

    public static final Supplier<ParticleType<BooleanParticleOption>> HEAL_PARTICLE =
            PARTICLE_TYPES.register("heal",
                    makeSupplier(BooleanParticleOption::codec, BooleanParticleOption::streamCodec));


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

    public static final Supplier<SimpleParticleType> DEBUG_PARTICLE =
            PARTICLE_TYPES.register("debug", () -> new SimpleParticleType(false));
}
