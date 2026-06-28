package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.network.chat.Component;

public class ListCodexEntry extends CodexEntry {
    public final Component title;
    public final Component description;
    public final Component tips;

    public ListCodexEntry(String id) {
        super(id);
        this.title = Component.translatable("codex.list_entry." + id + ".title");
        this.description = Component.translatable("codex.list_entry." + id + ".description");
        this.tips = Component.translatable("codex.list_entry." + id + ".tips");
    }
}
