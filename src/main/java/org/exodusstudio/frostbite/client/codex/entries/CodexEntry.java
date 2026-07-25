package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.codex.Codex;
import org.exodusstudio.frostbite.common.event.custom.CodexEntryUnlockedEvent;
import org.exodusstudio.frostbite.common.util.DataHelper;

import java.util.Arrays;

public abstract class CodexEntry {
    public final String id;
    public Component title;
    public final Identifier image;

    public CodexEntry(String id) {
        this.id = id;
        this.image = Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/codex/entries/" + id + ".png");
    }

    public static void addEntryToPlayer(Player player, CodexEntry entry) {
        if (!DataHelper.getString(player, "unlocked_entries").contains(entry.id)) {
            DataHelper.setData(player, "unlocked_entries", DataHelper.getString(player, "unlocked_entries") + entry.id + ";");
            NeoForge.EVENT_BUS.post(new CodexEntryUnlockedEvent(player, entry));
        }
    }

    public static void addEntryToPlayer(Player player, String entryId) {
        if (!DataHelper.getString(player, "unlocked_entries").contains(entryId) && Codex.ENTRIES.containsKey(entryId)) {
            DataHelper.setData(player, "unlocked_entries", DataHelper.getString(player, "unlocked_entries") + entryId + ";");
            NeoForge.EVENT_BUS.post(new CodexEntryUnlockedEvent(player, Codex.ENTRIES.get(entryId)));
        }
    }

    public static boolean playerHasEntry(Player player, CodexEntry entry) {
        return Arrays.stream((DataHelper.getString(player, "unlocked_entries").split(";"))).anyMatch(s -> s.equals(entry.id));
    }
}
