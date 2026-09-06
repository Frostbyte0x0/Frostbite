package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.common.PercentageAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;

public class AttributeRegistry {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, Frostbite.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> COLD_PROTECTION = ATTRIBUTES.register("cold_protection",
            () -> (new RangedAttribute("attribute.name.cold_protection", 0, 0, 30).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> LIFE_STEAL = ATTRIBUTES.register("life_steal",
            () -> (new PercentageAttribute("attribute.name.life_steal", 0, 0, 1).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> TEMPERATURE_STEAL = ATTRIBUTES.register("temperature_steal",
            () -> (new PercentageAttribute("attribute.name.temperature_steal", 0, 0, 1).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> COLD_DEFENCE = ATTRIBUTES.register("cold_defence",
            () -> (new PercentageAttribute("attribute.name.cold_defence", 0, 0, 1).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> THORNS = ATTRIBUTES.register("thorns",
            () -> (new PercentageAttribute("attribute.name.thorns", 0, 0, 1).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> DEFENCE = ATTRIBUTES.register("defence",
            () -> (new PercentageAttribute("attribute.name.defence", 0, 0, 1).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> SPELL_DAMAGE = ATTRIBUTES.register("spell_damage",
            () -> (new PercentageAttribute("attribute.name.spell_damage", 0, 0, 1).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> MELEE_DAMAGE = ATTRIBUTES.register("melee_damage",
            () -> (new PercentageAttribute("attribute.name.melee_damage", 0, 0, 1).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> JUMPS = ATTRIBUTES.register("jumps",
            () -> (new RangedAttribute("attribute.name.jumps", 0, 0, 10).setSyncable(true)));

    public static final DeferredHolder<Attribute, Attribute> POISON = ATTRIBUTES.register("poison",
            () -> (new RangedAttribute("attribute.name.poison", 0, 0, 60).setSyncable(true)));
}
