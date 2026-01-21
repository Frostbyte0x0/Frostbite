package org.exodusstudio.frostbite.client.codex.entries;

import org.exodusstudio.frostbite.client.codex.formations.CircleCodexFormation;

public class CircleCodexEntry extends CodexEntry {
    CircleCodexFormation circleCodexFormation;

    public CircleCodexEntry(String id, CircleCodexFormation codexFormation) {
        super(id);
        this.circleCodexFormation = codexFormation;
    }
}
