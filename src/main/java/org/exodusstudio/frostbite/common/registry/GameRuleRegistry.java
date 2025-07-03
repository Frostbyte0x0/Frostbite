package org.exodusstudio.frostbite.common.registry;

import net.minecraft.world.level.GameRules;

public class GameRuleRegistry {
    public static GameRules.Key<GameRules.BooleanValue> RULE_SPAWN_FROZEN_REMNANTS;

    public static void register() {
        RULE_SPAWN_FROZEN_REMNANTS =
                GameRules.register("frostbite:spawnFrozenRemnantsIfNotKeepInventory", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));
    }
}
