package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.effect.RageEffect;

public class EffectRegistry {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Frostbite.MOD_ID);


    public static final Holder<MobEffect> RAGE = MOB_EFFECTS.register("rage",
            () -> new RageEffect(MobEffectCategory.BENEFICIAL, 0x8c1111)
                    .addAttributeModifier(Attributes.ATTACK_SPEED, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "rage_attack_speed"),
                            0.3f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "rage_movement_speed"),
                            0.3f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ATTACK_DAMAGE, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "rage_attack_damage"),
                            0.3f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.ARMOR, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "rage_armor"),
                            0.3f, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
}
