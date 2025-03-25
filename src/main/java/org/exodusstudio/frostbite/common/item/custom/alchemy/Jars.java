package org.exodusstudio.frostbite.common.item.custom.alchemy;

import net.minecraft.world.effect.MobEffect;
import org.exodusstudio.frostbite.Frostbite;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;
import org.exodusstudio.frostbite.common.registry.RegistryRegistry;

import java.util.Optional;

public class Jars {
    public static final DeferredRegister<Jar> JARS =
            DeferredRegister.create(RegistryRegistry.JAR_REGISTRY, Frostbite.MOD_ID);

    public static final Holder<Jar> WATER = register("water",
            new Jar("water"));

    public static final Holder<Jar> FLORAL = register("floral",
            new Jar("floral", new MobEffectInstance(EffectRegistry.IRRITATION, 360000))); // infinite duration

    public static final Holder<Jar> PINE = register("pine",
            new Jar("pine", new MobEffectInstance(EffectRegistry.PARANOIA, 3600)));

    public static final Holder<Jar> AGARIC = register("agaric",
            new Jar("agaric", new MobEffectInstance(EffectRegistry.FATIGUE, 3600)));

    public static final Holder<Jar> DECAYING = register("decaying",
            new Jar("decaying", new MobEffectInstance(EffectRegistry.DECAY, 3600)));

    public static final Holder<Jar> LIGHT = register("light",
            new Jar("light", new MobEffectInstance(EffectRegistry.CORRUPTION, 3600)));

    public static final Holder<Jar> CRYSTAL = register("crystal",
            new Jar("crystal", new MobEffectInstance(EffectRegistry.TWITCHING, 3600)));

    public static final Holder<Jar> MOSSY = register("mossy",
            new Jar("mossy", new MobEffectInstance(EffectRegistry.MOLD, 3600)));


    public static Optional<Holder<Jar>> jarFromEffect(Holder<MobEffect> effect) {
        if (effect.is(EffectRegistry.IRRITATION)) {
            return Optional.of(FLORAL);
        }
        if (effect.is(EffectRegistry.PARANOIA)) {
            return Optional.of(PINE);
        }
        if (effect.is(EffectRegistry.FATIGUE)) {
            return Optional.of(AGARIC);
        }
        if (effect.is(EffectRegistry.DECAY)) {
            return Optional.of(DECAYING);
        }
        if (effect.is(EffectRegistry.CORRUPTION)) {
            return Optional.of(LIGHT);
        }
        if (effect.is(EffectRegistry.TWITCHING)) {
            return Optional.of(CRYSTAL);
        }
        if (effect.is(EffectRegistry.MOLD)) {
            return Optional.of(MOSSY);
        }

        return Optional.of(WATER);
    }


    private static Holder<Jar> register(String name, Jar jar) {
        return JARS.register(name, () -> jar); // ?
    }
}
