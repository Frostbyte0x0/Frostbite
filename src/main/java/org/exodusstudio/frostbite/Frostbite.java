package org.exodusstudio.frostbite;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.exodusstudio.frostbite.common.registry.*;
import org.exodusstudio.frostbite.common.util.HeaterStorage;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Mod(Frostbite.MOD_ID)
public class Frostbite {
    public static final String MOD_ID = "frostbite";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static MinecraftServer SERVER = null;
    public static List<HeaterStorage> heaterStorages = new ArrayList<>();
    public static List<HeaterStorage> heatersToRemove = new ArrayList<>();
    public static final ResourceKey<Level> frostbiteKey =
            ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "frostbite"));

    public Frostbite(IEventBus modEventBus, ModContainer ignored) {
        DataComponentTypeRegistry.DATA_COMPONENT_TYPES.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        CreativeModeTabRegistry.CREATIVE_MODE_TABS.register(modEventBus);
        EntityRegistry.SENSOR.register(modEventBus);
        EffectRegistry.MOB_EFFECTS.register(modEventBus);
        SoundRegistry.SOUND_EVENTS.register(modEventBus);
        ParticleRegistry.PARTICLE_TYPES.register(modEventBus);
        MenuTypeRegistry.MENU_TYPES.register(modEventBus);
        ConsumeEffectRegistry.CONSUME_EFFECT_TYPES.register(modEventBus);
        FoliagePlacerRegistry.FOLIAGE_PLACER_TYPES.register(modEventBus);
        StructureRegistry.STRUCTURES.register(modEventBus);
        BlockEntityRegistry.BLOCK_ENTITY_TYPES.register(modEventBus);
        MemoryModuleTypeRegistry.MEMORY_MODULE_TYPES.register(modEventBus);
        GameRuleRegistry.GAME_RULES.register(modEventBus);
        AttributeRegistry.ATTRIBUTES.register(modEventBus);
        AttachmentRegistry.ATTACHMENT_TYPES.register(modEventBus);
    }
}
