package org.exodusstudio.frostbite.common.item.armour;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.AttributeRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ArmourSets {
    public static final HashMap<String, ArmourSet> SETS = new HashMap<>();
    public static final HashMap<Item, String> CRAFTS = new HashMap<>();

    public static final ArmourSet CURSED = of(
            "cursed",
            Optional.empty(),
            new LinkedHashMap<>() {{
                put(AttributeRegistry.LIFE_STEAL, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "cursed_life_steal"), 0.3, AttributeModifier.Operation.ADD_VALUE));
                put(AttributeRegistry.TEMPERATURE_STEAL, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "cursed_temperature_steal"), 0.2, AttributeModifier.Operation.ADD_VALUE));
                put(Attributes.MAX_HEALTH, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "cursed_health"), -2, AttributeModifier.Operation.ADD_VALUE));
            }}
    );

    public static final ArmourSet TOXIC = of(
            "toxic",
            Optional.of(ItemRegistry.TOXIC_JELLY.asItem()),
            new LinkedHashMap<>() {{
                put(AttributeRegistry.POISON, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "toxic_poison"), 10, AttributeModifier.Operation.ADD_VALUE));
                put(AttributeRegistry.MELEE_DAMAGE, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "toxic_melee_damage"), 0.2, AttributeModifier.Operation.ADD_VALUE));
                put(Attributes.MAX_HEALTH, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "toxic_health"), -2, AttributeModifier.Operation.ADD_VALUE));
            }}
    );

    public static final ArmourSet BEAR = of(
            "bear",
            Optional.of(ItemRegistry.STRENGTHENED_BEAR_FUR.asItem()),
            new LinkedHashMap<>() {{
                put(AttributeRegistry.MELEE_DAMAGE, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "bear_melee_damage"), 0.3, AttributeModifier.Operation.ADD_VALUE));
                put(AttributeRegistry.DEFENCE, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "bear_defence"), 0.2, AttributeModifier.Operation.ADD_VALUE));
                put(Attributes.MOVEMENT_SPEED, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "bear_speed"), -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }}
    );

    public static final ArmourSet REBOUND = of(
            "rebound",
            Optional.of(ItemRegistry.REBOUND_INGOT.asItem()),
            new LinkedHashMap<>() {{
                put(AttributeRegistry.THORNS, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "rebound_thorns"), 0.3, AttributeModifier.Operation.ADD_VALUE));
                put(AttributeRegistry.POISON, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "rebound_poison"), 5, AttributeModifier.Operation.ADD_VALUE));
                put(Attributes.ATTACK_SPEED, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "rebound_speed"), -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }}
    );

    public static final ArmourSet MAGE = of(
            "mage",
            Optional.of(ItemRegistry.CLOAK.asItem()),
            new LinkedHashMap<>() {{
                put(AttributeRegistry.SPELL_DAMAGE, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "mage_spell"), 0.3, AttributeModifier.Operation.ADD_VALUE));
                put(Attributes.MOVEMENT_SPEED, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "mage_speed"), 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                put(AttributeRegistry.MELEE_DAMAGE, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "mage_melee"), -0.1, AttributeModifier.Operation.ADD_VALUE));
            }}
    );

    public static final ArmourSet RONIN = of(
            "ronin",
            Optional.empty(),
            new LinkedHashMap<>() {{
                put(Attributes.ATTACK_SPEED, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "ronin_speed"), 0.3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                put(AttributeRegistry.JUMPS, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "ronin_jumps"), 2, AttributeModifier.Operation.ADD_VALUE));
                put(AttributeRegistry.SPELL_DAMAGE, new MobEffect.AttributeTemplate(
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "ronin_spell"), -0.1, AttributeModifier.Operation.ADD_VALUE));
            }}
    );

    private static ArmourSet of(
            String id,
            Optional<Item> craft,
            Map<Holder<Attribute>, MobEffect.AttributeTemplate> attributes
    ) {
        ArmourSet set = new ArmourSet(id, attributes);
        SETS.put(set.id(), set);
        craft.ifPresent(item -> CRAFTS.put(item, id));
        return set;
    }
}

// Some sets spawn as is, others are crafted in weaving table with cutouts with materials dropped from various sources
//
// Level 2-3 sets (diamond) (+2-1 stats):
//  - Cursed:
//     +30% Life Steal
//     +20% Temperature Steal
//     -1 Heart
//  - Toxic:
//     +10s Poison
//     +20% Melee Damage
//     -1 Heart
//  - Bear:
//     +30% Cold Defence
//     +20% Defence
//     -10% Speed
//  - Rebound (found in runes):
//     +30% thorns
//     +5s poison
//     -10% Attack Speed
//  - Mage (from elf soldiers):
//     +30% Spell Damage
//     +20% Speed
//     -10% Melee Damage
//  - Ronin (from goat soldiers):
//     +30% Attack Speed
//     +2 Jumps
//     -10% Spell Damage
//
// Level 4-5 sets (netherite) (+3-1 stats, 1 ability): (build on previous sets, need mage for sorcerer, etc)
//  - Viper (Cursed):
//     +60% Life Steal
//     +
//     +5s Poison
//     -No health regeneration
//     A:
//  - Black Ice:
//     +
//     +
//     +
//     -
//     A:
//  - Elden:
//     +
//     +
//     +
//     -
//     A:
//  - Sorcerer (from elf general, Mage):
//     +60% spell damage
//     +
//     +
//     -
//     A:
//  - Samurai (from goat general, Ronin):
//     +
//     +
//     +
//     -
//     A:
//
// Level 6 sets (above netherite) (+4 stats, 1 ability, 1 passive aura):
//  - Frostbitten:
//     +
//     Scale with cold
//     Aura to steal cold and heat opponent
//
//  - Sweltering:
//     +
//     Scale with extreme heat
//     Aura to steal heat and cool opponent
