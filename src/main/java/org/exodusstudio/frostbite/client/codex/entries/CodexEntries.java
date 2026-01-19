package org.exodusstudio.frostbite.client.codex.entries;

import java.util.List;

public class CodexEntries {
    public static final List<CodexEntry> ALL_ENTRIES = List.of(
            // GENERAL
            new ListCodexEntry("getting_started"),
            new ListCodexEntry("frostbite_effects"),

            // TREE
            new ListCodexEntry("freezing_mechanics"),

            // ENNEMIES
            new ListCodexEntry("cold_biomes"),

            // BIOMES
            new ListCodexEntry("survival_tips")
    );

    // GENERAL
    public static final ListCodexEntry GENERAL = new ListCodexEntry("general");

    // TREE
    public static final TreeCodexEntry TREE = new TreeCodexEntry("tree", null);

    // ENEMIES
    public static final ListCodexEntry ENEMY = new ListCodexEntry("enemy");

    // BIOMES
    public static final ListCodexEntry BIOME = new ListCodexEntry("biome");
}
