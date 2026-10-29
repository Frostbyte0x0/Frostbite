package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class LivingIncomingDamageEvent extends CancellableEvent {
    public static final Event<Consumer<LivingIncomingDamageEvent>> EVENT = EventFactory.createConsumerLoop();
    private final LivingEntity entity;
    private final DamageSource source;
    private final float amount;

    public LivingIncomingDamageEvent(LivingEntity entity, DamageSource source, float amount) {
        this.entity = entity;
        this.source = source;
        this.amount = amount;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public DamageSource getSource() {
        return source;
    }

    public float getAmount() {
        return amount;
    }
}
