package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Consumer;

public class AdvancementProgressEvent {
    public static final Event<Consumer<AdvancementProgressEvent>> EVENT = EventFactory.createConsumerLoop();
    private final ServerPlayer entity;
    private final AdvancementHolder advancement;
    private final String criterionName;

    public AdvancementProgressEvent(ServerPlayer entity, AdvancementHolder advancement, String criterionName) {
        this.entity = entity;
        this.advancement = advancement;
        this.criterionName = criterionName;
    }

    public ServerPlayer getEntity() {
        return entity;
    }

    public AdvancementHolder getAdvancement() {
        return advancement;
    }

    public String getCriterionName() {
        return criterionName;
    }
}
