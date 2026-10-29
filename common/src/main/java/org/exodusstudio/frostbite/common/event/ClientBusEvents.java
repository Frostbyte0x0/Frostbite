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
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.overlays.FlashbangOverlay;
import org.exodusstudio.frostbite.common.block.renderers.LodestarRenderer;
import org.exodusstudio.frostbite.common.block.renderers.RuneRenderer;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.*;
import org.exodusstudio.frostbite.common.entity.client.renderers.*;
import org.exodusstudio.frostbite.common.entity.client.states.StateRenderState;
import org.exodusstudio.frostbite.common.entity.custom.helper.StateMonsterEntity;
import org.exodusstudio.frostbite.common.registry.BlockEntityRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.ModItemProperties;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ClientBusEvents {
    public static void init() {
        ClientEvents.init();
        FlashbangOverlay.init();
    }

    public static void registerRenderers() {
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
        EntityRenderers.register(EntityRegistry.IMPACT_PELLET.get(), ThrownItemRenderer::new);
        EntityRenderers.register(EntityRegistry.IMPACT_ZONE.get(), GenericEntityRenderer::new);
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
        EntityRenderers.register(EntityRegistry.RANGED_GUARD.get(), RangedGuardRenderer::new);
        EntityRenderers.register(EntityRegistry.PLAYER_ILLUSION.get(), PlayerIllusionRenderer::new);

        BlockEntityRenderers.register(BlockEntityRegistry.LODESTAR.get(), LodestarRenderer::new);
        BlockEntityRenderers.register(BlockEntityRegistry.RUNE.get(), RuneRenderer::new);
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

    public static void registerLayers(BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> registrar) {
        registrar.accept(ModModelLayers.ICE_SPIKE, IceSpikeModel::createBodyLayer);
        registrar.accept(ModModelLayers.WOOLLY_SHEEP, WoollySheepModel::createBodyLayer);
        registrar.accept(ModModelLayers.CURSE_BALL, CurseBallModel::createBodyLayer);
        registrar.accept(ModModelLayers.WOOLLY_SHEEP_FUR, WoollySheepFurModel::createFurLayer);
        registrar.accept(ModModelLayers.HEAVY_GUARD, HeavyGuardModel::createBodyLayer);
        registrar.accept(ModModelLayers.RANGED_GUARD, RangedGuardModel::createBodyLayer);
        registrar.accept(ModModelLayers.WOOLLY_SHEEP_BABY, () -> WoollySheepModel.createBodyLayer().apply(WoollySheepModel.BABY_TRANSFORMER));
        registrar.accept(ModModelLayers.WOOLLY_SHEEP_BABY_FUR, () -> WoollySheepFurModel.createFurLayer().apply(WoollySheepModel.BABY_TRANSFORMER));
        registrar.accept(ModModelLayers.GUARD, GuardModel::createBodyLayer);
        registrar.accept(ModModelLayers.CHIEF_GUARD, ChiefGuardModel::createBodyLayer);
        registrar.accept(ModModelLayers.ETHEREAL_HAMMER, EtherealHammerModel::createBodyLayer);
        registrar.accept(ModModelLayers.ETHEREAL_HANDS, EtherealHandsModel::createBodyLayer);
        registrar.accept(ModModelLayers.ETHEREAL_SWORD, EtherealSwordModel::createBodyLayer);
        registrar.accept(ModModelLayers.LEVITATING_JELLYFISH, LevitatingJellyfishModel::createBodyLayer);
        registrar.accept(ModModelLayers.SHAMAN, ShamanModel::createBodyLayer);
        registrar.accept(ModModelLayers.BIG_LEVITATING_JELLYFISH, BigLevitatingJellyfishModel::createBodyLayer);
        registrar.accept(ModModelLayers.FROZEN_REMNANTS, FrozenRemnantsModel::createBodyLayer);
        registrar.accept(ModModelLayers.FERAL_WOLF, FeralWolfModel::createBodyLayer);
        registrar.accept(ModModelLayers.BOAR, BoarModel::createBodyLayer);
        registrar.accept(ModModelLayers.MONK, MonkModel::createBodyLayer);
        registrar.accept(ModModelLayers.BOREAL_BEAR, BorealBearModel::createBodyLayer);
        registrar.accept(ModModelLayers.LODESTAR_CAGE, LodestarRenderer::createCageLayer);
        registrar.accept(ModModelLayers.SUMMONER_ELF, SummonerElfModel::createBodyLayer);
        registrar.accept(ModModelLayers.CASTER_ELF, CasterElfModel::createBodyLayer);
        registrar.accept(ModModelLayers.HEALER_ELF, HealerElfModel::createBodyLayer);
        registrar.accept(ModModelLayers.TANUKI, TanukiModel::createBodyLayer);
        registrar.accept(ModModelLayers.FIRE_SLICE, FireSliceModel::createBodyLayer);
        registrar.accept(ModModelLayers.TORCH, TorchModel::createBodyLayer);
        registrar.accept(ModModelLayers.BANDIT, BanditModel::createBodyLayer);
        registrar.accept(ModModelLayers.REVENANT, RevenantModel::createBodyLayer);
        registrar.accept(ModModelLayers.LODESTAR_EYE, LodestarRenderer::createEyeLayer);
        registrar.accept(ModModelLayers.LODESTAR_SHELL, LodestarRenderer::createShellLayer);
        registrar.accept(ModModelLayers.ICED_CREEPER, () -> CreeperModel.createBodyLayer(CubeDeformation.NONE));
        registrar.accept(ModModelLayers.ICED_ZOMBIE,
                () -> LayerDefinition.create(ZombieModel.createMesh(CubeDeformation.NONE, 0), 64, 64));
        registrar.accept(ModModelLayers.ICED_SKELETON, SkeletonModel::createBodyLayer);
        registrar.accept(ModModelLayers.SPECTER, SpecterModel::createBodyLayer);
    }
}
