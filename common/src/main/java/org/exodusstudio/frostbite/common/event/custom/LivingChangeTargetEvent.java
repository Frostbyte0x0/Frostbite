package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class LivingChangeTargetEvent extends CancellableEvent {
    public static final Event<Consumer<LivingChangeTargetEvent>> EVENT = EventFactory.createConsumerLoop();
    private final LivingEntity entity;
    private final @Nullable LivingEntity newTarget;

    public LivingChangeTargetEvent(LivingEntity entity, @Nullable LivingEntity newTarget) {
        this.entity = entity;
        this.newTarget = newTarget;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public @Nullable LivingEntity getNewAboutToBeSetTarget() {
        return newTarget;
    }
}
