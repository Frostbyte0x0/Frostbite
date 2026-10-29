package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Consumer;

public abstract class LivingDamageEvent {
    private final LivingEntity entity;
    private final DamageSource source;
    private final float originalDamage;

    protected LivingDamageEvent(LivingEntity entity, DamageSource source, float originalDamage) {
        this.entity = entity;
        this.source = source;
        this.originalDamage = originalDamage;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public DamageSource getSource() {
        return source;
    }

    public float getOriginalDamage() {
        return originalDamage;
    }

    public static class Pre extends LivingDamageEvent {
        public static final Event<Consumer<Pre>> EVENT = EventFactory.createConsumerLoop();
        private float newDamage;

        public Pre(LivingEntity entity, DamageSource source, float originalDamage, float newDamage) {
            super(entity, source, originalDamage);
            this.newDamage = newDamage;
        }

        public float getNewDamage() {
            return newDamage;
        }

        public void setNewDamage(float newDamage) {
            this.newDamage = newDamage;
        }
    }

    public static class Post extends LivingDamageEvent {
        public static final Event<Consumer<Post>> EVENT = EventFactory.createConsumerLoop();
        private final float inflictedDamage;

        public Post(LivingEntity entity, DamageSource source, float originalDamage, float inflictedDamage) {
            super(entity, source, originalDamage);
            this.inflictedDamage = inflictedDamage;
        }

        public float getInflictedDamage() {
            return inflictedDamage;
        }
    }
}
