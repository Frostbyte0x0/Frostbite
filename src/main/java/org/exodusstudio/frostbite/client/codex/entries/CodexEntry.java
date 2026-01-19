package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.AttachementRegistry;

import java.util.Arrays;

public abstract class CodexEntry {
    public final String id;
    public final Component title;
    public final Identifier image;
    public int x;
    public float y;

    public CodexEntry(String id) {
        this.id = id;
        this.title = Component.translatable("codex.entry." + id);
        this.image = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/gui/codex/entries/" + id + ".png");
    }

    public static void addEntryToPlayer(Player player, CodexEntry entry) {
        if (!player.getData(AttachementRegistry.UNLOCKED_ENTRIES).contains(entry.id)) {
            player.setData(AttachementRegistry.UNLOCKED_ENTRIES, player.getData(AttachementRegistry.UNLOCKED_ENTRIES) + entry.id + ";");
        }
    }

    public static boolean playerHasEntry(Player player, CodexEntry entry) {
        return Arrays.stream((player.getData(AttachementRegistry.UNLOCKED_ENTRIES).split(";"))).anyMatch(s -> s.equals(entry.id));
    }
}
