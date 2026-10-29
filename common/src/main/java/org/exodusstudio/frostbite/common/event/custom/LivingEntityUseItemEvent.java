package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public abstract class LivingEntityUseItemEvent extends CancellableEvent {
    private final LivingEntity entity;
    private final ItemStack item;
    private final int duration;

    protected LivingEntityUseItemEvent(LivingEntity entity, ItemStack item, int duration) {
        this.entity = entity;
        this.item = item;
        this.duration = duration;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getDuration() {
        return duration;
    }

    public static class Start extends LivingEntityUseItemEvent {
        public static final Event<Consumer<Start>> EVENT = EventFactory.createConsumerLoop();

        public Start(LivingEntity entity, ItemStack item, int duration) {
            super(entity, item, duration);
        }
    }

    public static class Tick extends LivingEntityUseItemEvent {
        public static final Event<Consumer<Tick>> EVENT = EventFactory.createConsumerLoop();

        public Tick(LivingEntity entity, ItemStack item, int duration) {
            super(entity, item, duration);
        }
    }
}
