package org.exodusstudio.frostbite.common.event;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.codex.Codex;
import org.exodusstudio.frostbite.client.codex.entries.CodexEntry;
import org.exodusstudio.frostbite.client.codex.entries.EntryContext;
import org.exodusstudio.frostbite.client.codex.entries.ListCodexEntry;
import org.exodusstudio.frostbite.client.codex.tabs.CodexTab;
import org.exodusstudio.frostbite.client.codex.tabs.ListCodexTab;
import org.exodusstudio.frostbite.client.codex.tabs.TreeCodexTab;
import org.exodusstudio.frostbite.common.block.HeaterBlock;
import org.exodusstudio.frostbite.common.commands.GiveFragmentCommand;
import org.exodusstudio.frostbite.common.commands.SpawnLastStandCommand;
import org.exodusstudio.frostbite.common.commands.WeatherCommand;
import org.exodusstudio.frostbite.common.contracts.Contract;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;
import org.exodusstudio.frostbite.common.entity.custom.helper.PseudoEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.FrozenRemnantsEntity;
import org.exodusstudio.frostbite.common.entity.custom.monk.MonkEntity;
import org.exodusstudio.frostbite.common.event.custom.MovePlayerEvent;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;
import org.exodusstudio.frostbite.common.registry.*;
import org.exodusstudio.frostbite.common.util.*;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;

import java.util.*;

import static org.exodusstudio.frostbite.common.util.Util.isFrostbite;

@EventBusSubscriber(modid = Frostbite.MOD_ID)
public class ModEvents {
    static RandomSource random = RandomSource.create();

    @SubscribeEvent
    public static void serverStarted(ServerStartingEvent event) {
        Frostbite.SERVER = event.getServer();
    }

