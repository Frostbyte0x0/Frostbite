package org.exodusstudio.frostbite.client.codex;

import org.exodusstudio.frostbite.client.codex.entries.CodexEntry;
import org.exodusstudio.frostbite.client.codex.entries.ListCodexEntry;
import org.exodusstudio.frostbite.client.codex.entries.TreeCodexEntry;
import org.exodusstudio.frostbite.client.codex.formations.CircleCodexFormation;
import org.exodusstudio.frostbite.client.codex.formations.TreeCodexFormation;
import org.exodusstudio.frostbite.client.codex.tabs.CodexTab;
import org.exodusstudio.frostbite.client.codex.tabs.CodexTabType;
import org.exodusstudio.frostbite.client.codex.tabs.ListCodexTab;
import org.exodusstudio.frostbite.client.codex.tabs.TreeCodexTab;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

import java.util.List;

public class Codex {
    // General
    public static final ListCodexEntry GENERAL_ENTRY = new ListCodexEntry("general");
    // Tree
    public static final TreeCodexEntry TREE_ENTRY = new TreeCodexEntry("tree", null, "Tree!11!1!!");
    public static final TreeCodexEntry TREE2_ENTRY = new TreeCodexEntry("tree2", null, "Treeeeeeeeeeee\neeeeeeeeeeeeee\ne2!11!1!!");
    // Enemies
    public static final ListCodexEntry ENEMY_ENTRY = new ListCodexEntry("enemy");
    // Biomes
    public static final ListCodexEntry BIOME_ENTRY = new ListCodexEntry("biome");
    // Structures
    public static final ListCodexEntry STRUCTURE_ENTRY = new ListCodexEntry("structure");

    public static final List<CodexEntry> ENTRIES = List.of(
            GENERAL_ENTRY,
            TREE_ENTRY,
            TREE2_ENTRY,
            ENEMY_ENTRY,
            BIOME_ENTRY,
            STRUCTURE_ENTRY
    );

    // Formations
    public static final TreeCodexFormation SPECIES_TREE = new TreeCodexFormation(0, 0);
    public static final CircleCodexFormation BIOME_CIRCLE = new CircleCodexFormation(0, 0, 100);
    public static final CircleCodexFormation HOT_CIRCLE = new CircleCodexFormation(0, 0, 100);


    public static void setup() {

    }



    // Tabs
    public static final CodexTab GENERAL_TAB = new ListCodexTab("General", CodexTabType.ABOVE, 0, ItemRegistry.ADVANCED_CLOCK.toStack(),
            Codex.GENERAL_ENTRY
    );
    public static final CodexTab TREE_TAB = new TreeCodexTab("Tree", CodexTabType.ABOVE, 1, ItemRegistry.CASTING_STAFF.toStack(),
            Codex.TREE_ENTRY,
            Codex.TREE2_ENTRY
    );
    public static final CodexTab ENEMIES_TAB = new ListCodexTab("Enemies", CodexTabType.ABOVE, 2, ItemRegistry.FIRE.toStack());
    public static final CodexTab BIOMES_TAB = new ListCodexTab("Biomes", CodexTabType.ABOVE, 3, ItemRegistry.FROSTBITTEN_GEM.toStack());

    public static final List<CodexTab> TABS = List.of(
            GENERAL_TAB,
            TREE_TAB,
            ENEMIES_TAB,
            BIOMES_TAB
    );
}
