package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record EntryContext(Level level, Player player) {
}
