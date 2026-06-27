package org.exodusstudio.frostbite.client.codex;

import org.exodusstudio.frostbite.client.codex.entries.ListCodexEntry;
import org.exodusstudio.frostbite.client.codex.entries.TargetCodexEntry;
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
    public static final CircleCodexFormation BIOME_CIRCLE = new CircleCodexFormation(150, 200, 75);
    public static final CircleCodexFormation HOT_CIRCLE = new CircleCodexFormation(0, 0, 100);


    // Entries
    // General
    public static final ListCodexEntry TEMPERATURE_ENTRY = new ListCodexEntry("general");
    // Targets
    public static final TargetCodexEntry GENERAL = new TargetCodexEntry("general", null, "The General The General The General The General The General The General The General The General The General The General The General The General The General The General The General The General The General The General The General The General The General ", SPECIES_TREE);
    public static final TargetCodexEntry ELF_GENERAL = new TargetCodexEntry("elf_general", GENERAL, "The Elf General", SPECIES_TREE);
    public static final TargetCodexEntry CASTER_ELF = new TargetCodexEntry("caster_elf", ELF_GENERAL, "The Caster Elf", SPECIES_TREE);
    public static final TargetCodexEntry HEALER_ELF = new TargetCodexEntry("healer_elf", ELF_GENERAL, "The Healer Elf", SPECIES_TREE);
    public static final TargetCodexEntry SUMMONER_ELF = new TargetCodexEntry("summoner_elf", ELF_GENERAL, "The Summoner Elf", SPECIES_TREE);
    public static final TargetCodexEntry ELF_GENERAL2 = new TargetCodexEntry("elf_general2", GENERAL, "The Elf General", SPECIES_TREE);
    public static final TargetCodexEntry CASTER_ELF2 = new TargetCodexEntry("caster_elf2", ELF_GENERAL2, "The Caster Elf", SPECIES_TREE);
    public static final TargetCodexEntry CASTER_ELF3 = new TargetCodexEntry("caster_elf3", ELF_GENERAL2, "The Caster Elf", SPECIES_TREE);
    public static final TargetCodexEntry HEALER_ELF2 = new TargetCodexEntry("healer_elf2", ELF_GENERAL2, "The Healer Elf", SPECIES_TREE);
    public static final TargetCodexEntry SUMMONER_ELF2 = new TargetCodexEntry("summoner_elf2", ELF_GENERAL2, "The Summoner Elf", SPECIES_TREE);

    public static final TargetCodexEntry SHAMAN = new TargetCodexEntry("shaman", null, "The Shaman", BIOME_CIRCLE);
    public static final TargetCodexEntry MONK = new TargetCodexEntry("monk", null, "The Monk", BIOME_CIRCLE);
    public static final TargetCodexEntry MONK1 = new TargetCodexEntry("summoner_elf", null, "The Summoner Elf", BIOME_CIRCLE);
    public static final TargetCodexEntry MONK2 = new TargetCodexEntry("summoner_elf", null, "The Summoner Elf", BIOME_CIRCLE);
    public static final TargetCodexEntry MONK3 = new TargetCodexEntry("summoner_elf", null, "The Summoner Elf", BIOME_CIRCLE);
    public static final TargetCodexEntry MONK4 = new TargetCodexEntry("summoner_elf", null, "The Summoner Elf", BIOME_CIRCLE);
    public static final TargetCodexEntry MONK5 = new TargetCodexEntry("summoner_elf", null, "The Summoner Elf", BIOME_CIRCLE);
    public static final TargetCodexEntry MONK6 = new TargetCodexEntry("summoner_elf", null, "The Summoner Elf", BIOME_CIRCLE);
    // Entities
    public static final ListCodexEntry BIG_LEVITATING_JELLYFISH_ENTRY = new ListCodexEntry("big_levitating_jellyfish");
    public static final ListCodexEntry BOAR_ENTRY = new ListCodexEntry("boar");
    // Biomes
    public static final ListCodexEntry SHROUDED_FOREST_ENTRY = new ListCodexEntry("shrouded_forest");
    public static final ListCodexEntry ICEBOUND_PLAINS_ENTRY = new ListCodexEntry("icebound_plains");
    public static final ListCodexEntry FRIGID_PEAKS_ENTRY = new ListCodexEntry("frigid_peaks");
    public static final ListCodexEntry CHARM_WOODS_ENTRY = new ListCodexEntry("charm_woods");
    // Structures
    public static final ListCodexEntry PORTAL_ENTRY = new ListCodexEntry("portal");


    // Tabs
    public static final CodexTab GENERAL_TAB = new ListCodexTab("General", CodexTabType.ABOVE, 0, "item/advanced_clock",
            Codex.TEMPERATURE_ENTRY
    );
    public static final CodexTab BOSSES_TAB = new TreeCodexTab("Bosses", CodexTabType.ABOVE, 1, "item/casting_staff",
            Codex.GENERAL,
            Codex.ELF_GENERAL,
            Codex.SUMMONER_ELF,
            Codex.CASTER_ELF,
            Codex.HEALER_ELF,
            Codex.ELF_GENERAL2,
            Codex.CASTER_ELF2,
            Codex.CASTER_ELF3,
            Codex.HEALER_ELF2,
            Codex.SUMMONER_ELF2,
            Codex.SHAMAN,
            Codex.MONK,
            Codex.MONK1,
            Codex.MONK2,
            Codex.MONK3,
            Codex.MONK4,
            Codex.MONK5,
            Codex.MONK6
    );
    public static final CodexTab ENTITIES_TAB = new ListCodexTab("Entities", CodexTabType.ABOVE, 2, "item/fire");
    public static final CodexTab BIOMES_TAB = new ListCodexTab("Biomes", CodexTabType.ABOVE, 3, "item/frostbitten_gem");
    public static final CodexTab STRUCTURES_TAB = new ListCodexTab("Structures", CodexTabType.ABOVE, 4, "item/frozen_arrow");

    public static final List<CodexTab> TABS = List.of(
            GENERAL_TAB,
            BOSSES_TAB,
            ENTITIES_TAB,
            BIOMES_TAB,
            STRUCTURES_TAB
    );
}
