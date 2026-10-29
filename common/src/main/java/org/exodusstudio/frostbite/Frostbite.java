package org.exodusstudio.frostbite;

import com.mojang.logging.LogUtils;
import net.minecraft.server.MinecraftServer;
import org.exodusstudio.frostbite.common.event.ModEventBusEvents;
import org.exodusstudio.frostbite.common.event.ModEvents;
import org.exodusstudio.frostbite.common.network.NetworkRegistry;
import org.exodusstudio.frostbite.common.registry.*;
import org.slf4j.Logger;

public final class Frostbite {
    public static final String MOD_ID = "frostbite";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static MinecraftServer SERVER = null;

    public static void init() {
        DataComponentTypeRegistry.DATA_COMPONENT_TYPES.register();
        AttributeRegistry.ATTRIBUTES.register();
        EffectRegistry.MOB_EFFECTS.register();
        SoundRegistry.SOUND_EVENTS.register();
        ParticleRegistry.PARTICLE_TYPES.register();
        ConsumeEffectRegistry.CONSUME_EFFECT_TYPES.register();
        EntityRegistry.ENTITY_TYPES.register();
        EntityRegistry.SENSOR.register();
        MemoryModuleTypeRegistry.MEMORY_MODULE_TYPES.register();
        BlockRegistry.BLOCKS.register();
        ItemRegistry.ITEMS.register();
        BlockEntityRegistry.BLOCK_ENTITY_TYPES.register();
        CreativeModeTabRegistry.CREATIVE_MODE_TABS.register();
        MenuTypeRegistry.MENU_TYPES.register();
        FoliagePlacerRegistry.FOLIAGE_PLACER_TYPES.register();
        FeatureRegistry.FEATURE_TYPES.register();
        StructureRegistry.STRUCTURES.register();
        GameRuleRegistry.GAME_RULES.register();

        NetworkRegistry.init();
        ModEventBusEvents.init();
        ModEvents.init();
    }
}
