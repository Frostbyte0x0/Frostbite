package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class MobEffectEvent extends CancellableEvent {
    private final LivingEntity entity;
    private final @Nullable MobEffectInstance effectInstance;

    protected MobEffectEvent(LivingEntity entity, @Nullable MobEffectInstance effectInstance) {
        this.entity = entity;
        this.effectInstance = effectInstance;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public @Nullable MobEffectInstance getEffectInstance() {
        return effectInstance;
    }

    public static class Remove extends MobEffectEvent {
        public static final Event<Consumer<Remove>> EVENT = EventFactory.createConsumerLoop();
        private final Holder<MobEffect> effect;

        public Remove(LivingEntity entity, Holder<MobEffect> effect, @Nullable MobEffectInstance effectInstance) {
            super(entity, effectInstance);
            this.effect = effect;
        }

        public Holder<MobEffect> getEffect() {
            return effect;
        }
    }
}
