package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.network.chat.Component;

import java.util.function.Function;

public class ListCodexEntry extends CodexEntry {
    public final Component description;
    public final Component tips;
    public final Function<EntryContext, Boolean> function;

    @SafeVarargs
    public ListCodexEntry(String id, Function<EntryContext, Boolean>... function) {
        super(id);
        this.function = function.length > 0 ? function[0] : null;
        this.title = Component.translatable("codex.list_entry." + id + ".title");
        this.description = Component.translatable("codex.list_entry." + id + ".description");
        this.tips = Component.translatable("codex.list_entry." + id + ".tips");
    }
}
