package org.exodusstudio.frostbite.common.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.storage.loot.LootTable;
import org.exodusstudio.frostbite.Frostbite;

public enum RuneLootLevel implements StringRepresentable {
    WOOD(50),
    STONE(100),
    IRON(150),
    GOLD(200),
    DIAMOND(250);

    final int maxHealth;
    final ResourceKey<LootTable> lootTableKey;
    RuneLootLevel(int maxHealth) {
        this.maxHealth = maxHealth;
        this.lootTableKey = ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "chests/rune_" + getSerializedName()));
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public ResourceKey<LootTable> getLootTableKey() {
        return lootTableKey;
    }

    public RuneLootLevel next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase();
    }
}
