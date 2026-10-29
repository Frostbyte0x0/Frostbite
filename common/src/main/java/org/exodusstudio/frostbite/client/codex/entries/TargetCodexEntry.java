package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import org.exodusstudio.frostbite.client.codex.formations.CodexFormation;

import java.util.Optional;

public class TargetCodexEntry extends CodexEntry {
    public final TargetCodexEntry parent;
    public final Component description;
    public final CodexFormation formation;
    public final Optional<Item> drops;

    public TargetCodexEntry(String id, TargetCodexEntry parent, CodexFormation formation, Optional<Item> drops) {
        super(id);
        this.title = Component.translatable("codex.target_entry." + id + ".title");
        this.parent = parent;
        this.description = Component.translatable("codex.target_entry." + id + ".description");
        this.formation = formation;
        this.drops = drops;
    }
}
