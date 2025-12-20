package org.exodusstudio.frostbite.common.registry;


import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;

public class GameRuleRegistry {
    public static final DeferredRegister<GameRule<?>> GAME_RULES =
            DeferredRegister.create(BuiltInRegistries.GAME_RULE, Frostbite.MOD_ID);
    public static DeferredHolder<GameRule<?>, GameRule<Boolean>> RULE_SPAWN_FROZEN_REMNANTS =
            GAME_RULES.register("spawn_frozen_remnants_if_not_keep_inventory", () -> new GameRule<>(
                GameRuleCategory.PLAYER,
                GameRuleType.BOOL,
                BoolArgumentType.bool(),
                GameRuleTypeVisitor::visitBoolean,
                Codec.BOOL,
                (p_460985_) -> p_460985_ ? 1 : 0,
                true,
                FeatureFlagSet.of()
            ));
}
