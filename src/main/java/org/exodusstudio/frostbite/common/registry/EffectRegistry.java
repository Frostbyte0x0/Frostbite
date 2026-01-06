package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.effect.GenericEffect;
import org.exodusstudio.frostbite.common.effect.RageEffect;
import org.exodusstudio.frostbite.common.effect.SatiatedEffect;

public class EffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Frostbite.MOD_ID);

    public static final Holder<MobEffect> RAGE = MOB_EFFECTS.register("rage",
            () -> new RageEffect(MobEffectCategory.BENEFICIAL, 0x8c1111)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "rage_attack_speed"),
                            0.3f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "rage_movement_speed"),
                            0.3f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "rage_attack_damage"),
                            0.3f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ARMOR, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "rage_armor"),
                            0.3f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final Holder<MobEffect> COLD_WEAKNESS = MOB_EFFECTS.register("cold_weakness",
            () -> new GenericEffect(MobEffectCategory.HARMFUL, 0x5bc0de));

    public static final Holder<MobEffect> SATIATED = MOB_EFFECTS.register("satiated",
            () -> new SatiatedEffect(MobEffectCategory.BENEFICIAL, 0xf5b642));

    public static final Holder<MobEffect> FATIGUE = MOB_EFFECTS.register("fatigue",
            () -> new GenericEffect(MobEffectCategory.HARMFUL, 0x3e374f));

    public static final Holder<MobEffect> TWITCHING = MOB_EFFECTS.register("twitching",
            () -> new GenericEffect(MobEffectCategory.HARMFUL, 0x2f6363));

    public static final Holder<MobEffect> LEECH_CURSE = MOB_EFFECTS.register("leech_curse",
            () -> new GenericEffect(MobEffectCategory.HARMFUL, 0x7e28bf));

    public static final Holder<MobEffect> PARALYSIS_CURSE = MOB_EFFECTS.register("paralysis_curse",
            () -> new GenericEffect(MobEffectCategory.HARMFUL, 0x483f4f));

    public static final Holder<MobEffect> CONDEMNED_CURSE = MOB_EFFECTS.register("condemned_curse",
            () -> new GenericEffect(MobEffectCategory.HARMFUL, 0x000000));

    public static boolean isCurse(Holder<MobEffect> effect) {
        return effect == LEECH_CURSE || effect == PARALYSIS_CURSE || effect == CONDEMNED_CURSE;
    }
}
