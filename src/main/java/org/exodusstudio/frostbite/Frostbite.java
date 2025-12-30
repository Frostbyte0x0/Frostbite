package org.exodusstudio.frostbite;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.exodusstudio.frostbite.common.block.renderers.LodestarRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.*;
import org.exodusstudio.frostbite.common.entity.client.renderers.bullet.RevolverBulletRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.bullet.SniperBulletRenderer;
import org.exodusstudio.frostbite.common.registry.*;
import org.exodusstudio.frostbite.common.util.BreathEntityLike;
import org.exodusstudio.frostbite.common.util.HeaterStorage;
import org.exodusstudio.frostbite.common.util.ModItemProperties;
import org.exodusstudio.frostbite.common.util.TemperatureStorage;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod(Frostbite.MOD_ID)
public class Frostbite {
    public static final String MOD_ID = "frostbite";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static TemperatureStorage temperatureStorage = TemperatureStorage.init();
    public static List<HeaterStorage> heaterStorages = new ArrayList<>();
    public static List<HeaterStorage> heatersToRemove = new ArrayList<>();
    public static List<BreathEntityLike> breathEntityLikes = new ArrayList<>();
    public static List<BreathEntityLike> breathEntityLikesToRemove = new ArrayList<>();
    public static boolean shouldShowLining = false;
    public static BlockPos frostbiteSpawnPoint = BlockPos.ZERO;
    public static BlockPos overworldSpawnPoint = BlockPos.ZERO;
    public static WeatherInfo weatherInfo = new WeatherInfo();
    public static final HashMap<BlockPos, EntityType<?>> addedBosses = new HashMap<>();
    public static final HashMap<BlockPos, EntityType<?>> bossesToAdd = new HashMap<>();

    public Frostbite(IEventBus modEventBus, ModContainer ignored) {
        DataComponentTypeRegistry.DATA_COMPONENT_TYPES.register(modEventBus);
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        CreativeModeTabRegistry.CREATIVE_MODE_TABS.register(modEventBus);
        EntityRegistry.ENTITY_TYPES.register(modEventBus);
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
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(EntityRegistry.SNIPER_BULLET_ENTITY.get(), SniperBulletRenderer::new);
            EntityRenderers.register(EntityRegistry.REVOLVER_BULLET_ENTITY.get(), RevolverBulletRenderer::new);
            EntityRenderers.register(EntityRegistry.RAIN_FROG.get(), RainFrogRenderer::new);
            EntityRenderers.register(EntityRegistry.WOOLLY_SHEEP.get(), WoollySheepRenderer::new);
            EntityRenderers.register(EntityRegistry.DRAIN_CIRCLE.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.HAILCOIL.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.LAST_STAND.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.ICE_BLOCK.get(), FallingBlockRenderer::new);
            EntityRenderers.register(EntityRegistry.ICE_SPIKE.get(), IceSpikeRenderer::new);
            EntityRenderers.register(EntityRegistry.EXPLODING_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.BLUE_HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.PACKED_HARDENED_SNOWBALL_PROJECTILE_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(EntityRegistry.LEVITATING_JELLYFISH.get(), LevitatingJellyfishRenderer::new);
            EntityRenderers.register(EntityRegistry.FERAL_WOLF.get(), FeralWolfRenderer::new);
            EntityRenderers.register(EntityRegistry.FROZEN_REMNANTS.get(), FrozenRemnantsRenderer::new);
            EntityRenderers.register(EntityRegistry.WIND_CIRCLE.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.HEALING_CIRCLE.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.BOAR.get(), BoarRenderer::new);
            EntityRenderers.register(EntityRegistry.ROAMING_BLIZZARD.get(), GenericEntityRenderer::new);
            EntityRenderers.register(EntityRegistry.ICED_SKELETON.get(), IcedSkeletonRenderer::new);
            EntityRenderers.register(EntityRegistry.ICED_ZOMBIE.get(), IcedZombieRenderer::new);
            EntityRenderers.register(EntityRegistry.ICED_CREEPER.get(), IcedCreeperRenderer::new);
            EntityRenderers.register(EntityRegistry.FROZEN_ARROW.get(), FrozenArrowRenderer::new);

            BlockEntityRenderers.register(BlockEntityRegistry.LODESTAR.get(), LodestarRenderer::new);
            EntityRenderers.register(EntityRegistry.SPECTER.get(), SpecterRenderer::new);
            EntityRenderers.register(EntityRegistry.REVENANT.get(), RevenantRenderer::new);
            EntityRenderers.register(EntityRegistry.BANDIT.get(), BanditRenderer::new);
            EntityRenderers.register(EntityRegistry.TORCH.get(), TorchRenderer::new);
            EntityRenderers.register(EntityRegistry.FIRE_SLICE.get(), FireSliceRenderer::new);
            EntityRenderers.register(EntityRegistry.TANUKI.get(), TanukiRenderer::new);
            EntityRenderers.register(EntityRegistry.HEALER_ELF.get(), HealerElfRenderer::new);
            EntityRenderers.register(EntityRegistry.CASTER_ELF.get(), CasterElfRenderer::new);
            EntityRenderers.register(EntityRegistry.SUMMONER_ELF.get(), SummonerElfRenderer::new);
            EntityRenderers.register(EntityRegistry.BOREAL_BEAR.get(), BorealBearRenderer::new);
            EntityRenderers.register(EntityRegistry.MONK.get(), MonkRenderer::new);
            EntityRenderers.register(EntityRegistry.BIG_LEVITATING_JELLYFISH.get(), BigLevitatingJellyfishRenderer::new);
            EntityRenderers.register(EntityRegistry.BARD.get(), BardRenderer::new);
            EntityRenderers.register(EntityRegistry.ETHEREAL_SWORD.get(), EtherealSwordRenderer::new);
            EntityRenderers.register(EntityRegistry.ETHEREAL_HANDS.get(), EtherealHandsRenderer::new);
            EntityRenderers.register(EntityRegistry.ETHEREAL_HAMMER.get(), EtherealHammerRenderer::new);
            EntityRenderers.register(EntityRegistry.GUARD.get(), GuardRenderer::new);
            EntityRenderers.register(EntityRegistry.CHIEF_GUARD.get(), ChiefGuardRenderer::new);
            EntityRenderers.register(EntityRegistry.HEAVY_GUARD.get(), HeavyGuardRenderer::new);
            ModItemProperties.addCustomItemProperties();
        }
    }
}
