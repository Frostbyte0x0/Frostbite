package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.network.chat.Component;
import org.exodusstudio.frostbite.client.codex.formations.CodexFormation;

public class TreeCodexEntry extends CodexEntry {
    public final TreeCodexEntry parent;
    public final Component description;
    public final CodexFormation formation;

    public TreeCodexEntry(String id, TreeCodexEntry parent, String description, CodexFormation formation) {
        super(id);
        this.parent = parent;
        this.description = Component.literal(description);
        this.formation = formation;
    }
}
