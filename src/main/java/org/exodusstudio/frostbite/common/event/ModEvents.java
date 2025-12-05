package org.exodusstudio.frostbite.common.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingUseTotemEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.HeaterBlock;
import org.exodusstudio.frostbite.common.commands.SpawnLastStandCommand;
import org.exodusstudio.frostbite.common.commands.WeatherCommand;
import org.exodusstudio.frostbite.common.entity.custom.misc.FrozenRemnantsEntity;
import org.exodusstudio.frostbite.common.item.weapons.elf.ModeWeapon;
import org.exodusstudio.frostbite.common.network.StaffPayload;
import org.exodusstudio.frostbite.common.registry.DataComponentTypeRegistry;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.structures.FTOPortal;
import org.exodusstudio.frostbite.common.structures.OTFPortal;
import org.exodusstudio.frostbite.common.util.BreathEntityLike;
import org.exodusstudio.frostbite.common.util.HeaterStorage;
import org.exodusstudio.frostbite.common.util.PlayerWrapper;
import org.exodusstudio.frostbite.common.weather.FrostbiteWeatherEffectRenderer;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

import static org.exodusstudio.frostbite.common.util.Util.isFrostbite;

@EventBusSubscriber(modid = Frostbite.MOD_ID)
public class ModEvents {
    public static String currentBiome = "";
    public static float time = 0;
    public static FrostbiteWeatherEffectRenderer weatherEffectRenderer = new FrostbiteWeatherEffectRenderer();

    @SubscribeEvent
    public static void reset(ServerStoppedEvent event) {
        OTFPortal.canSpawn = true;
        FTOPortal.canSpawn = true;
        Frostbite.temperatureStorage.clear();
    }

