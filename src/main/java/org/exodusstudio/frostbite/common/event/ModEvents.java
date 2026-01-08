package org.exodusstudio.frostbite.common.event;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.entity.living.LivingUseTotemEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
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
import org.exodusstudio.frostbite.common.entity.custom.monk.MonkEntity;
import org.exodusstudio.frostbite.common.item.weapons.elf.ModeWeapon;
import org.exodusstudio.frostbite.common.network.StaffPayload;
import org.exodusstudio.frostbite.common.registry.*;
import org.exodusstudio.frostbite.common.structures.FTOPortal;
import org.exodusstudio.frostbite.common.structures.OTFPortal;
import org.exodusstudio.frostbite.common.util.BreathEntityLike;
import org.exodusstudio.frostbite.common.util.HeaterStorage;
import org.exodusstudio.frostbite.common.util.PlayerWrapper;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

import static org.exodusstudio.frostbite.common.util.Util.isFrostbite;

@EventBusSubscriber(modid = Frostbite.MOD_ID)
public class ModEvents {
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

        if (level instanceof ServerLevel serverLevel && serverLevel.dimensionType().hasSkyLight()) {
            if (serverLevel.getGameRules().get(GameRules.ADVANCE_WEATHER)) {
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
                        Frostbite.weatherInfo.setWhiteouting();
                    } else {
                        j = WeatherInfo.WHITEOUT_DELAY.sample(serverLevel.random);
                        Frostbite.weatherInfo.setSnowing();
                    }

                    if (k > 0) {
                        if (--k == 0) {
                            flag2 = !flag2;
                        }
                    } else if (flag2) {
                        k = WeatherInfo.BLIZZARD_DURATION.sample(serverLevel.random);
                        Frostbite.weatherInfo.setBlizzarding();
                    } else {
                        k = WeatherInfo.BLIZZARD_DELAY.sample(serverLevel.random);
                        Frostbite.weatherInfo.setSnowing();
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
    public static void cancelClearingEffects(MobEffectEvent.Remove event) {
        if (event.getEffectInstance() != null && EffectRegistry.isCurse(event.getEffectInstance().getEffect()) && event.getEntity() instanceof Player) {
            event.setCanceled(true);
        }
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
    public static void snow(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = event.getEntity().level();
        RandomSource random = player.getRandom();
        if (level.isClientSide() && isFrostbite(level)) {
            float r = Mth.lerp(Frostbite.weatherInfo.whiteoutLevel,
                      Mth.lerp(Frostbite.weatherInfo.blizzardLevel, 30, 20), 15);
            float offset = Mth.lerp(Frostbite.weatherInfo.whiteoutLevel,
                           Mth.lerp(Frostbite.weatherInfo.blizzardLevel, 10, 5), 2);
            float s = Mth.lerp(Frostbite.weatherInfo.whiteoutLevel,
                      Mth.lerp(Frostbite.weatherInfo.blizzardLevel, 0.025f, 0.1f), 0.1f);
            float d = Mth.lerp(Frostbite.weatherInfo.whiteoutLevel,
                      Mth.lerp(Frostbite.weatherInfo.blizzardLevel, 0.05f, 0.1f), 0.1f);
            int count = (int) Mth.lerp(Frostbite.weatherInfo.whiteoutLevel,
                              Mth.lerp(Frostbite.weatherInfo.blizzardLevel, 125, 100), 75);
            for (int i = 0; i < count; i++) {
                double d0 = player.getX() + player.getLookAngle().normalize().x * offset + (0.5D - random.nextDouble()) * r;
                double d1 = player.getY() + player.getLookAngle().normalize().y * offset + (0.5D - random.nextDouble()) * r / 2f + r / 4f;
                double d2 = player.getZ() + player.getLookAngle().normalize().z * offset + (0.5D - random.nextDouble()) * r;

                level.addAlwaysVisibleParticle(
                        random.nextFloat() < 0.01 ? ParticleTypes.END_ROD : ParticleRegistry.AMBIENT_SNOW_PARTICLE.get(),
                        d0, d1, d2,
                        (random.nextDouble()) * d + s,
                        (-random.nextDouble()) * d - s,
                        (random.nextDouble()) * d + s);
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
//                    if (livingEntity instanceof GuardEntity guard && Minecraft.getInstance().level != null) {
//                        Util.spawnParticlesFromAABB(Minecraft.getInstance().level, guard.getAttackAABB(), 10);
//                    }
                }
            });
            if (event.getServer().getTickCount() % 20 == 0) {
                Frostbite.heaterStorages.forEach(heater -> {
                    if (heater.getDimensionName().equals(level.dimension().identifier().toString())) heater.tickBlock(level);
                });
                Frostbite.heaterStorages.removeAll(Frostbite.heatersToRemove);
                Frostbite.heatersToRemove.clear();
            }
            if (isFrostbite(level)) {
                Frostbite.bossesToAdd.forEach((pos, boss) -> {
                    if (Frostbite.addedBosses.containsKey(pos)) return;

                    Entity e = boss.create(level, EntitySpawnReason.STRUCTURE);
                    e.setPos(pos.getX(), pos.getY(), pos.getZ());
                    level.addFreshEntityWithPassengers(e);
                    level.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(e));
                    if (e instanceof MonkEntity monkEntity) {
                        monkEntity.setArenaCenter(pos.getCenter().toVector3f());
                    }
                });
                Frostbite.addedBosses.putAll(Frostbite.bossesToAdd);
                Frostbite.bossesToAdd.clear();
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
                                heater.getDimensionName().equals(serverLevel.dimension().identifier().toString()))) {
            Frostbite.heaterStorages.add(new HeaterStorage(event.getPos(), block, serverLevel.dimension().identifier().toString()));
            event.cancelWithResult(InteractionResult.FAIL);

        }
    }
}