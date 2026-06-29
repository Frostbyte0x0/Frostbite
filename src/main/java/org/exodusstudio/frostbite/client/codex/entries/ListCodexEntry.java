package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class ListCodexEntry extends CodexEntry {
    public final Component title;
    public final List<FormattedCharSequence> titleLines;
    public final Component description;
    public final List<FormattedCharSequence> descriptionLines;
    public final Component tips;
    public final List<FormattedCharSequence> tipsLines;
    public ListCodexEntry(String id) {
        super(id);
        this.title = Component.translatable("codex.list_entry." + id + ".title");
        this.titleLines = Minecraft.getInstance().font.split(this.title, 163);
        this.description = Component.translatable("codex.list_entry." + id + ".description");
        this.descriptionLines = Minecraft.getInstance().font.split(this.description, 163);
        this.tips = Component.translatable("codex.list_entry." + id + ".tips");
        this.tipsLines = Minecraft.getInstance().font.split(this.tips, 163);
    }
}
