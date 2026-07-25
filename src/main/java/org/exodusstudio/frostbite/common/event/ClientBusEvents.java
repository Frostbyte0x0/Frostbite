package org.exodusstudio.frostbite.common.event;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.monster.creeper.CreeperModel;
import net.minecraft.client.model.monster.skeleton.SkeletonModel;
import net.minecraft.client.model.monster.zombie.ZombieModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.gui.WeavingScreen;
import org.exodusstudio.frostbite.client.gui.scribing.ApplyingScreen;
import org.exodusstudio.frostbite.client.gui.scribing.CombiningScreen;
import org.exodusstudio.frostbite.client.gui.scribing.DecipheringScreen;
import org.exodusstudio.frostbite.client.overlays.*;
import org.exodusstudio.frostbite.common.block.renderers.LodestarRenderer;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.*;
import org.exodusstudio.frostbite.common.entity.client.models.bullet.RevolverBulletModel;
import org.exodusstudio.frostbite.common.entity.client.models.bullet.SniperBulletModel;
import org.exodusstudio.frostbite.common.entity.client.renderers.*;
import org.exodusstudio.frostbite.common.entity.client.renderers.bullet.RevolverBulletRenderer;
import org.exodusstudio.frostbite.common.entity.client.renderers.bullet.SniperBulletRenderer;
import org.exodusstudio.frostbite.common.entity.client.states.StateRenderState;
import org.exodusstudio.frostbite.common.entity.custom.helper.StateMonsterEntity;
import org.exodusstudio.frostbite.common.particle.*;
import org.exodusstudio.frostbite.common.registry.BlockEntityRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.MenuTypeRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;
import org.exodusstudio.frostbite.common.util.ModItemProperties;

import java.lang.reflect.InvocationTargetException;

