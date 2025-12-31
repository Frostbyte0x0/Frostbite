package org.exodusstudio.frostbite.common.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.exodusstudio.frostbite.Frostbite;

public class AttributeRegistry {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, Frostbite.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> COLD_PROTECTION = ATTRIBUTES.register("cold_protection",
            () -> (new RangedAttribute("attribute.name.cold_protection", 0, 0, 30).setSyncable(true)));

}
