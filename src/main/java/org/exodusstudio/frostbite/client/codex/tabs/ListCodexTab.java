package org.exodusstudio.frostbite.client.codex.tabs;

import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.client.codex.entries.ListCodexEntry;

public class ListCodexTab extends CodexTab {
    public ListCodexTab(String title, CodexTabType type, int index, Identifier icon, ListCodexEntry... entries) {
        super(title, type, index, icon);
    }
}