@EventBusSubscriber(modid = Frostbite.MOD_ID, value = Dist.CLIENT)
public class ClientBusEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        EntityRenderers.register(EntityRegistry.SNIPER_BULLET_ENTITY.get(), SniperBulletRenderer::new);
        EntityRenderers.register(EntityRegistry.REVOLVER_BULLET_ENTITY.get(), RevolverBulletRenderer::new);
        EntityRenderers.register(EntityRegistry.RAIN_FROG.get(), RainFrogRenderer::new);
        EntityRenderers.register(EntityRegistry.WOOLLY_SHEEP.get(), WoollySheepRenderer::new);
        EntityRenderers.register(EntityRegistry.WHIRLPOOL.get(), GenericEntityRenderer::new);
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
        EntityRenderers.register(EntityRegistry.SPECTER.get(), SpecterRenderer::new);
        EntityRenderers.register(EntityRegistry.REVENANT.get(), RevenantRenderer::new);
        EntityRenderers.register(EntityRegistry.BANDIT.get(), BanditRenderer::new);
        EntityRenderers.register(EntityRegistry.TORCH.get(), TorchRenderer::new);
        EntityRenderers.register(EntityRegistry.FIRE_SLICE.get(), FireSliceRenderer::new);
        EntityRenderers.register(EntityRegistry.TANUKI.get(), TanukiRenderer::new);
        EntityRenderers.register(EntityRegistry.HEALER_ELF.get(), c -> new ElfRenderer(c, new HealerElfModel(c.bakeLayer(ModModelLayers.HEALER_ELF)), 0.45f, "healer_elf"));
        EntityRenderers.register(EntityRegistry.CASTER_ELF.get(), c -> new ElfRenderer(c, new CasterElfModel(c.bakeLayer(ModModelLayers.CASTER_ELF)), 0.45f, "caster_elf"));
        EntityRenderers.register(EntityRegistry.SUMMONER_ELF.get(), c -> new ElfRenderer(c, new SummonerElfModel(c.bakeLayer(ModModelLayers.SUMMONER_ELF)), 0.45f, "summoner_elf"));
        EntityRenderers.register(EntityRegistry.BOREAL_BEAR.get(), BorealBearRenderer::new);
        EntityRenderers.register(EntityRegistry.MONK.get(), MonkRenderer::new);
        EntityRenderers.register(EntityRegistry.BIG_LEVITATING_JELLYFISH.get(), BigLevitatingJellyfishRenderer::new);
        EntityRenderers.register(EntityRegistry.SHAMAN.get(), ShamanRenderer::new);
        EntityRenderers.register(EntityRegistry.ETHEREAL_SWORD.get(), EtherealSwordRenderer::new);
        EntityRenderers.register(EntityRegistry.ETHEREAL_HANDS.get(), EtherealHandsRenderer::new);
        EntityRenderers.register(EntityRegistry.ETHEREAL_HAMMER.get(), EtherealHammerRenderer::new);
        EntityRenderers.register(EntityRegistry.GUARD.get(), renderer(GuardModel.class, "guard", ModModelLayers.GUARD));
        EntityRenderers.register(EntityRegistry.CHIEF_GUARD.get(), renderer(ChiefGuardModel.class, "chief_guard", ModModelLayers.CHIEF_GUARD));
        EntityRenderers.register(EntityRegistry.HEAVY_GUARD.get(), renderer(HeavyGuardModel.class, "heavy_guard", ModModelLayers.HEAVY_GUARD));

        BlockEntityRenderers.register(BlockEntityRegistry.LODESTAR.get(), LodestarRenderer::new);
        EntityRenderers.register(EntityRegistry.CURSE_BALL.get(), CurseBallRenderer::new);
        ModItemProperties.addCustomItemProperties();
    }

    public static EntityRendererProvider<StateMonsterEntity> renderer(
            Class<? extends HumanoidModel<? extends StateRenderState>> clazz,
            String name,
            ModelLayerLocation modelLayer
    ) {
        return c -> {
            try {
                return new StateMonsterRenderer<>(c, clazz.getConstructor(ModelPart.class).newInstance(c.bakeLayer(modelLayer)), 0.45f,
                        Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/" + name + "/" + name + ".png"));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.SNIPER_BULLET, SniperBulletModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.REVOLVER_BULLET, RevolverBulletModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.RAIN_FROG, RainFrogModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ICE_SPIKE, IceSpikeModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.WOOLLY_SHEEP, WoollySheepModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CURSE_BALL, CurseBallModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.WOOLLY_SHEEP_FUR, WoollySheepFurModel::createFurLayer);
        event.registerLayerDefinition(ModModelLayers.HEAVY_GUARD, HeavyGuardModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.WOOLLY_SHEEP_BABY, () -> WoollySheepModel.createBodyLayer().apply(WoollySheepModel.BABY_TRANSFORMER));
        event.registerLayerDefinition(ModModelLayers.WOOLLY_SHEEP_BABY_FUR, () -> WoollySheepFurModel.createFurLayer().apply(WoollySheepModel.BABY_TRANSFORMER));
        event.registerLayerDefinition(ModModelLayers.GUARD, GuardModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CHIEF_GUARD, ChiefGuardModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ETHEREAL_HAMMER, EtherealHammerModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ETHEREAL_HANDS, EtherealHandsModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.ETHEREAL_SWORD, EtherealSwordModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.LEVITATING_JELLYFISH, LevitatingJellyfishModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SHAMAN, ShamanModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BIG_LEVITATING_JELLYFISH, BigLevitatingJellyfishModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.FROZEN_REMNANTS, FrozenRemnantsModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.FERAL_WOLF, FeralWolfModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BOAR, BoarModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MONK, MonkModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BOREAL_BEAR, BorealBearModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.LODESTAR_CAGE, LodestarRenderer::createCageLayer);
        event.registerLayerDefinition(ModModelLayers.SUMMONER_ELF, SummonerElfModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.CASTER_ELF, CasterElfModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.HEALER_ELF, HealerElfModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.TANUKI, TanukiModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.FIRE_SLICE, FireSliceModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.TORCH, TorchModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.BANDIT, BanditModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.REVENANT, RevenantModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.LODESTAR_EYE, LodestarRenderer::createEyeLayer);
        event.registerLayerDefinition(ModModelLayers.LODESTAR_SHELL, LodestarRenderer::createShellLayer);
        event.registerLayerDefinition(ModModelLayers.ICED_CREEPER, () -> CreeperModel.createBodyLayer(CubeDeformation.NONE));
        event.registerLayerDefinition(ModModelLayers.ICED_ZOMBIE,
                () -> LayerDefinition.create(ZombieModel.createMesh(CubeDeformation.NONE, 0), 64, 64));
        event.registerLayerDefinition(ModModelLayers.ICED_SKELETON, SkeletonModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.SPECTER, SpecterModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerOverlayEvent(RegisterGuiLayersEvent event) {
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "thermometer_overlay"),
                ThermometerOverlay::render);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "fire_overlay"),
                FireOverlay::render);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "combo_overlay"),
                ComboOverlay::render);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "gun_overlay"),
                GunOverlay::render);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "rage_overlay"),
                RageOverlay::render);
        event.registerAboveAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "lining_bar"),
                LiningBarOverlay::render);
        event.registerBelowAll(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "thermal_lens_overlay"),
                ThermalLensOverlay::render);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ParticleRegistry.WHIRLPOOL_PARTICLE.get(), WhirlpoolParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.SHOCKWAVE_PARTICLE.get(), ShockwaveParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.CONFETTI_PARTICLE.get(), ConfettiParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.EXPANDING_CIRCLE_PARTICLE.get(), ExpandingCircleParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.BUTTERFLY_PARTICLE.get(), ButterflyParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.SWIRLING_LEAF_PARTICLE.get(), SwirlingLeafParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.SNOWFLAKE_PARTICLE.get(), SnowflakeParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.ICY_BREATH_PARTICLE.get(), IcyBreathParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.ROAMING_BLIZZARD_PARTICLE.get(), RoamingBlizzardParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.DEBUG_PARTICLE.get(), DebugParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.HEAL_PARTICLE.get(), HealParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.AMBIENT_SNOW_PARTICLE.get(), AmbientSnowParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.DAMAGE_PARTICLE.get(), DamageParticle.Provider::new);
        event.registerSpriteSet(ParticleRegistry.SLASH_PARTICLE.get(), SlashParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(MenuTypeRegistry.WEAVING_MENU.get(), WeavingScreen::new);
        event.register(MenuTypeRegistry.DECIPHERING_MENU.get(), DecipheringScreen::new);
        event.register(MenuTypeRegistry.COMBINING_MENU.get(), CombiningScreen::new);
        event.register(MenuTypeRegistry.APPLYING_MENU.get(), ApplyingScreen::new);
    }
}
