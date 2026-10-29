package org.exodusstudio.frostbite.common.registry;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import org.exodusstudio.frostbite.common.effect.RaiseTemperatureConsumeEffect;

import java.util.List;

public class ConsumableRegistry {
    public static final Consumable WARMTH_BOTTLE =
            defaultDrink()
                    .consumeSeconds(3.0F)
                    .sound(SoundEvents.HONEY_DRINK)
                    .onConsume(new RaiseTemperatureConsumeEffect(List.of(10f, 10f))).build();
    public static final Consumable HEAT_BOTTLE =
            defaultDrink()
                    .consumeSeconds(2.0F)
                    .sound(SoundEvents.HONEY_DRINK)
                    .onConsume(new RaiseTemperatureConsumeEffect(List.of(30f, 30f))).build();
    public static final Consumable CHILLI =
            defaultFood()
                    .onConsume(new RaiseTemperatureConsumeEffect(List.of(0f, 10f))).build();
    public static final Consumable SATIATED =
            defaultFood()
                    .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(EffectRegistry.SATIATED, 4800))).build();

    public static Consumable.Builder defaultFood() {
        return Consumable.builder().consumeSeconds(1.6F).animation(ItemUseAnimation.EAT).sound(SoundEvents.GENERIC_EAT).hasConsumeParticles(true);
    }

    public static Consumable.Builder defaultDrink() {
        return Consumable.builder().consumeSeconds(1.6F).animation(ItemUseAnimation.DRINK).sound(SoundEvents.GENERIC_DRINK).hasConsumeParticles(false);
    }
}
