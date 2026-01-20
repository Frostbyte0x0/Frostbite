package org.exodusstudio.frostbite.client.codex.tabs;

import net.minecraft.world.item.ItemStack;
import org.exodusstudio.frostbite.client.codex.entries.ListCodexEntry;

public class ListCodexTab extends CodexTab {
    public ListCodexTab(String title, CodexTabType type, int index, ItemStack icon, ListCodexEntry... entries) {
        super(title, type, index, icon);
    }
}
