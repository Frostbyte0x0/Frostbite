package org.exodusstudio.frostbite.common.event.custom;

import net.neoforged.bus.api.Event;
import org.exodusstudio.frostbite.client.codex.entries.CodexEntry;

public class PlayerHasEntryEvent extends Event {
    private final CodexEntry entry;
    public boolean hasEntry;

    public PlayerHasEntryEvent(CodexEntry entry) {
        this.entry = entry;
    }

    public CodexEntry getEntry() {
        return entry;
    }
}
