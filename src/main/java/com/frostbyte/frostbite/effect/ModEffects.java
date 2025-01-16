package com.frostbyte.frostbite.effect;

import com.frostbyte.frostbite.Frostbite;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Frostbite.MOD_ID);

    public static final Holder<MobEffect> IRRITATION = MOB_EFFECTS.register("irritation",
            () -> new IrritationEffect(MobEffectCategory.HARMFUL, 0xab640e)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "irritation_slowness"),
                            -0.1f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "irritation_attack_slowness"),
                            -0.1f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final Holder<MobEffect> PARANOIA = MOB_EFFECTS.register("paranoia",
            () -> new ParanoiaEffect(MobEffectCategory.HARMFUL, 0x400b6b));

    public static final Holder<MobEffect> FATIGUE = MOB_EFFECTS.register("fatigue",
            () -> new FatigueEffect(MobEffectCategory.HARMFUL, 0x3b4052));

    public static final Holder<MobEffect> PARALYSIS = MOB_EFFECTS.register("paralysis",
            () -> new FatigueEffect(MobEffectCategory.HARMFUL, 0x3b4052));

    public static final Holder<MobEffect> PETRIFICATION = MOB_EFFECTS.register("petrification",
            () -> new FatigueEffect(MobEffectCategory.HARMFUL, 0x3b4052));

    public static final Holder<MobEffect> MOLD = MOB_EFFECTS.register("mold",
            () -> new FatigueEffect(MobEffectCategory.HARMFUL, 0x3b4052));

    public static final Holder<MobEffect> DECAY = MOB_EFFECTS.register("decay",
            () -> new FatigueEffect(MobEffectCategory.HARMFUL, 0x3b4052));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
