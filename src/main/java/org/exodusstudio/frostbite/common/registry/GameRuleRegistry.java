package org.exodusstudio.frostbite.common.registry;


import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRules;

public class GameRuleRegistry {
    public static GameRule<Boolean> RULE_SPAWN_FROZEN_REMNANTS;

    public static void register() {
        RULE_SPAWN_FROZEN_REMNANTS =
                GameRules.registerBoolean("frostbite:spawnFrozenRemnantsIfNotKeepInventory", GameRuleCategory.PLAYER, true);
    }
}
