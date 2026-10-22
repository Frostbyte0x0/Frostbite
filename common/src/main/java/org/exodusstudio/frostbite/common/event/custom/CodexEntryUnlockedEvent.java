package org.exodusstudio.frostbite.common.event.custom;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.client.codex.entries.CodexEntry;

import java.util.function.Consumer;

public class CodexEntryUnlockedEvent {
    public static final Event<Consumer<CodexEntryUnlockedEvent>> EVENT = EventFactory.createConsumerLoop();
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
