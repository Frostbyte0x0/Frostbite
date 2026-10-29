package org.exodusstudio.frostbite.neoforge.common.events;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.AttributeRegistry;
import org.exodusstudio.frostbite.neoforge.datagen.ModModelProvider;

@EventBusSubscriber(modid = Frostbite.MOD_ID)
public class FrostbiteNeoForgeBusEvents {
    @SubscribeEvent
    public static void modifyAttributes(EntityAttributeModificationEvent event) {
        for (Holder<Attribute> holder : AttributeRegistry.ATTRIBUTES.getEntries())
            event.add(EntityTypes.PLAYER, holder);
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(ModModelProvider::new);
    }
}
