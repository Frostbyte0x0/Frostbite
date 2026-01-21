package org.exodusstudio.frostbite.client.codex;

import org.exodusstudio.frostbite.client.codex.entries.ListCodexEntry;
import org.exodusstudio.frostbite.client.codex.entries.TreeCodexEntry;
import org.exodusstudio.frostbite.client.codex.formations.CircleCodexFormation;
import org.exodusstudio.frostbite.client.codex.formations.TreeCodexFormation;
import org.exodusstudio.frostbite.client.codex.tabs.CodexTab;
import org.exodusstudio.frostbite.client.codex.tabs.CodexTabType;
import org.exodusstudio.frostbite.client.codex.tabs.ListCodexTab;
import org.exodusstudio.frostbite.client.codex.tabs.TreeCodexTab;

import java.util.List;

public class Codex {
    // Formations
    public static final TreeCodexFormation SPECIES_TREE = new TreeCodexFormation(0, 0);
    public static final CircleCodexFormation BIOME_CIRCLE = new CircleCodexFormation(0, 0, 100);
    public static final CircleCodexFormation HOT_CIRCLE = new CircleCodexFormation(0, 0, 100);


    // Entries
    // General
    public static final ListCodexEntry GENERAL_ENTRY = new ListCodexEntry("general");
    // Tree
    public static final TreeCodexEntry ELF_GENERAL = new TreeCodexEntry("elf_general", null, "The Elf General", SPECIES_TREE);
    public static final TreeCodexEntry CASTER_ELF = new TreeCodexEntry("caster_elf", ELF_GENERAL, "The Caster Elf", SPECIES_TREE);
    public static final TreeCodexEntry HEALER_ELF = new TreeCodexEntry("healer_elf", ELF_GENERAL, "The Healer Elf", SPECIES_TREE);
    public static final TreeCodexEntry SUMMONER_ELF = new TreeCodexEntry("summoner_elf", ELF_GENERAL, "The Summoner Elf", SPECIES_TREE);
    // Enemies
    public static final ListCodexEntry ENEMY_ENTRY = new ListCodexEntry("enemy");
    // Biomes
    public static final ListCodexEntry BIOME_ENTRY = new ListCodexEntry("biome");
    // Structures
    public static final ListCodexEntry STRUCTURE_ENTRY = new ListCodexEntry("structure");


    // Tabs
    public static final CodexTab GENERAL_TAB = new ListCodexTab("General", CodexTabType.ABOVE, 0, "item/advanced_clock",
            Codex.GENERAL_ENTRY
    );
    public static final CodexTab BOSSES_TAB = new TreeCodexTab("Bosses", CodexTabType.ABOVE, 1, "item/casting_staff",
            Codex.ELF_GENERAL,
            Codex.SUMMONER_ELF,
            Codex.CASTER_ELF,
            Codex.HEALER_ELF
    );
    public static final CodexTab ENEMIES_TAB = new ListCodexTab("Enemies", CodexTabType.ABOVE, 2, "item/fire");
    public static final CodexTab BIOMES_TAB = new ListCodexTab("Biomes", CodexTabType.ABOVE, 3, "item/frostbitten_gem");
    public static final CodexTab STRUCTURES_TAB = new ListCodexTab("Structures", CodexTabType.ABOVE, 4, "item/frozen_arrow");

    public static final List<CodexTab> TABS = List.of(
            GENERAL_TAB,
            BOSSES_TAB,
            ENEMIES_TAB,
            BIOMES_TAB,
            STRUCTURES_TAB
    );
}
