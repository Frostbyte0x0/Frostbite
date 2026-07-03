package org.exodusstudio.frostbite.client.codex.entries;

import com.mojang.datafixers.util.Either;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public class ListCodexEntry extends CodexEntry {
    public final Component title;
    public final Component description;
    public final Component tips;
    public final Either<ListEntryType, Function<EntryContext, Boolean>> typeOrFunction;

    public ListCodexEntry(String id, Either<ListEntryType, Function<EntryContext, Boolean>> typeOrFunction) {
        super(id);
        this.typeOrFunction = typeOrFunction;
        this.title = Component.translatable("codex.list_entry." + id + ".title");
        this.description = Component.translatable("codex.list_entry." + id + ".description");
        this.tips = Component.translatable("codex.list_entry." + id + ".tips");
    }
}
