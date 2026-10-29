package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.Mob;

import java.util.function.Consumer;

public class FinalizeSpawnEvent {
    public static final Event<Consumer<FinalizeSpawnEvent>> EVENT = EventFactory.createConsumerLoop();
    private final Mob entity;

    public FinalizeSpawnEvent(Mob entity) {
        this.entity = entity;
    }

    public Mob getEntity() {
        return entity;
    }
}
