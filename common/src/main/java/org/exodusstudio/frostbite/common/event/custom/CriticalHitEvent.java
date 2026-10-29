package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public class CriticalHitEvent {
    public static final Event<Consumer<CriticalHitEvent>> EVENT = EventFactory.createConsumerLoop();
    private final Player entity;
    private final Entity target;
    private float damageMultiplier;

    public CriticalHitEvent(Player entity, Entity target, float damageMultiplier) {
        this.entity = entity;
        this.target = target;
        this.damageMultiplier = damageMultiplier;
    }

    public Player getEntity() {
        return entity;
    }

    public Entity getTarget() {
        return target;
    }

    public float getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }
}
