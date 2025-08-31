package org.exodusstudio.frostbite.common.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FogType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingUseTotemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.HeaterBlock;
import org.exodusstudio.frostbite.common.block.HeaterStorage;
import org.exodusstudio.frostbite.common.commands.SpawnLastStandCommand;
import org.exodusstudio.frostbite.common.commands.WeatherCommand;
import org.exodusstudio.frostbite.common.entity.custom.FrozenRemnantsEntity;
import org.exodusstudio.frostbite.common.item.last_stand.LastStand;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.structures.FTOPortal;
import org.exodusstudio.frostbite.common.structures.OTFPortal;
import org.exodusstudio.frostbite.common.weather.FrostbiteWeatherEffectRenderer;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Frostbite.MOD_ID)
public class ModEvents {
    public static float blendLerp = 0;
    public static FrostbiteWeatherEffectRenderer weatherEffectRenderer = new FrostbiteWeatherEffectRenderer();

    @SubscribeEvent
    public static void reset(ServerStoppingEvent event) {
        //OTFPortal.count = 0;
        OTFPortal.canSpawn = true;
        //FTOPortal.count = 0;
        FTOPortal.canSpawn = true;
        blendLerp = 0;
        Frostbite.savedTemperatures.clear();
    }

    @SubscribeEvent
    public static void entityDamaged(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            ((LastStand) player).frostbite$addDamage(event.getNewDamage());
        }
    }

    @SubscribeEvent
    public static void fov(ComputeFovModifierEvent event) {
        if (event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND).is(ItemRegistry.SNIPER) && event.getPlayer().isShiftKeyDown()) {
            event.setNewFovModifier(0.05f);
        }
    }

    @SubscribeEvent
    public static void weatherControl(LevelTickEvent.Post event) {
        Level level = event.getLevel();

        if (level instanceof ServerLevel serverLevel && serverLevel.dimensionType().hasSkyLight()) {
            if (serverLevel.getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
                int i = Frostbite.weatherInfo.snowTime;
                int j = Frostbite.weatherInfo.whiteoutTime;
                int k = Frostbite.weatherInfo.blizzardTime;
                boolean flag1 = Frostbite.weatherInfo.isWhiteouting;
                boolean flag2 = Frostbite.weatherInfo.isBlizzarding;
                if (i > 0) {
                    i--;
                    j = flag1 ? 0 : 1;
                    k = flag2 ? 0 : 1;
                    flag1 = false;
                    flag2 = false;
                } else {
                    if (j > 0) {
                        if (--j == 0) {
                            flag1 = !flag1;
                        }
                    } else if (flag1) {
                        j = WeatherInfo.WHITEOUT_DURATION.sample(serverLevel.random);
                    } else {
                        j = WeatherInfo.WHITEOUT_DELAY.sample(serverLevel.random);
                    }

                    if (k > 0) {
                        if (--k == 0) {
                            flag2 = !flag2;
                        }
                    } else if (flag2) {
                        k = WeatherInfo.BLIZZARD_DURATION.sample(serverLevel.random);
                    } else {
                        k = WeatherInfo.BLIZZARD_DELAY.sample(serverLevel.random);
                    }
                }

                Frostbite.weatherInfo.whiteoutTime = j;
                Frostbite.weatherInfo.blizzardTime = k;
                Frostbite.weatherInfo.snowTime = i;
                Frostbite.weatherInfo.isWhiteouting = flag1;
                Frostbite.weatherInfo.isBlizzarding = flag2;
            }

            Frostbite.weatherInfo.oWhiteoutLevel = Frostbite.weatherInfo.whiteoutLevel;
            if (Frostbite.weatherInfo.isWhiteouting) {
                Frostbite.weatherInfo.whiteoutLevel += 0.0025F;
            } else {
                Frostbite.weatherInfo.whiteoutLevel -= 0.0025F;
            }

            Frostbite.weatherInfo.whiteoutLevel = Mth.clamp(Frostbite.weatherInfo.whiteoutLevel, 0.0F, 1.0F);
            Frostbite.weatherInfo.oBlizzardLevel = Frostbite.weatherInfo.blizzardLevel;
            if (Frostbite.weatherInfo.isBlizzarding) {
                Frostbite.weatherInfo.blizzardLevel += 0.0025F;
            } else {
                Frostbite.weatherInfo.blizzardLevel -= 0.0025F;
            }

            Frostbite.weatherInfo.blizzardLevel = Mth.clamp(Frostbite.weatherInfo.blizzardLevel, 0.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void weatherRender(RenderLevelStageEvent.AfterWeather event) {
        LevelRenderer renderer = event.getLevelRenderer();
        if (Minecraft.getInstance().level.dimension().toString().equals("ResourceKey[minecraft:dimension / frostbite:frostbite]")) {
            weatherEffectRenderer.render(Minecraft.getInstance().level, renderer.renderBuffers.bufferSource(),
                    renderer.getTicks(), event.getPartialTick().getGameTimeDeltaPartialTick(false),
                    event.getCamera().getPosition());
        }
    }

    @SubscribeEvent
    public static void fogDist(ViewportEvent.RenderFog event) {
        Player player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;
        if (player == null || !player.isAlive() || level == null) return;

        if (event.getType() == FogType.ATMOSPHERIC) {
            float nearShrouded = Mth.lerp(blendLerp, event.getNearPlaneDistance(), -50f);
            float farShrouded = Mth.lerp(blendLerp, event.getFarPlaneDistance(), 100f);

            float near;
            float far;

            if (Frostbite.weatherInfo.isWhiteouting) {
                near = Mth.lerp(Frostbite.weatherInfo.getWhiteoutLevel((float) event.getPartialTick()), nearShrouded, -50f);
                far = Mth.lerp(Frostbite.weatherInfo.getWhiteoutLevel((float) event.getPartialTick()), farShrouded, 50f);
            } else if (Frostbite.weatherInfo.isBlizzarding) {
                near = Mth.lerp(Frostbite.weatherInfo.getBlizzardLevel((float) event.getPartialTick()), nearShrouded, 0f);
                far = Mth.lerp(Frostbite.weatherInfo.getBlizzardLevel((float) event.getPartialTick()), farShrouded, 100f);
            } else {
                near = Mth.lerp(Frostbite.weatherInfo.getBlizzardLevel((float) event.getPartialTick()), 0, nearShrouded);
                far = Mth.lerp(Frostbite.weatherInfo.getBlizzardLevel((float) event.getPartialTick()), 100, farShrouded);
            }

//            float near = event.getNearPlaneDistance();
//            float far = event.getFarPlaneDistance();
//            float oNear = event.getFarPlaneDistance();
//            float oFar = event.getFarPlaneDistance();
//
//            if (Frostbite.weatherInfo.isWhiteouting) {
//                oNear = near;
//                oFar = far;
//                near = -50f;
//                far = 50f;
//            } else if (Frostbite.weatherInfo.isBlizzarding) {
//                oNear = near;
//                oFar = far;
//                near = 0f;
//                far = 100f;
//            } else if (level.getBiome(player.blockPosition()).toString().contains("shrouded_forest")) {
//                oNear = near;
//                oFar = far;
//                near = -50f;
//                far = 100f;
//            }
//
//            float t = Mth.clamp(, 0, 1);
//            float nearPlane = Mth.lerp(t, oNear, near);
//            float farPlane = Mth.lerp(t, oFar, far);

            event.setNearPlaneDistance(near);
            event.setFarPlaneDistance(far);
        }
    }

    @SubscribeEvent
    public static void fogColour(ViewportEvent.ComputeFogColor event) {
        Player player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;
        if (player == null || !player.isAlive() || level == null) return;

        computeBlendLerp(level, player, event.getPartialTick());
        float red1 = Mth.lerp(blendLerp, event.getRed(), 73 / 255f);
        float green1 = Mth.lerp(blendLerp, event.getGreen(), 106 / 255f);
        float blue1 = Mth.lerp(blendLerp, event.getBlue(), 184 / 255f);

        float red = red1;
        float green = green1;
        float blue = blue1;

        float blizzardLevel = Frostbite.weatherInfo.getBlizzardLevel((float) event.getPartialTick());
        float whiteoutLevel = Frostbite.weatherInfo.getWhiteoutLevel((float) event.getPartialTick());

        if (Frostbite.weatherInfo.isWhiteouting) {
            red = Mth.lerp(blizzardLevel, red1, 200 / 255f);
            green = Mth.lerp(blizzardLevel, green1, 200 / 255f);
            blue = Mth.lerp(blizzardLevel, blue1, 200 / 255f);
        } else if (Frostbite.weatherInfo.isBlizzarding) {
            red = Mth.lerp(whiteoutLevel, red1, 150 / 255f);
            green = Mth.lerp(whiteoutLevel, green1, 150 / 255f);
            blue = Mth.lerp(whiteoutLevel, blue1, 150 / 255f);
        }


        event.setRed(red);
        event.setGreen(green);
        event.setBlue(blue);
    }

    @SubscribeEvent
    public static void totem(LivingUseTotemEvent event) {
        if (event.getTotem().is(ItemRegistry.LAST_STAND) && event.getEntity() instanceof Player player &&
                player.level() instanceof ServerLevel serverLevel) {
            ((LastStand) player).frostbite$startAccumulatingDamage(serverLevel);
        }
    }

    @SubscribeEvent
    public static void commands(RegisterCommandsEvent event) {
        SpawnLastStandCommand.register(event.getDispatcher(), event.getBuildContext());
        WeatherCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void livingDamagedEvent(LivingDamageEvent.Post event) {
        if (event.getEntity().isDeadOrDying() && event.getEntity() instanceof Player player) {
            if (player.level() instanceof ServerLevel serverLevel && FrozenRemnantsEntity.shouldSpawnFrozenRemnants(serverLevel)) {
                FrozenRemnantsEntity frozenRemnants = new FrozenRemnantsEntity(EntityRegistry.FROZEN_REMNANTS.get(), serverLevel);
                frozenRemnants.setOwner(player);
                frozenRemnants.moveOrInterpolateTo(player.position(), 0.0F, 0.0F);
                frozenRemnants.setItems(player.getInventory().getNonEquipmentItems());
                frozenRemnants.finalizeSpawn(serverLevel,
                        player.level().getCurrentDifficultyAt(BlockPos.containing(player.position())),
                        EntitySpawnReason.EVENT, null);
                frozenRemnants.setTarget(player);

                serverLevel.addFreshEntityWithPassengers(frozenRemnants);
                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, player.position(), GameEvent.Context.of(player));
            }
        }
    }

    @SubscribeEvent
    public static void serverTick(ServerTickEvent.Pre event) {
        List<LivingEntity> entities = new ArrayList<>();
        event.getServer().getAllLevels().forEach((level) -> {
            level.getEntities().getAll().forEach((entity) -> {
                if (entity instanceof LivingEntity livingEntity) {
                    entities.add(livingEntity);
                }
            });
            if (event.getServer().getTickCount() % 20 == 0) {
                Frostbite.savedHeaters.forEach(heater -> {
                    if (heater.getDimensionName().equals(level.dimension().location().toString())) heater.tickBlock(level);
                });
                Frostbite.savedHeaters.removeAll(Frostbite.heatersToRemove);
                Frostbite.heatersToRemove.clear();
            }
        });
        Frostbite.savedTemperatures.updateEntityTemperatures(entities);
    }

    @SubscribeEvent
    public static void heater(UseItemOnBlockEvent event) {
        assert event.getPlayer() != null;
        BlockState state = event.getLevel().getBlockState(event.getPos());
        ItemStack stack = event.getPlayer().getItemInHand(event.getHand());

        if (stack.is(Items.FLINT_AND_STEEL) &&
                event.getLevel() instanceof ServerLevel serverLevel &&
                state.getBlock() instanceof HeaterBlock block &&
                Frostbite.savedHeaters.stream().noneMatch(heater ->
                        heater.getPos().equals(event.getPos()) &&
                        heater.getDimensionName().equals(serverLevel.dimension().location().toString()))) {
            Frostbite.savedHeaters.add(new HeaterStorage(event.getPos(), block, serverLevel.dimension().location().toString()));
            event.cancelWithResult(InteractionResult.FAIL);

        }
    }

    @SubscribeEvent
    public static void containerOpen(PlayerContainerEvent.Open event) {
        Frostbite.LOGGER.debug(event.getContainer().toString());
    }

    @SubscribeEvent
    public static void playerTick(PlayerTickEvent.Pre event) {
        Player player = event.getEntity();

        if (Frostbite.savedLinings.getLiningsForPlayerOrSetEmpty(player.getStringUUID()) == null) {
            Frostbite.savedLinings.setLiningsForPlayer(player.getStringUUID(),
                    ItemStack.EMPTY,
                    ItemStack.EMPTY,
                    ItemStack.EMPTY,
                    ItemStack.EMPTY);
        }
    }

    public static void computeBlendLerp(ClientLevel level, Player player, double partialTicks) {
        String name = level.getBiome(player.blockPosition()).toString();

        if (name.contains("shrouded_forest")) {
            blendLerp = (float) Mth.clamp(blendLerp + partialTicks / 200, 0, 1);
        } else {
            blendLerp = (float) Mth.clamp(blendLerp - partialTicks / 200, 0, 1);
        }
    }
}
