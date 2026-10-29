package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.exodusstudio.frostbite.Frostbite;

public class Tags {
    public static TagKey<Structure> STRUCTURE_OTF = create("otf");
    public static final TagKey<Item> PARRY_WEAPONS = TagKey.create(
            Registries.ITEM, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "parry_weapons"));

    private static TagKey<Structure> create(String name) {
        return TagKey.create(Registries.STRUCTURE, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, name));
    }
}
