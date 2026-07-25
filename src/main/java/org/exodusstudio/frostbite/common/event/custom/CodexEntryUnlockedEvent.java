package org.exodusstudio.frostbite.common.event.custom;

import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.Event;
import org.exodusstudio.frostbite.client.codex.entries.CodexEntry;

public class CodexEntryUnlockedEvent extends Event {
    private final Player player;
    private final CodexEntry entry;

    public CodexEntryUnlockedEvent(Player player, CodexEntry entry) {
        this.player = player;
        this.entry = entry;
    }

    public Player getPlayer() {
        return player;
    }

    public CodexEntry getEntry() {
        return entry;
    }
}
