package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class LivingUseTotemEvent {
    public static final Event<Consumer<LivingUseTotemEvent>> EVENT = EventFactory.createConsumerLoop();
    private final LivingEntity entity;
    private final DamageSource source;
    private final ItemStack totem;

    public LivingUseTotemEvent(LivingEntity entity, DamageSource source, ItemStack totem) {
        this.entity = entity;
        this.source = source;
        this.totem = totem;
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public DamageSource getSource() {
        return source;
    }

    public ItemStack getTotem() {
        return totem;
    }
}
