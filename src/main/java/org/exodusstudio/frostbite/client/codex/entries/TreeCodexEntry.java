package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.network.chat.Component;

public class TreeCodexEntry extends CodexEntry {
    public final TreeCodexEntry parent;
    public final Component description;

    public TreeCodexEntry(String id, TreeCodexEntry parent, String description) {
        super(id);
        this.parent = parent;
        this.description = Component.literal(description);
    }
}
