package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class LivingKnockBackEvent {
    public static final Event<Consumer<LivingKnockBackEvent>> EVENT = EventFactory.createConsumerLoop();
    private final LivingEntity entity;
    private float strength;

    public LivingKnockBackEvent(LivingEntity entity, float strength) {
        this.entity = entity;
        this.strength = strength;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }
}