    @SubscribeEvent
    public static void entityDamaged(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            ((PlayerWrapper) player).frostbite$addDamage(event.getNewDamage());
        }
    }

    @SubscribeEvent
    public static void weatherControl(LevelTickEvent.Post event) {
        Level level = event.getLevel();

        if (level instanceof ServerLevel serverLevel && serverLevel.dimensionType().hasSkyLight()) {
            WeatherInfo weatherInfo = DataHelper.getWeatherInfo(serverLevel);

            if (serverLevel.getGameRules().get(GameRules.ADVANCE_WEATHER)) {
                int i = weatherInfo.snowTime;
                int j = weatherInfo.whiteoutTime;
                int k = weatherInfo.blizzardTime;
                boolean flag1 = weatherInfo.isWhiteouting;
                boolean flag2 = weatherInfo.isBlizzarding;
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
                        j = WeatherInfo.WHITEOUT_DURATION.sample(serverLevel.getRandom());
                        weatherInfo.setWhiteouting();
                    } else {
                        j = WeatherInfo.WHITEOUT_DELAY.sample(serverLevel.getRandom());
                        weatherInfo.setSnowing();
                    }

                    if (k > 0) {
                        if (--k == 0) {
                            flag2 = !flag2;
                        }
                    } else if (flag2) {
                        k = WeatherInfo.BLIZZARD_DURATION.sample(serverLevel.getRandom());
                        weatherInfo.setBlizzarding();
                    } else {
                        k = WeatherInfo.BLIZZARD_DELAY.sample(serverLevel.getRandom());
                        weatherInfo.setSnowing();
                    }
                }

                weatherInfo.whiteoutTime = j;
                weatherInfo.blizzardTime = k;
                weatherInfo.snowTime = i;
                weatherInfo.isWhiteouting = flag1;
                weatherInfo.isBlizzarding = flag2;
            }

            weatherInfo.oWhiteoutLevel = weatherInfo.whiteoutLevel;
            if (weatherInfo.isWhiteouting) {
                weatherInfo.whiteoutLevel += 0.0025F;
            } else {
                weatherInfo.whiteoutLevel -= 0.0025F;
            }

            weatherInfo.whiteoutLevel = Mth.clamp(weatherInfo.whiteoutLevel, 0.0F, 1.0F);
            weatherInfo.oBlizzardLevel = weatherInfo.blizzardLevel;
            if (weatherInfo.isBlizzarding) {
                weatherInfo.blizzardLevel += 0.0025F;
            } else {
                weatherInfo.blizzardLevel -= 0.0025F;
            }

            weatherInfo.blizzardLevel = Mth.clamp(weatherInfo.blizzardLevel, 0.0F, 1.0F);
            DataHelper.setWeatherInfo(serverLevel, weatherInfo);
        }
    }

    @SubscribeEvent
    public static void spicyStew(FinalizeSpawnEvent event) {
        if (event.getEntity() instanceof TemperatureEntity temperatureEntity) {
            ((TE) temperatureEntity).setInnerTemp(temperatureEntity.getSpawnTemperature());
            ((TE) temperatureEntity).setOuterTemp(temperatureEntity.getSpawnTemperature());
        } else {
            ((TE) event.getEntity()).setInnerTemp(20);
            ((TE) event.getEntity()).setOuterTemp(20);
        }
    }

    @SubscribeEvent
    public static void food(LivingEntityUseItemEvent.Start event) {
        Contract c;
        // TODO: change to tags
        if (event.getEntity() instanceof Player player && (c = player.getData(AttachmentRegistry.LIVING_CONTRACT_INFO.get()).contract()) != null) {
            if (c.hasAttribute(ContractAttributes.DIABETIC) && Util.isSweet(event.getItem())) {
                event.setCanceled(true);
            } else if (c.hasAttribute(ContractAttributes.VEGETARIAN) && Util.isMeat(event.getItem())) {
                event.setCanceled(true);
            }
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
                    DataHelper.getInt(event.getItem(), "spicyness")));
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
        GiveFragmentCommand.register(event.getDispatcher());
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
    public static void spawnFrozenRemnants(LivingDamageEvent.Post event) {
        if (event.getEntity().isDeadOrDying() && event.getEntity() instanceof Player player) {
            if (player.level() instanceof ServerLevel serverLevel && FrozenRemnantsEntity.shouldSpawnFrozenRemnants(serverLevel)) {
                FrozenRemnantsEntity frozenRemnants = new FrozenRemnantsEntity(EntityRegistry.FROZEN_REMNANTS.get(), serverLevel);
                frozenRemnants.setOwner(player);
                frozenRemnants.moveOrInterpolateTo(player.position(), 0.0F, 0.0F);
                List<ItemStack> items = new ArrayList<>(player.getInventory().getNonEquipmentItems());
                items.add(((InventoryWrapper) player.getInventory()).frostbite$getEquipment().get(EquipmentSlot.FEET));
                items.add(((InventoryWrapper) player.getInventory()).frostbite$getEquipment().get(EquipmentSlot.LEGS));
                items.add(((InventoryWrapper) player.getInventory()).frostbite$getEquipment().get(EquipmentSlot.CHEST));
                items.add(((InventoryWrapper) player.getInventory()).frostbite$getEquipment().get(EquipmentSlot.HEAD));
                items.add(player.getInventory().equipment.get(EquipmentSlot.FEET));
                items.add(player.getInventory().equipment.get(EquipmentSlot.LEGS));
                items.add(player.getInventory().equipment.get(EquipmentSlot.CHEST));
                items.add(player.getInventory().equipment.get(EquipmentSlot.HEAD));
                items.add(player.getOffhandItem());
                frozenRemnants.setItems(NonNullList.copyOf(items));
                frozenRemnants.setTarget(player);

                serverLevel.addFreshEntityWithPassengers(frozenRemnants);
                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, player.position(), GameEvent.Context.of(player));
            }
        }
    }

    @SubscribeEvent
    public static void serverTick(ServerTickEvent.Pre event) {
        event.getServer().getAllLevels().forEach((level) -> {
            HashMap<UUID, Pair<String, Long>> toRemove = new HashMap<>();

            level.getData(AttachmentRegistry.CURRENT_RENDERING_ATTACKS).forEach((uuid, chargeAttackRenderables) -> {
                for (Pair<String, Long> chargeAttackRenderable : chargeAttackRenderables) {
                    LivingEntity user = (LivingEntity) level.getEntity(uuid);
                    if (user != null) {
                        Renderable renderable = Renderables.RENDERABLES.get(chargeAttackRenderable.getFirst());
                        if (renderable.shouldStopRendering(new Renderable.RenderableContext(
                                user,
                                null,
                                null,
                                (level.getGameTime() - chargeAttackRenderable.getSecond()) / 20f,
                                null
                        ))) {
                            toRemove.put(uuid, chargeAttackRenderable);
                        }
                    }
                }
            });

            for (Map.Entry<UUID, Pair<String, Long>> entry : toRemove.entrySet()) {
                LivingEntity user = (LivingEntity) level.getEntity(entry.getKey());
                if (user == null) continue;
                String renderable = entry.getValue().getFirst();
                Renderable.removeRenderable(user, renderable);
            }

            Iterable<Entity> entities = level.getEntities().getAll();
            if (entities.iterator().hasNext()) {
                entities.forEach((entity) -> {
                    if (entity instanceof LivingEntity livingEntity) {
                        TemperatureManager.updateEntityTemperature(livingEntity);
                    }
                });
            }

            if (event.getServer().getTickCount() % 20 == 0) {
                Frostbite.heaterStorages.forEach(heater -> {
                    if (heater.getDimensionName().equals(level.dimension().identifier().toString())) heater.tickBlock(level);
                });
                Frostbite.heaterStorages.removeAll(Frostbite.heatersToRemove);
                Frostbite.heatersToRemove.clear();
            }
            if (isFrostbite(level)) {
                Map<BlockPos, EntityType<?>> bossesToAdd = DataHelper.getBossesToAdd(level);
                Map<BlockPos, EntityType<?>> addedBosses = DataHelper.getAddedBosses(level);
                bossesToAdd.forEach((pos, boss) -> {
                    if (addedBosses.containsKey(pos)) return;

                    Entity e = boss.create(level, EntitySpawnReason.STRUCTURE);
                    if (e == null) {
                        Frostbite.LOGGER.error("Failed to spawn boss at {}", pos);
                        return;
                    }
                    e.setPos(pos.getX(), pos.getY(), pos.getZ());
                    level.addFreshEntityWithPassengers(e);
                    level.gameEvent(GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of(e));
                    if (e instanceof MonkEntity monkEntity) {
                        monkEntity.setArenaCenter(Vec3.atCenterOf(pos).toVector3f());
                    }
                });
                DataHelper.addAddedBosses(level, bossesToAdd);
                DataHelper.clearBossesToAdd(level);
            }

            List<PseudoEntity> pseudoEntitiesToRemove = new ArrayList<>();
            DataHelper.getAllPseudoEntities(level).forEach((key, pseudoEntities) -> {
                PseudoEntityTypes.PseudoEntityType type = PseudoEntityTypes.PSEUDO_ENTITY_TYPES.get(key);
                pseudoEntities.forEach(pseudoEntity -> {
                    PseudoEntity.PseudoEntityContext context = new PseudoEntity.PseudoEntityContext(
                            pseudoEntity,
                            level,
                            level.getEntity(pseudoEntity.owner),
                            level.getGameTime() - pseudoEntity.startTick);

                    if (type == null) {
                        Frostbite.LOGGER.error("PseudoEntityType {} not found", key);
                        return;
                    }
                    if (type.shouldRemove().apply(context)) {
                        pseudoEntitiesToRemove.add(pseudoEntity);
                        return;
                    }
                    type.tick().accept(context);
                });
            });

            DataHelper.removePseudoEntities(level, pseudoEntitiesToRemove);
        });
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

    @SubscribeEvent
    public static void targetCodex(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof LivingEntity target) {
            String name = target.typeHolder().getRegisteredName().split(":")[1];

            for (CodexTab tab : Codex.TABS) {
                if (tab instanceof TreeCodexTab treeTab) {
                    for (CodexEntry entry : treeTab.entries) {
                        if (!entry.id.equals(name)) continue;
                        CodexEntry.addEntryToPlayer(player, entry);
                    }
                }
            }

            if (Codex.TRACKED_LIST_ENTRIES.contains(name)) {
                CodexEntry.addEntryToPlayer(player, name);
            }
        }
    }

    @SubscribeEvent
    public static void inexperienced(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof LivingEntity) {
            if (LivingContractInfo.hasAppliedAttribute(player, ContractAttributes.INEXPERIENCED)) {
                event.setNewDamage(Math.min(event.getNewDamage(), player.experienceLevel));
            }
        }
    }

    @SubscribeEvent
    public static void flame(LivingDamageEvent.Post event) {
        if (event.getSource().getDirectEntity() instanceof LivingEntity entity && event.getEntity() instanceof Player player) {
            if (LivingContractInfo.hasAppliedAttribute(player, ContractAttributes.FLAME)) {
                entity.setRemainingFireTicks(entity.getRemainingFireTicks()
                        + 20 * (int) LivingContractInfo.getStat(player, ContractAttributes.FLAME));
            }
        }
    }

    @SubscribeEvent
    public static void hitEntity(LivingIncomingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity victim &&
                LivingContractInfo.hasAppliedAttribute(attacker, ContractAttributes.STOIC) &&
                !DataHelper.hasHitEntity(victim, attacker)) {
            event.setCanceled(true);
        }

        if (event.getEntity() instanceof LivingEntity victim && event.getSource().getEntity() instanceof LivingEntity attacker) {
            if (!DataHelper.hasHitEntity(attacker, victim)) {
                DataHelper.addHitEntity(attacker, victim);
            }
        }
    }

    @SubscribeEvent
    public static void filterHitEntity(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity entity && entity.tickCount % 200 == 0) {
            DataHelper.filterHitEntities(entity, entity.level());
        }
    }

    @SubscribeEvent
    public static void catlike(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity living && event.getSource().is(DamageTypes.FALL)) {
            if (LivingContractInfo.hasAppliedAttribute(living, ContractAttributes.CATLIKE)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void smelly(LivingChangeTargetEvent event) {
        if (event.getNewAboutToBeSetTarget() instanceof Player player) {
            if (LivingContractInfo.hasAppliedAttribute(player, ContractAttributes.SMELLY) && !DataHelper.hasHitEntity(player, event.getEntity())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void charged(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity entity && LivingContractInfo.hasAppliedAttribute(entity, ContractAttributes.CHARGED)) {
            if (entity.tickCount % 1200 == 0 && random.nextFloat() < LivingContractInfo.getStat(entity, ContractAttributes.CHARGED) / 100) {
                LightningBolt bolt = new LightningBolt(EntityTypes.LIGHTNING_BOLT, entity.level());
                bolt.setDamage(8);
                bolt.moveOrInterpolateTo(new Vec3(entity.getX(), entity.getY(), entity.getZ()));
                entity.level().addFreshEntity(bolt);
            }
        }
    }

    @SubscribeEvent
    public static void transport(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity entity && LivingContractInfo.hasAppliedAttribute(entity, ContractAttributes.TRANSPORT) && entity.level() instanceof ServerLevel) {
            if (entity.tickCount % 100 == 0 && random.nextFloat() < LivingContractInfo.getStat(entity, ContractAttributes.TRANSPORT) / 100) {
                Util.teleportEntityRandomly(entity.level(), 32, entity);
            }
        }
    }

    @SubscribeEvent
    public static void palpitations(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity entity && LivingContractInfo.hasAppliedAttribute(entity, ContractAttributes.PALPITATIONS)) {
            if (entity.tickCount % (LivingContractInfo.getStat(entity, ContractAttributes.PALPITATIONS) * 20) == 300) {
                entity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 100, 0));
                entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
                entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 2));
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
            }
        }
    }

    @SubscribeEvent
    public static void delayAccumulate(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity entity &&
                LivingContractInfo.hasAppliedAttribute(entity, ContractAttributes.DELAY) &&
                !event.getSource().is(DamageTypeRegistry.DELAY)) {
            List<Float> damageOverTime = DataHelper.getList(entity, "damage_over_time");
            float over = LivingContractInfo.getStat(entity, ContractAttributes.DELAY);
            if (damageOverTime.size() < over) {
                int difference = (int) (over - damageOverTime.size());
                for (int i = 0; i < difference; i++) {
                    damageOverTime.add(0.0f);
                }
            }
            damageOverTime.replaceAll(d -> d + event.getAmount() / over);
            DataHelper.setData(entity, "damage_over_time", damageOverTime);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void delayApply(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity entity &&
                LivingContractInfo.hasAppliedAttribute(entity, ContractAttributes.DELAY) &&
                entity.level() instanceof ServerLevel serverLevel) {
            List<Float> damageOverTime = DataHelper.getList(entity, "damage_over_time");
            if (damageOverTime.isEmpty() || entity.tickCount % 20 != 0) return;

            entity.hurtServer(serverLevel, serverLevel.damageSources().source(DamageTypeRegistry.DELAY, null, null), damageOverTime.getFirst());
            damageOverTime.removeFirst();
            DataHelper.setData(entity, "damage_over_time", damageOverTime);
        }
    }

    @SubscribeEvent
    public static void dull(EntityTickEvent.Post event) {
        if (event.getEntity() instanceof LivingEntity entity &&
                LivingContractInfo.hasAppliedAttributeOnWeapon(entity, ContractAttributes.DULL) &&
                entity.tickCount % 20 == 0) {
            ItemStack stack = entity.getItemInHand(InteractionHand.MAIN_HAND);
            Util.setEnchantmentsLevelOne(stack);
        }
    }

    @SubscribeEvent
    public static void midas(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity entity &&
                LivingContractInfo.hasAppliedAttributeOnWeapon(entity, ContractAttributes.MIDAS) &&
                entity.level() instanceof ServerLevel serverLevel) {
            if (random.nextFloat() >= LivingContractInfo.getStatOnWeapon(entity, ContractAttributes.MIDAS) / 100) return;
            ItemEntity itemEntity = new ItemEntity(serverLevel,
                    event.getEntity().getX(),
                    event.getEntity().getY(),
                    event.getEntity().getZ(),
                    new ItemStack(Util.randomValuable(), 1));
            serverLevel.addFreshEntity(itemEntity);
        }
    }

    @SubscribeEvent
    public static void leech(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity target &&
                LivingContractInfo.hasAppliedAttributeOnWeapon(attacker, ContractAttributes.LEECH)) {
            ((TE) attacker).increaseTemperature(LivingContractInfo.getStatOnWeapon(attacker, ContractAttributes.LEECH), false);
            ((TE) target).decreaseTemperature(LivingContractInfo.getStatOnWeapon(attacker, ContractAttributes.LEECH), false);
        }
    }

    @SubscribeEvent
    public static void spin(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity target &&
                LivingContractInfo.hasAppliedAttributeOnWeapon(attacker, ContractAttributes.SPIN)) {
            if (random.nextFloat() >= LivingContractInfo.getStatOnWeapon(attacker, ContractAttributes.SPIN) / 100) return;
            target.setYHeadRot(target.getYHeadRot() + 180);
            target.setYRot(target.getYRot() + 180);
        }
    }

    @SubscribeEvent
    public static void uppercut(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity target &&
                LivingContractInfo.hasAppliedAttributeOnWeapon(attacker, ContractAttributes.UPPERCUT)) {
            if (random.nextFloat() >= LivingContractInfo.getStatOnWeapon(attacker, ContractAttributes.UPPERCUT) / 100) return;
            target.setXRot(-90);
        }
    }

    @SubscribeEvent
    public static void tempReset(PlayerEvent.PlayerRespawnEvent event) {
        ((TE) event.getEntity()).setInnerTemp(20);
        ((TE) event.getEntity()).setOuterTemp(20);
    }

    @SubscribeEvent
    public static void contractDamage(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity) {
            float add = 0;
            if (LivingContractInfo.hasAppliedAttributeOnWeapon(attacker, ContractAttributes.SHADOW) && attacker.level().isDarkOutside())
                add += - LivingContractInfo.getStatOnWeapon(attacker, ContractAttributes.SHADOW) / 100;
            if (LivingContractInfo.hasAppliedAttribute(attacker, ContractAttributes.BERSERK) && attacker.getHealth() < (attacker.getMaxHealth() * 0.5))
                add += LivingContractInfo.getStat(attacker, ContractAttributes.BERSERK) / 100;
            if (LivingContractInfo.hasAppliedAttributeOnWeapon(attacker, ContractAttributes.CORROSION)) {
                int ticksSinceStart = Math.toIntExact(attacker.level().getGameTime() - DataHelper.getInt(attacker.getItemInHand(InteractionHand.MAIN_HAND), "corrosion_start"));
                float halfEvery = LivingContractInfo.getStatOnWeapon(attacker, ContractAttributes.CORROSION) * 60;
                add -= Util.getLog2Reduction(ticksSinceStart, halfEvery);
            }

            float comboAdd = ComboWeapon.getDamageBonus(event.getSource());
            float comboAddBonus = 0;
            if (LivingContractInfo.hasAppliedAttribute(attacker, ContractAttributes.SEQUENCE))
                comboAddBonus = comboAdd * LivingContractInfo.getStat(attacker, ContractAttributes.SEQUENCE) / 100f;
            event.setNewDamage((event.getNewDamage() + comboAdd) * (1 + add) + comboAddBonus);
        }
    }

    @SubscribeEvent
    public static void cowardly(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof LivingEntity living) {
            if (LivingContractInfo.hasAppliedAttribute(living, ContractAttributes.COWARDLY) && living.getHealth() < (living.getMaxHealth() * 0.5)) {
                event.setNewDamage(event.getNewDamage() * (1 - LivingContractInfo.getStat(living, ContractAttributes.COWARDLY) / 100));
            }
        }
    }

    @SubscribeEvent
    public static void sticky(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity target &&
                LivingContractInfo.hasAppliedAttributeOnWeapon(attacker, ContractAttributes.STICKY)) {
            Vec3 delta = target.position().subtract(attacker.position()).normalize().scale(LivingContractInfo.getStatOnWeapon(attacker, ContractAttributes.STICKY) / 100);
            if (attacker instanceof Player player) {
                NeoForge.EVENT_BUS.post(new MovePlayerEvent(delta, player.getUUID()));
            } else attacker.setDeltaMovement(delta);
        }
    }

    @SubscribeEvent
    public static void critical(CriticalHitEvent event) {
        if (LivingContractInfo.hasAppliedAttributeOnWeapon(event.getEntity(), ContractAttributes.CRITICAL)) {
            event.setDamageMultiplier(event.getDamageMultiplier() + LivingContractInfo.getStatOnWeapon(event.getEntity(), ContractAttributes.CRITICAL) / 100);
        }
    }

    @SubscribeEvent
    public static void slippery(LivingEntityUseItemEvent.Start event) {
        if (LivingContractInfo.hasAppliedAttributeOnWeapon(event.getEntity(), ContractAttributes.SLIPPERY) &&
                event.getEntity() instanceof LivingEntity attacker &&
                attacker.level() instanceof ServerLevel serverLevel) {
            if (random.nextFloat() >= LivingContractInfo.getStatOnWeapon(event.getEntity(), ContractAttributes.SLIPPERY) / 100) return;
            ItemEntity itemEntity = new ItemEntity(serverLevel,
                    attacker.getX(),
                    attacker.getEyeY(),
                    attacker.getZ(),
                    attacker.getItemInHand(InteractionHand.MAIN_HAND).copy());
            itemEntity.setDefaultPickUpDelay();
            itemEntity.setDeltaMovement(attacker.getDeltaMovement().add(attacker.getViewVector(1.0F)).normalize().scale(0.25));
            serverLevel.addFreshEntity(itemEntity);
            attacker.getItemInHand(InteractionHand.MAIN_HAND).setCount(0);
        }
    }

    @SubscribeEvent
    public static void slippery(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity &&
                LivingContractInfo.hasAppliedAttributeOnWeapon(attacker, ContractAttributes.SLIPPERY) &&
                attacker.level() instanceof ServerLevel serverLevel) {
            if (random.nextFloat() >= LivingContractInfo.getStatOnWeapon(attacker, ContractAttributes.SLIPPERY) / 100) return;
            ItemEntity itemEntity = new ItemEntity(serverLevel,
                    attacker.getX(),
                    attacker.getEyeY(),
                    attacker.getZ(),
                    attacker.getItemInHand(InteractionHand.MAIN_HAND).copy());
            itemEntity.setDefaultPickUpDelay();
            itemEntity.setDeltaMovement(attacker.getDeltaMovement().add(attacker.getViewVector(1.0F)).normalize().scale(0.25));
            serverLevel.addFreshEntity(itemEntity);
            attacker.getItemInHand(InteractionHand.MAIN_HAND).setCount(0);
        }
    }

    @SubscribeEvent
    public static void listCodex(PlayerTickEvent.Post event) {
        if (event.getEntity().tickCount % 20 != 0) return;

        for (CodexTab tab : Codex.TABS) {
            if (tab instanceof ListCodexTab listTab) {
                for (CodexEntry entry : listTab.entries) {
                    if (entry instanceof ListCodexEntry listEntry &&
                        !CodexEntry.playerHasEntry(event.getEntity(), entry) &&
                        listEntry.function != null &&
                        listEntry.function.apply(new EntryContext(event.getEntity().level(), event.getEntity()))) {

                        CodexEntry.addEntryToPlayer(event.getEntity(), entry);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void listCodex(AdvancementEvent.AdvancementProgressEvent event) {
        String[] s = event.getCriterionName().split(":");
        if (s.length < 2) return;
        String entry = s[1];
        if (Codex.TRACKED_LIST_ENTRIES.contains(entry)) {
            CodexEntry.addEntryToPlayer(event.getEntity(), entry);
        }
    }
}