package org.exodusstudio.frostbite.client.codex.entries;

public class TreeCodexEntry extends CodexEntry {
    public final TreeCodexEntry parent;

    public TreeCodexEntry(String id, TreeCodexEntry parent) {
        super(id);
        this.parent = parent;
    }
}
