package org.exodusstudio.frostbite.common.registry;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;

public class KeyMappingRegistry {
    public static final KeyMapping.Category FROSTBITE =
            new KeyMapping.Category(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "frostbite"));
    public static final KeyMapping CODEX = new KeyMapping("key.codex", InputConstants.KEY_I, FROSTBITE);
    public static final KeyMapping PARRY = new KeyMapping("key.frostbite.parry", InputConstants.KEY_G, FROSTBITE);
}
