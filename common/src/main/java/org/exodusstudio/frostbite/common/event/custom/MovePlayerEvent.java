package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;
import java.util.function.Consumer;

public class MovePlayerEvent {
    public static final Event<Consumer<MovePlayerEvent>> EVENT = EventFactory.createConsumerLoop();
    private final Vec3 speed;
    private final UUID player;

    public MovePlayerEvent(Vec3 speed, UUID player) {
        this.speed = speed;
        this.player = player;
    }

    public Vec3 getSpeed() {
        return speed;
    }

    public UUID getPlayer() {
        return player;
    }
}
