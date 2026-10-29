package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public class LivingTickEvent {
    public static final Event<Consumer<LivingTickEvent>> EVENT = EventFactory.createConsumerLoop();
    private final LivingEntity entity;

    public LivingTickEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }
}
