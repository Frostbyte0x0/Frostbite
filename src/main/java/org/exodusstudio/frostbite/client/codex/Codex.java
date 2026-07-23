package org.exodusstudio.frostbite.client.codex;

import org.exodusstudio.frostbite.client.codex.entries.CodexEntry;
import org.exodusstudio.frostbite.client.codex.entries.ListCodexEntry;
import org.exodusstudio.frostbite.client.codex.entries.TargetCodexEntry;
import org.exodusstudio.frostbite.client.codex.formations.CircleCodexFormation;
import org.exodusstudio.frostbite.client.codex.formations.TreeCodexFormation;
import org.exodusstudio.frostbite.client.codex.tabs.CodexTab;
import org.exodusstudio.frostbite.client.codex.tabs.TabType;
import org.exodusstudio.frostbite.client.codex.tabs.ListCodexTab;
import org.exodusstudio.frostbite.client.codex.tabs.TreeCodexTab;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.util.TE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Codex {
    public static final Map<String, CodexEntry> ENTRIES = new HashMap<>();

    // Formations
    public static final TreeCodexFormation SPECIES_TREE = new TreeCodexFormation(0, 0);
    public static final CircleCodexFormation BIOME_CIRCLE = new CircleCodexFormation(150, 200, 75);
    public static final CircleCodexFormation HOT_CIRCLE = new CircleCodexFormation(0, 0, 100);


    // Entries
    // General
    public static final ListCodexEntry TEMPERATURE_ENTRY = of(new ListCodexEntry("temperature", c -> ((TE) c.player()).getInnerTemp() < 0));
    // Targets
    public static final TargetCodexEntry GENERAL = of(new TargetCodexEntry("general", null, SPECIES_TREE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));
    public static final TargetCodexEntry ELF_GENERAL = of(new TargetCodexEntry("elf_general", GENERAL, SPECIES_TREE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));
    public static final TargetCodexEntry CASTER_ELF = of(new TargetCodexEntry("caster_elf", ELF_GENERAL, SPECIES_TREE, Optional.of(ItemRegistry.CASTING_STAFF.get())));
    public static final TargetCodexEntry HEALER_ELF = of(new TargetCodexEntry("healer_elf", ELF_GENERAL, SPECIES_TREE, Optional.of(ItemRegistry.HEALING_STAFF.get())));
    public static final TargetCodexEntry SUMMONER_ELF = of(new TargetCodexEntry("summoner_elf", ELF_GENERAL, SPECIES_TREE, Optional.of(ItemRegistry.SUMMONING_STAFF.get())));
    public static final TargetCodexEntry ELF_GENERAL2 = of(new TargetCodexEntry("elf_general2", GENERAL, SPECIES_TREE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));
    public static final TargetCodexEntry CASTER_ELF2 = of(new TargetCodexEntry("caster_elf2", ELF_GENERAL2, SPECIES_TREE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));
    public static final TargetCodexEntry CASTER_ELF3 = of(new TargetCodexEntry("caster_elf3", ELF_GENERAL2, SPECIES_TREE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));
    public static final TargetCodexEntry HEALER_ELF2 = of(new TargetCodexEntry("healer_elf2", ELF_GENERAL2, SPECIES_TREE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));
    public static final TargetCodexEntry SUMMONER_ELF2 = of(new TargetCodexEntry("summoner_elf2", ELF_GENERAL2, SPECIES_TREE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));

    public static final TargetCodexEntry SHAMAN = of(new TargetCodexEntry("shaman", null, BIOME_CIRCLE, Optional.of(ItemRegistry.METAL_COG.get())));
    public static final TargetCodexEntry MONK = of(new TargetCodexEntry("monk", null, BIOME_CIRCLE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));
    public static final TargetCodexEntry MONK1 = of(new TargetCodexEntry("monk1", null, BIOME_CIRCLE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));
    public static final TargetCodexEntry MONK2 = of(new TargetCodexEntry("monk2", null, BIOME_CIRCLE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));
    public static final TargetCodexEntry MONK3 = of(new TargetCodexEntry("monk3", null, BIOME_CIRCLE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));
    public static final TargetCodexEntry MONK4 = of(new TargetCodexEntry("monk4", null, BIOME_CIRCLE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));
    public static final TargetCodexEntry MONK5 = of(new TargetCodexEntry("monk5", null, BIOME_CIRCLE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));
    public static final TargetCodexEntry MONK6 = of(new TargetCodexEntry("monk6", null, BIOME_CIRCLE, Optional.of(ItemRegistry.ADVANCED_CLOCK.get())));
    // Entities
    public static final ListCodexEntry BIG_LEVITATING_JELLYFISH_ENTRY = of(new ListCodexEntry("big_levitating_jellyfish"));
    public static final ListCodexEntry BOAR_ENTRY = of(new ListCodexEntry("boar"));
    // Biomes
    public static final ListCodexEntry SHROUDED_FOREST_ENTRY = of(new ListCodexEntry("shrouded_forest"));
    public static final ListCodexEntry ICEBOUND_PLAINS_ENTRY = of(new ListCodexEntry("icebound_plains"));
    public static final ListCodexEntry FRIGID_PEAKS_ENTRY = of(new ListCodexEntry("frigid_peaks"));
    public static final ListCodexEntry CHARM_WOODS_ENTRY = of(new ListCodexEntry("charm_woods"));
    // Structures
    public static final ListCodexEntry PORTAL_ENTRY = of(new ListCodexEntry("otf_portal"));


    // Tabs
    public static final CodexTab GENERAL_TAB = new ListCodexTab("general", TabType.ABOVE, 0, "item/advanced_clock",
            Codex.TEMPERATURE_ENTRY
    );
    public static final CodexTab BOSSES_TAB = new TreeCodexTab("bosses", TabType.ABOVE, 1, "item/casting_staff",
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
    public static final CodexTab ENTITIES_TAB = new ListCodexTab("entities", TabType.ABOVE, 2, "item/fire",
            Codex.BIG_LEVITATING_JELLYFISH_ENTRY,
            Codex.BOAR_ENTRY
    );
    public static final CodexTab BIOMES_TAB = new ListCodexTab("biomes", TabType.ABOVE, 3, "item/frostbitten_gem",
            Codex.SHROUDED_FOREST_ENTRY,
            Codex.ICEBOUND_PLAINS_ENTRY,
            Codex.FRIGID_PEAKS_ENTRY,
            Codex.CHARM_WOODS_ENTRY,
            Codex.SHROUDED_FOREST_ENTRY,
            Codex.ICEBOUND_PLAINS_ENTRY,
            Codex.FRIGID_PEAKS_ENTRY,
            Codex.CHARM_WOODS_ENTRY
    );
    public static final CodexTab STRUCTURES_TAB = new ListCodexTab("structures", TabType.ABOVE, 4, "item/frozen_arrow",
            Codex.PORTAL_ENTRY
    );

    public static final List<CodexTab> TABS = List.of(
            GENERAL_TAB,
            BOSSES_TAB,
            ENTITIES_TAB,
            BIOMES_TAB,
            STRUCTURES_TAB
    );

    public static ListCodexEntry of(ListCodexEntry entry) {
        ENTRIES.put(entry.id, entry);
        return entry;
    }

    public static TargetCodexEntry of(TargetCodexEntry entry) {
        ENTRIES.put(entry.id, entry);
        return entry;
    }

    public static String TRACKED_LIST_ENTRIES = "";

    static {
        for (CodexTab tab : TABS) {
            if (tab instanceof ListCodexTab listTab) {
                for (CodexEntry entry : listTab.entries) {
                    if (entry instanceof ListCodexEntry listEntry) {
                        TRACKED_LIST_ENTRIES += listEntry.id + ";";
                    }
                }
            }
        }
    }
}
