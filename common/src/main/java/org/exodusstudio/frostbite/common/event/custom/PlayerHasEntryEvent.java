package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import org.exodusstudio.frostbite.client.codex.entries.CodexEntry;

import java.util.function.Consumer;

public class PlayerHasEntryEvent {
    public static final Event<Consumer<PlayerHasEntryEvent>> EVENT = EventFactory.createConsumerLoop();
    private final CodexEntry entry;
    public boolean hasEntry;

    public PlayerHasEntryEvent(CodexEntry entry) {
        this.entry = entry;
    }

    public CodexEntry getEntry() {
        return entry;
    }
}
