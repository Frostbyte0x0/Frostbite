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
import org.exodusstudio.frostbite.common.mixinterfaces.TE;

import java.util.*;

public class Codex {
    public static final Map<String, CodexEntry> ENTRIES = new HashMap<>();

    // Formations
    public static final TreeCodexFormation ELF_TREE = new TreeCodexFormation(0, 0);
    public static final TreeCodexFormation GUARD_TREE = new TreeCodexFormation(0, 0);
    public static final CircleCodexFormation BIOME_CIRCLE = new CircleCodexFormation(150, 200, 75);


    // Entries
    // General
    public static final ListCodexEntry TEMPERATURE = of(new ListCodexEntry("temperature", c -> ((TE) c.player()).getInnerTemp() < 0));
    public static final ListCodexEntry STARSEEKING_COMPASS = of(new ListCodexEntry("starseeking_compass", _ -> true));
    public static final ListCodexEntry LINING = of(new ListCodexEntry("lining", c -> (c.player().getInventory().hasAnyOf(Set.of(ItemRegistry.HIDE.value())))));
    public static final ListCodexEntry CRYSTALITE = of(new ListCodexEntry("crystalite", c -> (c.player().getInventory().hasAnyOf(Set.of(ItemRegistry.MAGIC_DUST.value())))));
    public static final ListCodexEntry MAGIC_DUST = of(new ListCodexEntry("magic_dust", c -> (c.player().getInventory().hasAnyOf(Set.of(ItemRegistry.MAGIC_DUST.value())))));
    public static final ListCodexEntry STAFFS = of(new ListCodexEntry("staffs", c -> (c.player().getInventory().hasAnyOf(Set.of(ItemRegistry.MAGIC_DUST.value()))))); // TODO: add staff tag for this

    // Targets
    public static final TargetCodexEntry ELF_GENERAL = of(new TargetCodexEntry("elf_general", null, ELF_TREE, Optional.of(ItemRegistry.BOTTLE_OF_HEAT.get())));
    public static final TargetCodexEntry CASTER_ELF = of(new TargetCodexEntry("caster_elf", ELF_GENERAL, ELF_TREE, Optional.of(ItemRegistry.CASTING_STAFF.get())));
    public static final TargetCodexEntry HEALER_ELF = of(new TargetCodexEntry("healer_elf", ELF_GENERAL, ELF_TREE, Optional.of(ItemRegistry.HEALING_STAFF.get())));
    public static final TargetCodexEntry SUMMONER_ELF = of(new TargetCodexEntry("summoner_elf", ELF_GENERAL, ELF_TREE, Optional.of(ItemRegistry.SUMMONING_STAFF.get())));
    public static final TargetCodexEntry CHIEF_GUARD = of(new TargetCodexEntry("chief_guard", null, GUARD_TREE, Optional.of(ItemRegistry.BOTTLE_OF_HEAT.get())));
    public static final TargetCodexEntry GUARD = of(new TargetCodexEntry("guard", CHIEF_GUARD, GUARD_TREE, Optional.of(ItemRegistry.CASTING_STAFF.get())));
    public static final TargetCodexEntry RANGED_GUARD = of(new TargetCodexEntry("ranged_guard", CHIEF_GUARD, GUARD_TREE, Optional.of(ItemRegistry.HEALING_STAFF.get())));
    public static final TargetCodexEntry HEAVY_GUARD = of(new TargetCodexEntry("heavy_guard", CHIEF_GUARD, GUARD_TREE, Optional.of(ItemRegistry.SUMMONING_STAFF.get())));

    public static final TargetCodexEntry SHAMAN = of(new TargetCodexEntry("shaman", null, BIOME_CIRCLE, Optional.of(ItemRegistry.BOTTLE_OF_HEAT.get())));
    public static final TargetCodexEntry MONK = of(new TargetCodexEntry("monk", null, BIOME_CIRCLE, Optional.of(ItemRegistry.BOTTLE_OF_HEAT.get())));

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
            Codex.TEMPERATURE,
            Codex.STARSEEKING_COMPASS,
            Codex.LINING,
            Codex.CRYSTALITE,
            Codex.MAGIC_DUST,
            Codex.STAFFS
    );
    public static final CodexTab BOSSES_TAB = new TreeCodexTab("bosses", TabType.ABOVE, 1, "item/casting_staff",
            Codex.ELF_GENERAL,
            Codex.SUMMONER_ELF,
            Codex.CASTER_ELF,
            Codex.HEALER_ELF,
            Codex.CHIEF_GUARD,
            Codex.GUARD,
            Codex.RANGED_GUARD,
            Codex.HEAVY_GUARD,
            Codex.SHAMAN,
            Codex.MONK
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

    public static String TRACKED_LIST_ENTRIES;

    static {
        StringBuilder builder = new StringBuilder();
        for (CodexTab tab : TABS) {
            if (tab instanceof ListCodexTab listTab) {
                for (CodexEntry entry : listTab.entries) {
                    if (entry instanceof ListCodexEntry listEntry) {
                        builder.append(listEntry.id).append(";");
                    }
                }
            }
        }
        TRACKED_LIST_ENTRIES = builder.toString();
    }
}