    @SubscribeEvent
    public static void entityDamaged(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            ((PlayerWrapper) player).frostbite$addDamage(event.getNewDamage());
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
        Frostbite.weatherInfo.normalFarFog = Minecraft.getInstance().options.getEffectiveRenderDistance() * 16;

        if (level instanceof ServerLevel serverLevel && serverLevel.dimensionType().hasSkyLight()) {
            long time = level.getGameTime();
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
                        Frostbite.weatherInfo.setWhiteouting(time);
                    } else {
                        j = WeatherInfo.WHITEOUT_DELAY.sample(serverLevel.random);
                        Frostbite.weatherInfo.setSnowing(time);
                    }

                    if (k > 0) {
                        if (--k == 0) {
                            flag2 = !flag2;
                        }
                    } else if (flag2) {
                        k = WeatherInfo.BLIZZARD_DURATION.sample(serverLevel.random);
                        Frostbite.weatherInfo.setBlizzarding(time);
                    } else {
                        k = WeatherInfo.BLIZZARD_DELAY.sample(serverLevel.random);
                        Frostbite.weatherInfo.setSnowing(time);
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
        assert Minecraft.getInstance().level != null;
        if (isFrostbite(Minecraft.getInstance().level)) {
            weatherEffectRenderer.render(Minecraft.getInstance().level, renderer.renderBuffers.bufferSource(),
                    renderer.getTicks(), event.getPartialTick().getGameTimeDeltaPartialTick(false),
                    event.getCamera().getPosition());
        }
    }

    @SubscribeEvent
    public static void fogDist(ViewportEvent.RenderFog event) {
        Player player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;

        if (player == null || !player.isAlive() || level == null ||
                !isFrostbite(level)) return;

        if (event.getType() == FogType.ATMOSPHERIC) {
            float t = Frostbite.weatherInfo.getLerp();

            float nearPlane = Mth.lerp(t, Frostbite.weatherInfo.oNearFog, Frostbite.weatherInfo.nearFog);
            float farPlane = Mth.lerp(t, Frostbite.weatherInfo.oFarFog, Frostbite.weatherInfo.farFog);

            event.setNearPlaneDistance(nearPlane);
            event.setFarPlaneDistance(farPlane);
        }
    }

    @SubscribeEvent
    public static void spicyStew(LivingEntityUseItemEvent.Tick event) {
        if (
                (event.getItem().is(ItemRegistry.SPICY_VEGETABLE_STEW) ||
                event.getItem().is(ItemRegistry.SPICY_FISH_SOUP) ||
                event.getItem().is(ItemRegistry.SPICY_MEAT_STEW)) &&
                event.getDuration() == 1) {
            event.getEntity().addEffect(new MobEffectInstance(EffectRegistry.SATIATED, 4800,
                    event.getItem().get(DataComponentTypeRegistry.CHARGE.get()).charge()));
        }
    }

    @SubscribeEvent
    public static void fogColour(ViewportEvent.ComputeFogColor event) {
        Player player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;

        if (player == null || !player.isAlive() || level == null ||
                !isFrostbite(level)) return;

        computeWeatherInfo(level, player);

        float t = Frostbite.weatherInfo.getLerp();

        float red = Mth.lerp(t, Frostbite.weatherInfo.oRed, Frostbite.weatherInfo.red);
        float green = Mth.lerp(t, Frostbite.weatherInfo.oGreen, Frostbite.weatherInfo.green);
        float blue = Mth.lerp(t, Frostbite.weatherInfo.oBlue, Frostbite.weatherInfo.blue);

        event.setRed(red);
        event.setGreen(green);
        event.setBlue(blue);
    }

    @SubscribeEvent
    public static void totem(LivingUseTotemEvent event) {
        if (event.getTotem().is(ItemRegistry.LAST_STAND) && event.getEntity() instanceof Player player &&
                player.level() instanceof ServerLevel serverLevel) {
            ((PlayerWrapper) player).frostbite$startAccumulatingDamage(serverLevel);
        }
    }

    @SubscribeEvent
    public static void commands(RegisterCommandsEvent event) {
        SpawnLastStandCommand.register(event.getDispatcher(), event.getBuildContext());
        WeatherCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void damageLining(PlayerTickEvent.Post event) {
        if (event.getEntity().level() instanceof ServerLevel serverLevel && isFrostbite(serverLevel) && serverLevel.getGameTime() % 400 == 0) {
            for (int i = 43; i < 48; i++) {
                event.getEntity().getInventory().getItem(i).hurtAndBreak(1, serverLevel, (ServerPlayer) (event.getEntity()), (item) -> {});
            }
        }
    }

    @SubscribeEvent
    public static void livingDamagedEvent(LivingDamageEvent.Post event) {
        if (event.getEntity().isDeadOrDying() && event.getEntity() instanceof Player player) {
            if (player.level() instanceof ServerLevel serverLevel && FrozenRemnantsEntity.shouldSpawnFrozenRemnants(serverLevel)) {
                FrozenRemnantsEntity frozenRemnants = new FrozenRemnantsEntity(EntityRegistry.FROZEN_REMNANTS.get(), serverLevel);
                frozenRemnants.setOwner(player);
                frozenRemnants.moveOrInterpolateTo(player.position(), 0.0F, 0.0F);
                frozenRemnants.setItems(player.getInventory().getNonEquipmentItems());
                frozenRemnants.setTarget(player);

                serverLevel.addFreshEntityWithPassengers(frozenRemnants);
                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, player.position(), GameEvent.Context.of(player));
            }
        }
    }

    @SubscribeEvent
    public static void staffControl(InputEvent.MouseButton.Pre event) {
        if (event.getButton() == 1) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                if (itemInHand.getItem() instanceof ModeWeapon staff && !player.getCooldowns().isOnCooldown(itemInHand)) {
                    staff.attack(player.level(), player);
                    player.getCooldowns().addCooldown(itemInHand, 20);
                    event.setCanceled(true);
                    ClientPacketDistributor.sendToServer(new StaffPayload(new StaffPayload.StaffInfo(staff.mode, player.getUUID())));
                }
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
                Frostbite.heaterStorages.forEach(heater -> {
                    if (heater.getDimensionName().equals(level.dimension().location().toString())) heater.tickBlock(level);
                });
                Frostbite.heaterStorages.removeAll(Frostbite.heatersToRemove);
                Frostbite.heatersToRemove.clear();
            }
        });
        Frostbite.temperatureStorage.updateEntityTemperatures(entities);
        Frostbite.breathEntityLikes.forEach(BreathEntityLike::tick);
        Frostbite.breathEntityLikes.removeAll(Frostbite.breathEntityLikesToRemove);
        Frostbite.breathEntityLikesToRemove.clear();
    }

    @SubscribeEvent
    public static void heater(UseItemOnBlockEvent event) {
        assert event.getPlayer() != null;
        BlockState state = event.getLevel().getBlockState(event.getPos());
        ItemStack stack = event.getPlayer().getItemInHand(event.getHand());

        if (stack.is(Items.FLINT_AND_STEEL) &&
                event.getLevel() instanceof ServerLevel serverLevel &&
                state.getBlock() instanceof HeaterBlock block &&
                Frostbite.heaterStorages.stream().noneMatch(heater ->
                        heater.getPos().equals(event.getPos()) &&
                                heater.getDimensionName().equals(serverLevel.dimension().location().toString()))) {
            Frostbite.heaterStorages.add(new HeaterStorage(event.getPos(), block, serverLevel.dimension().location().toString()));
            event.cancelWithResult(InteractionResult.FAIL);

        }
    }

    public static void computeWeatherInfo(ClientLevel level, Player player) {
        String name = level.getBiome(player.blockPosition()).toString();

        if (!currentBiome.equals(name) || level.getGameTime() < 20) {
            assert Minecraft.getInstance().level != null;
            if (Minecraft.getInstance().level.getGameTime() - time > 100) {
                if (name.contains("shrouded_forest") && !Frostbite.weatherInfo.isBlizzarding && !Frostbite.weatherInfo.isWhiteouting) {
                    Frostbite.weatherInfo.oNearFog = Frostbite.weatherInfo.nearFog;
                    Frostbite.weatherInfo.oFarFog = Frostbite.weatherInfo.farFog;
                    Frostbite.weatherInfo.nearFog = -50f;
                    Frostbite.weatherInfo.farFog = 100f;
                    Frostbite.weatherInfo.oRed = Frostbite.weatherInfo.red;
                    Frostbite.weatherInfo.oGreen = Frostbite.weatherInfo.green;
                    Frostbite.weatherInfo.oBlue = Frostbite.weatherInfo.blue;
                    Frostbite.weatherInfo.red = 73 / 255f;
                    Frostbite.weatherInfo.green = 106 / 255f;
                    Frostbite.weatherInfo.blue = 184 / 255f;
                } else {
                    Frostbite.weatherInfo.oNearFog = Frostbite.weatherInfo.nearFog;
                    Frostbite.weatherInfo.oFarFog = Frostbite.weatherInfo.farFog;
                    Frostbite.weatherInfo.oRed = Frostbite.weatherInfo.red;
                    Frostbite.weatherInfo.oGreen = Frostbite.weatherInfo.green;
                    Frostbite.weatherInfo.oBlue = Frostbite.weatherInfo.blue;

                    if (Frostbite.weatherInfo.isWhiteouting) {
                        Frostbite.weatherInfo.red = WeatherInfo.WHITEOUT_COLOUR;
                        Frostbite.weatherInfo.green = WeatherInfo.WHITEOUT_COLOUR;
                        Frostbite.weatherInfo.blue = WeatherInfo.WHITEOUT_COLOUR;
                        Frostbite.weatherInfo.nearFog = WeatherInfo.WHITEOUT_NEAR_FOG;
                        Frostbite.weatherInfo.farFog = WeatherInfo.WHITEOUT_FAR_FOG;
                    } else if (Frostbite.weatherInfo.isBlizzarding) {
                        Frostbite.weatherInfo.red = WeatherInfo.BLIZZARD_COLOUR;
                        Frostbite.weatherInfo.green = WeatherInfo.BLIZZARD_COLOUR;
                        Frostbite.weatherInfo.blue = WeatherInfo.BLIZZARD_COLOUR;
                        Frostbite.weatherInfo.nearFog = WeatherInfo.BLIZZARD_NEAR_FOG;
                        Frostbite.weatherInfo.farFog = WeatherInfo.BLIZZARD_FAR_FOG;
                    } else {
                        Frostbite.weatherInfo.red = WeatherInfo.normalRed;
                        Frostbite.weatherInfo.green = WeatherInfo.normalGreen;
                        Frostbite.weatherInfo.blue = WeatherInfo.normalBlue;
                        Frostbite.weatherInfo.nearFog = WeatherInfo.normalNearFog;
                        Frostbite.weatherInfo.farFog = Frostbite.weatherInfo.normalFarFog;
                    }
                }
                currentBiome = name;
                Frostbite.weatherInfo.timeSinceLastUpdate = Minecraft.getInstance().level.getGameTime();
                time = Minecraft.getInstance().level.getGameTime();
            }
        }
    }
}