package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.particle.DrainParticleType;

import java.util.function.Supplier;

public class ParticleRegistry {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Frostbite.MOD_ID);

    public static final Supplier<DrainParticleType> DRAIN_PARTICLE =
            PARTICLE_TYPES.register("drain", () -> new DrainParticleType(false));
}
