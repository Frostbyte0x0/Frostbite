package org.exodusstudio.frostbite.common.event.custom;

import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.Event;

import java.util.UUID;

public class MovePlayerEvent extends Event {
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
