package org.exodusstudio.frostbite.client.codex.tabs;

import org.exodusstudio.frostbite.client.codex.CodexTabType;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

import java.util.List;

public class CodexTabs {
    public static final CodexTab GENERAL = new CodexTab("general", CodexTabType.ABOVE, 0, ItemRegistry.ADVANCED_CLOCK.toStack());
    public static final CodexTab TREE = new CodexTab("tree", CodexTabType.ABOVE, 1, ItemRegistry.CASTING_STAFF.toStack());
    public static final CodexTab ENEMIES = new CodexTab("enemies", CodexTabType.ABOVE, 2, ItemRegistry.FIRE.toStack());
    public static final CodexTab BIOMES = new CodexTab("biomes", CodexTabType.ABOVE, 3, ItemRegistry.FROSTBITTEN_GEM.toStack());

    public static final List<CodexTab> TABS = List.of(
            GENERAL,
            TREE,
            ENEMIES,
            BIOMES
    );
}
