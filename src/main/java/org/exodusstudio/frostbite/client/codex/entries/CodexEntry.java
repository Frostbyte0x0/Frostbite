package org.exodusstudio.frostbite.client.codex.entries;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.codex.Codex;
import org.exodusstudio.frostbite.client.codex.CodexEntryToast;
import org.exodusstudio.frostbite.common.util.Util;

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
        if (!Util.getString(player, "unlocked_entries").contains(entry.id)) {
            Util.setData(player, "unlocked_entries", Util.getString(player, "unlocked_entries") + entry.id + ";");
            Minecraft.getInstance().gui.toastManager().addToast(new CodexEntryToast(entry));
        }
    }

    public static void addEntryToPlayer(Player player, String entryId) {
        if (!Util.getString(player, "unlocked_entries").contains(entryId)) {
            Util.setData(player, "unlocked_entries", Util.getString(player, "unlocked_entries") + entryId + ";");
            Minecraft.getInstance().gui.toastManager().addToast(new CodexEntryToast(Codex.ENTRIES.get(entryId)));
        }
    }

    public static boolean playerHasEntry(Player player, CodexEntry entry) {
        return Arrays.stream((Util.getString(player, "unlocked_entries").split(";"))).anyMatch(s -> s.equals(entry.id));
    }
}
