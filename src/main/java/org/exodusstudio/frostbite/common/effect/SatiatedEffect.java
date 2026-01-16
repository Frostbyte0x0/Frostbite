package org.exodusstudio.frostbite.common.effect;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.exodusstudio.frostbite.common.util.TE;

public class SatiatedEffect extends MobEffect {
    public SatiatedEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity living, int amplifier) {
        ((TE) living).increaseTemperature(1.0f + amplifier * 0.5f, true);
        return super.applyEffectTick(serverLevel, living, amplifier);
    }

    public boolean shouldApplyEffectTickThisTick(int i, int amplifier) {
        return i % 60 == 0;
    }

    public void onEffectStarted(LivingEntity living, int p_295222_) {
        super.onEffectStarted(living, p_295222_);
        ((TE) living).increaseTemperature(10, true);
    }
}
