package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.network.chat.Component;
import org.exodusstudio.frostbite.client.codex.formations.CodexFormation;

public class TargetCodexEntry extends CodexEntry {
    public final TargetCodexEntry parent;
    public final Component description;
    public final CodexFormation formation;

    public TargetCodexEntry(String id, TargetCodexEntry parent, String description, CodexFormation formation) {
        super(id);
        this.parent = parent;
        this.description = Component.literal(description);
        this.formation = formation;
    }
}
