package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.particle.ConfettiParticleType;
import org.exodusstudio.frostbite.common.particle.DrainParticleType;
import org.exodusstudio.frostbite.common.particle.WindCircleParticleType;

import java.util.function.Supplier;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Frostbite.MOD_ID);

    public static final Supplier<DrainParticleType> DRAIN_PARTICLE =
            PARTICLE_TYPES.register("drain", () -> new DrainParticleType(false));

    public static final Supplier<SimpleParticleType> SHOCKWAVE_PARTICLE =
            PARTICLE_TYPES.register("shockwave", () -> new SimpleParticleType(false));

    public static final Supplier<ConfettiParticleType> CONFETTI_PARTICLE =
            PARTICLE_TYPES.register("confetti", () -> new ConfettiParticleType(false));

    public static final Supplier<WindCircleParticleType> WIND_CIRCLE_PARTICLE =
            PARTICLE_TYPES.register("wind_circle", () -> new WindCircleParticleType(false));

    public static final Supplier<SimpleParticleType> BUTTERFLY_PARTICLE =
            PARTICLE_TYPES.register("butterfly", () -> new SimpleParticleType(false));

    public static final Supplier<SimpleParticleType> SWIRLING_LEAF_PARTICLE =
            PARTICLE_TYPES.register("swirling_leaf", () -> new SimpleParticleType(false));}
