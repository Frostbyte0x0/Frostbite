package org.exodusstudio.frostbite.common.registry;

import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;

public class KeyMappingRegistry {
    public static final KeyMapping.Category FROSTBITE =
            new KeyMapping.Category(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "frostbite"));
    public static final KeyMapping CODEX = new KeyMapping("key.codex", 79, FROSTBITE);
}
