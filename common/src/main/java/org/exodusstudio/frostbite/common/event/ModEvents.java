package org.exodusstudio.frostbite.common.event;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Pair;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.client.codex.Codex;
import org.exodusstudio.frostbite.client.codex.entries.CodexEntry;
import org.exodusstudio.frostbite.client.codex.entries.EntryContext;
import org.exodusstudio.frostbite.client.codex.entries.ListCodexEntry;
import org.exodusstudio.frostbite.client.codex.tabs.CodexTab;
import org.exodusstudio.frostbite.client.codex.tabs.ListCodexTab;
import org.exodusstudio.frostbite.client.codex.tabs.TreeCodexTab;
import org.exodusstudio.frostbite.common.block.*;
import org.exodusstudio.frostbite.common.combat.ParryManager;
import org.exodusstudio.frostbite.common.block.block_entities.RuneBlockEntity;
import org.exodusstudio.frostbite.common.commands.GiveFragmentCommand;
import org.exodusstudio.frostbite.common.commands.SpawnLastStandCommand;
import org.exodusstudio.frostbite.common.commands.WeatherCommand;
import org.exodusstudio.frostbite.common.contracts.Contract;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.contracts.LivingContractInfo;
import org.exodusstudio.frostbite.common.entity.custom.helper.PseudoEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.FrozenRemnantsEntity;
import org.exodusstudio.frostbite.common.entity.custom.monk.MonkEntity;
import org.exodusstudio.frostbite.common.event.custom.*;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;
import org.exodusstudio.frostbite.common.item.weapons.HuntersCatalyst;
import org.exodusstudio.frostbite.common.mixinterfaces.InventoryWrapper;
import org.exodusstudio.frostbite.common.mixinterfaces.PlayerWrapper;
import org.exodusstudio.frostbite.common.mixinterfaces.TE;
import org.exodusstudio.frostbite.common.mixinterfaces.TemperatureEntity;
import org.exodusstudio.frostbite.common.registry.*;
import org.exodusstudio.frostbite.common.util.Renderable;
import org.exodusstudio.frostbite.common.util.TemperatureManager;
import org.exodusstudio.frostbite.common.util.Util;
import org.exodusstudio.frostbite.common.util.helpers.DataHelper;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;
import org.exodusstudio.frostbite.platform.EntityPlatform;

import java.util.*;

import static net.minecraft.world.level.block.SnowyBlock.SNOWY;
import static org.exodusstudio.frostbite.common.util.Util.isFrostbite;

public class ModEvents {
    static RandomSource random = RandomSource.create();

    private static final String LOCK_IN_END_TICK = "frostbite_lock_in_end_tick";
    private static final String LOCK_IN_ANCHOR = "frostbite_lock_in_anchor";
    private static final String WEBBING_STACKS = "frostbite_webbing_stacks";
    private static final String WEBBING_TICKS = "frostbite_webbing_ticks";
    private static final String WEBBING_BASE_AMPLIFIER = "frostbite_webbing_base_amplifier";
    private static final float REPEL_RADIUS = 4.0F;

    public static void init() {
        LifecycleEvent.SERVER_BEFORE_START.register(ModEvents::serverStarted);
        TickEvent.SERVER_PRE.register(ModEvents::serverTick);
        TickEvent.SERVER_LEVEL_POST.register(ModEvents::weatherControl);
        TickEvent.PLAYER_POST.register(ModEvents::parryTick);
        TickEvent.PLAYER_POST.register(ModEvents::damageLining);
        TickEvent.PLAYER_POST.register(ModEvents::listCodex);
        CommandRegistrationEvent.EVENT.register(ModEvents::commands);
        InteractionEvent.RIGHT_CLICK_BLOCK.register(ModEvents::blockInteract);
        PlayerEvent.ATTACK_ENTITY.register(ModEvents::parryBlocksAttacks);
        PlayerEvent.PLAYER_RESPAWN.register(ModEvents::playerSpawn);
        EntityEvent.LIVING_DEATH.register(ModEvents::midas);

        LivingDamageEvent.Pre.EVENT.register(ModEvents::entityDamaged);
        LivingDamageEvent.Pre.EVENT.register(ModEvents::parryDamage);
        LivingDamageEvent.Pre.EVENT.register(ModEvents::inexperienced);
        LivingDamageEvent.Pre.EVENT.register(ModEvents::attributesPre);
        LivingDamageEvent.Pre.EVENT.register(ModEvents::contractDamage);
        LivingDamageEvent.Pre.EVENT.register(ModEvents::cowardly);
        LivingDamageEvent.Post.EVENT.register(ModEvents::spawnFrozenRemnants);
        LivingDamageEvent.Post.EVENT.register(ModEvents::targetCodex);
        LivingDamageEvent.Post.EVENT.register(ModEvents::flame);
        LivingDamageEvent.Post.EVENT.register(ModEvents::leech);
        LivingDamageEvent.Post.EVENT.register(ModEvents::spin);
        LivingDamageEvent.Post.EVENT.register(ModEvents::uppercut);
        LivingDamageEvent.Post.EVENT.register(ModEvents::attributesPost);
        LivingDamageEvent.Post.EVENT.register(ModEvents::sticky);
        LivingDamageEvent.Post.EVENT.register(ModEvents::slippery);
        LivingIncomingDamageEvent.EVENT.register(ModEvents::hitEntity);
        LivingIncomingDamageEvent.EVENT.register(ModEvents::catlike);
        LivingIncomingDamageEvent.EVENT.register(ModEvents::delayAccumulate);
        LivingKnockBackEvent.EVENT.register(ModEvents::parryKnockback);
        LivingChangeTargetEvent.EVENT.register(ModEvents::smelly);
        LivingEntityUseItemEvent.Start.EVENT.register(ModEvents::food);
        LivingEntityUseItemEvent.Start.EVENT.register(ModEvents::slippery);
        LivingEntityUseItemEvent.Tick.EVENT.register(ModEvents::spicyStew);
        MobEffectEvent.Remove.EVENT.register(ModEvents::cancelClearingEffects);
        LivingUseTotemEvent.EVENT.register(ModEvents::totem);
        FinalizeSpawnEvent.EVENT.register(ModEvents::spawnEntity);
        CriticalHitEvent.EVENT.register(ModEvents::critical);
        AdvancementProgressEvent.EVENT.register(ModEvents::listCodex);
        LivingTickEvent.EVENT.register(ModEvents::parryContractEffects);
        LivingTickEvent.EVENT.register(ModEvents::filterHitEntity);
        LivingTickEvent.EVENT.register(ModEvents::charged);
        LivingTickEvent.EVENT.register(ModEvents::transport);
        LivingTickEvent.EVENT.register(ModEvents::palpitations);
        LivingTickEvent.EVENT.register(ModEvents::delayApply);
        LivingTickEvent.EVENT.register(ModEvents::dull);
    }

    public static void serverStarted(MinecraftServer server) {
        Frostbite.SERVER = server;
    }

    public static void entityDamaged(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            ((PlayerWrapper) player).frostbite$addDamage(event.getNewDamage());
        }
    }

    public static void parryDamage(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof Player player)
                || !ParryManager.isParrying(player)
                || !ParryManager.isAttackingDamage(event.getSource())) return;
        boolean perfect = ParryManager.ticks(player) < ParryManager.PERFECT_WINDOW;
        float multiplier = ParryManager.damageMultiplier(player);
        event.setNewDamage(event.getNewDamage() * multiplier);
        DataHelper.setData(player, ParryManager.KNOCKBACK_MULTIPLIER, multiplier);
        DataHelper.setData(player, ParryManager.KNOCKBACK_PENDING_TICK, player.tickCount);
        DataHelper.setData(player, ParryManager.PERFECT_FLASH, perfect ? 1 : 0);

        if (player.level() instanceof ServerLevel serverLevel) {
            LivingEntity opponent = getParryOpponent(event.getSource());
            if (perfect && LivingContractInfo.hasAppliedAttribute(player, ContractAttributes.REPEL)) {
                repel(serverLevel, player);
            }
            if (opponent != null) {
                if (LivingContractInfo.hasAppliedAttributeOnWeapon(player, ContractAttributes.WEBBING)) {
                    webbing(opponent, LivingContractInfo.getStatOnWeapon(player, ContractAttributes.WEBBING));
                }
                if (perfect && LivingContractInfo.hasAppliedAttributeOnWeapon(player, ContractAttributes.LOCK_IN)) {
                    lockIn(opponent, LivingContractInfo.getStatOnWeapon(player, ContractAttributes.LOCK_IN));
                }
            }
        }

        if (perfect) {
            player.level().playSound(null, player.blockPosition(), SoundRegistry.PARRY.get(),
                    net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    public static void parryKnockback(LivingKnockBackEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        float multiplier = DataHelper.getFloat(player, ParryManager.KNOCKBACK_MULTIPLIER);
        if (multiplier <= 0.0F) return;
        event.setStrength(event.getStrength() * multiplier);
        DataHelper.setData(player, ParryManager.KNOCKBACK_MULTIPLIER, 0.0F);
    }

    public static void parryTick(Player player) {
        if (player.level().isClientSide()) return;
        if (DataHelper.getFloat(player, ParryManager.KNOCKBACK_MULTIPLIER) > 0.0F
                && DataHelper.getInt(player, ParryManager.KNOCKBACK_PENDING_TICK) != player.tickCount) {
            DataHelper.setData(player, ParryManager.KNOCKBACK_MULTIPLIER, 0.0F);
        }
        ParryManager.tick(player);
    }

    public static EventResult parryBlocksAttacks(Player player, Level level, Entity target, InteractionHand hand, EntityHitResult hitResult) {
        return ParryManager.isParrying(player) ? EventResult.interruptFalse() : EventResult.pass();
    }

    public static void parryContractEffects(LivingTickEvent event) {
        if (!(event.getEntity() instanceof LivingEntity entity) || entity.level().isClientSide()) return;

        int lockEndTick = DataHelper.getInt(entity, LOCK_IN_END_TICK);
        if (lockEndTick > entity.tickCount) {
            List<Float> anchor = DataHelper.getList(entity, LOCK_IN_ANCHOR);
            if (anchor.size() >= 3) {
                entity.setPos(anchor.get(0), anchor.get(1), anchor.get(2));
                entity.setDeltaMovement(Vec3.ZERO);
            }
        } else if (lockEndTick != 0) {
            DataHelper.setData(entity, LOCK_IN_END_TICK, 0);
            DataHelper.setData(entity, LOCK_IN_ANCHOR, List.of());
        }

        int webbingTicks = DataHelper.getInt(entity, WEBBING_TICKS);
        int webbingStacks = DataHelper.getInt(entity, WEBBING_STACKS);
        if (webbingTicks <= 0) {
            if (webbingStacks != 0) {
                DataHelper.setData(entity, WEBBING_STACKS, 0);
                DataHelper.setData(entity, WEBBING_BASE_AMPLIFIER, 0);
            }
            return;
        }

        if (webbingStacks <= 0) {
            DataHelper.setData(entity, WEBBING_TICKS, 0);
            return;
        }

        DataHelper.setData(entity, WEBBING_TICKS, webbingTicks - 1);
        int amplifier = Math.min(webbingStacks - 1 + DataHelper.getInt(entity, WEBBING_BASE_AMPLIFIER), 4);
        entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 2, amplifier));
    }

    private static LivingEntity getParryOpponent(DamageSource source) {
        if (source.getEntity() instanceof LivingEntity living) return living;
        if (source.getDirectEntity() instanceof LivingEntity living) return living;
        return null;
    }

    private static void repel(ServerLevel level, Player player) {
        level.sendParticles(ParticleRegistry.SHOCKWAVE_PARTICLE.get(),
                player.getX(), player.getY() + 0.1, player.getZ(),
                24, REPEL_RADIUS * 0.55, 0.15, REPEL_RADIUS * 0.55, 0.02);

        AABB area = player.getBoundingBox().inflate(REPEL_RADIUS);
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area,
                target -> target != player && target.isAlive())) {
            Vec3 direction = target.position().subtract(player.position());
            double distance = Math.sqrt(direction.x * direction.x + direction.z * direction.z);
            if (distance < 0.001) direction = player.getViewVector(1.0F);
            else direction = new Vec3(direction.x / distance, 0.0, direction.z / distance);

            DamageSource damageSource = target.damageSources().playerAttack(player);
            if (target.hurtServer(level, damageSource, 2.0F)) {
                target.knockback(0.4F, -direction.x, -direction.z, damageSource, 2.0F);
            }
        }
    }

    private static void lockIn(LivingEntity target, float durationSeconds) {
        if (random.nextFloat() >= Mth.clamp(durationSeconds / 2.0F, 0.0F, 1.0F)) return;

        int durationTicks = Math.max(1, Mth.ceil(durationSeconds * 20.0F));
        DataHelper.setData(target, LOCK_IN_END_TICK, target.tickCount + durationTicks);
        DataHelper.setData(target, LOCK_IN_ANCHOR,
                List.of((float) target.getX(), (float) target.getY(), (float) target.getZ()));
        target.setDeltaMovement(Vec3.ZERO);
    }

    private static void webbing(LivingEntity target, float slowAmount) {
        int stacks = Math.min(5, DataHelper.getInt(target, WEBBING_STACKS) + 1);
        DataHelper.setData(target, WEBBING_STACKS, stacks);
        int baseAmplifier = Mth.clamp(Mth.ceil(slowAmount / 25.0F) - 1, 0, 2);
        DataHelper.setData(target, WEBBING_BASE_AMPLIFIER,
                Math.max(baseAmplifier, DataHelper.getInt(target, WEBBING_BASE_AMPLIFIER)));
        DataHelper.setData(target, WEBBING_TICKS, 60);
        target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 2,
                Math.min(stacks - 1 + baseAmplifier, 4)));
    }

    public static void weatherControl(ServerLevel level) {
        if (level instanceof ServerLevel serverLevel && serverLevel.dimensionType().hasSkyLight()) {
            if (isFrostbite(serverLevel)) {
                serverLevel.setRainLevel(0.0F);
                serverLevel.setThunderLevel(0.0F);
            }

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

    public static void spawnEntity(FinalizeSpawnEvent event) {
        if (event.getEntity() instanceof TemperatureEntity temperatureEntity) {
            ((TE) temperatureEntity).setInnerTemp(temperatureEntity.getSpawnTemperature());
            ((TE) temperatureEntity).setOuterTemp(temperatureEntity.getSpawnTemperature());
        } else {
            ((TE) event.getEntity()).setInnerTemp(20);
            ((TE) event.getEntity()).setOuterTemp(20);
        }
    }

    public static void playerSpawn(ServerPlayer player, boolean conqueredEnd, Entity.RemovalReason removalReason) {
        ((TE) player).setOuterTemp(TemperatureManager.MAX_TEMP);
        ((TE) player).setInnerTemp(TemperatureManager.MAX_TEMP);
    }

    public static void food(LivingEntityUseItemEvent.Start event) {
        Contract c;
        // TODO: change to tags
        if (event.getEntity() instanceof Player player && (c = player.getData(AttachmentRegistry.LIVING_CONTRACT_INFO).contract()) != null) {
            if (c.hasAttribute(ContractAttributes.DIABETIC) && Util.isSweet(event.getItem())) {
                event.setCanceled(true);
            } else if (c.hasAttribute(ContractAttributes.VEGETARIAN) && Util.isMeat(event.getItem())) {
                event.setCanceled(true);
            }
        }
    }

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

    public static void cancelClearingEffects(MobEffectEvent.Remove event) {
        if (event.getEffectInstance() != null && EffectRegistry.isCurse(event.getEffectInstance().getEffect()) && event.getEntity() instanceof Player) {
            event.setCanceled(true);
        }
    }

    public static void totem(LivingUseTotemEvent event) {
        if (event.getTotem().is(ItemRegistry.LAST_STAND) && event.getEntity() instanceof Player player &&
                player.level() instanceof ServerLevel serverLevel) {
            ((PlayerWrapper) player).frostbite$startAccumulatingDamage(serverLevel);
        }
    }

    public static void commands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection) {
        SpawnLastStandCommand.register(dispatcher, buildContext);
        WeatherCommand.register(dispatcher);
        GiveFragmentCommand.register(dispatcher);
    }

    public static void damageLining(Player player) {
        if (player.level() instanceof ServerLevel serverLevel && isFrostbite(serverLevel) && serverLevel.getGameTime() % 400 == 0) {
            for (int i = 43; i < 48; i++) {
                player.getInventory().getItem(i).hurtAndBreak(1, serverLevel, (ServerPlayer) player, (item) -> {});
            }
        }
    }

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
                frozenRemnants.setItems(NonNullList.of(ItemStack.EMPTY, items.toArray(ItemStack[]::new)));
                frozenRemnants.setTarget(player);

                serverLevel.addFreshEntityWithPassengers(frozenRemnants);
                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, player.position(), GameEvent.Context.of(player));
            }
        }
    }

    public static void serverTick(MinecraftServer server) {
        server.getAllLevels().forEach((level) -> {
            HuntersCatalyst.tickFlashDeaggro(level);
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
                try {
                    entities.forEach((entity) -> {
                        if (entity instanceof LivingEntity livingEntity) {
                            TemperatureManager.updateEntityTemperature(livingEntity);
                        }
                    });
                } catch (ArrayIndexOutOfBoundsException e) {
                    Frostbite.LOGGER.error("Couldn't loop over entities: {}", Arrays.toString(e.getStackTrace()));
                }
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
                        pseudoEntitiesToRemove.addAll(pseudoEntities);
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

////    public static void entityJoin(EntityJoinLevelEvent event) {
//        if (!event.getLevel().dimension().equals(Util.FROSTBITE_KEY) || event.getLevel().isClientSide() || event.loadedFromDisk()) return;
//        if (Arrays.stream(DataHelper.getString(event.getLevel(), "tracked_entity_types").split(";"))
//                .anyMatch(s -> s.equals(event.getEntity().typeHolder().value().getDescriptionId()))) {
//            int count = DataHelper.getInt(event.getLevel(), event.getEntity().typeHolder().value().getDescriptionId() + "Count");
//            DataHelper.setData(event.getLevel(), event.getEntity().typeHolder().value().getDescriptionId() + "Count",
//                    Math.max(0, DataHelper.getInt(event.getLevel(), event.getEntity().typeHolder().value().getDescriptionId() + "Count") + 1));
//        }
//    }
//
////    public static void entityLeave(EntityLeaveLevelEvent event) {
//        if (!event.getLevel().dimension().equals(Util.FROSTBITE_KEY) || event.getLevel().isClientSide()) return;
//        if (Arrays.stream(DataHelper.getString(event.getLevel(), "tracked_entity_types").split(";"))
//                .anyMatch(s -> s.equals(event.getEntity().typeHolder().value().getDescriptionId()))) {
//            int count = DataHelper.getInt(event.getLevel(), event.getEntity().typeHolder().value().getDescriptionId() + "Count");
//            DataHelper.setData(event.getLevel(), event.getEntity().typeHolder().value().getDescriptionId() + "Count",
//                    Math.max(0, DataHelper.getInt(event.getLevel(), event.getEntity().typeHolder().value().getDescriptionId() + "Count") - 1));
//        }
//    }

    public static EventResult blockInteract(Player player, InteractionHand hand, BlockPos pos, Direction face) {
        Level level = player.level();
        BlockState state = level.getBlockState(pos);
        BlockEntity entity = level.getBlockEntity(pos);
        ItemStack stack = player.getItemInHand(hand);
        boolean canceled = false;

        if (stack.is(Items.FLINT_AND_STEEL) &&
                state.getBlock() instanceof BrazierBlock) {
            level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 2);

            canceled = true;
        }

        if (Util.isSnowy(stack.getItem()) &&
                (state.getBlock() instanceof SnowableSlabBlock &&
                state.getValue(SnowableSlabBlock.TYPE).equals(SlabType.BOTTOM)) ||
                (state.getBlock() instanceof SnowableStairBlock &&
                state.getValue(SnowableStairBlock.HALF).equals(Half.BOTTOM)) ||
                state.getBlock() instanceof SnowableWallBlock
        ) {
            state = state.setValue(SNOWY, true);
            stack.consume(1, player);
            level.setBlock(pos, state, 2);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));
            canceled = true;
        }

        if (player.isShiftKeyDown() &&
                player.isCreative() &&
                state.getBlock() instanceof RuneBlock &&
                entity instanceof RuneBlockEntity) {
            RuneLootLevel next = state.getValue(RuneBlock.LOOT_LEVEL).next();
            level.setBlock(pos, state
                    .setValue(RuneBlock.LOOT_LEVEL, next)
                    .setValue(RuneBlock.OPENED, false), 2);
            DataHelper.setBlockData(level.getChunkAt(pos), pos, "health", next.getMaxHealth());
            DataHelper.setBlockData(level.getChunkAt(pos), pos, "max_health", next.getMaxHealth());

            canceled = true;
        }

        return canceled ? EventResult.interruptFalse() : EventResult.pass();
    }

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

    public static void inexperienced(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof Player player && event.getEntity() instanceof LivingEntity) {
            if (LivingContractInfo.hasAppliedAttribute(player, ContractAttributes.INEXPERIENCED)) {
                event.setNewDamage(Math.min(event.getNewDamage(), player.experienceLevel));
            }
        }
    }

    public static void flame(LivingDamageEvent.Post event) {
        if (event.getSource().getDirectEntity() instanceof LivingEntity entity && event.getEntity() instanceof Player player) {
            if (LivingContractInfo.hasAppliedAttribute(player, ContractAttributes.FLAME)) {
                entity.setRemainingFireTicks(entity.getRemainingFireTicks()
                        + 20 * (int) LivingContractInfo.getStat(player, ContractAttributes.FLAME));
            }
        }
    }

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

    public static void filterHitEntity(LivingTickEvent event) {
        if (event.getEntity() instanceof LivingEntity entity && entity.tickCount % 200 == 0) {
            DataHelper.filterHitEntities(entity, entity.level());
        }
    }

    public static void catlike(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity living && event.getSource().is(DamageTypes.FALL)) {
            if (LivingContractInfo.hasAppliedAttribute(living, ContractAttributes.CATLIKE)) {
                event.setCanceled(true);
            }
        }
    }

    public static void smelly(LivingChangeTargetEvent event) {
        if (event.getNewAboutToBeSetTarget() instanceof Player player) {
            if (LivingContractInfo.hasAppliedAttribute(player, ContractAttributes.SMELLY) && !DataHelper.hasHitEntity(player, event.getEntity())) {
                event.setCanceled(true);
            }
        }
    }

    public static void charged(LivingTickEvent event) {
        if (event.getEntity() instanceof LivingEntity entity && LivingContractInfo.hasAppliedAttribute(entity, ContractAttributes.CHARGED)) {
            if (entity.tickCount % 1200 == 500 && random.nextFloat() < LivingContractInfo.getStat(entity, ContractAttributes.CHARGED) / 100) {
                LightningBolt bolt = new LightningBolt(EntityTypes.LIGHTNING_BOLT, entity.level());
                EntityPlatform.setLightningDamage(bolt, 3);
                bolt.moveOrInterpolateTo(PositionPath.of(new Vec3(entity.getX(), entity.getY(), entity.getZ())));
                entity.level().addFreshEntity(bolt);
            }
        }
    }

    public static void transport(LivingTickEvent event) {
        if (event.getEntity() instanceof LivingEntity entity &&
                LivingContractInfo.hasAppliedAttribute(entity, ContractAttributes.TRANSPORT) &&
                entity.level() instanceof ServerLevel serverLevel) {
            if (entity.tickCount % 100 == 40 && random.nextFloat() < LivingContractInfo.getStat(entity, ContractAttributes.TRANSPORT) / 100) {
                Util.teleportEntityRandomly(serverLevel, 32, entity);
            }
        }
    }

    public static void palpitations(LivingTickEvent event) {
        if (event.getEntity() instanceof LivingEntity entity && LivingContractInfo.hasAppliedAttribute(entity, ContractAttributes.PALPITATIONS)) {
            if (entity.tickCount % (LivingContractInfo.getStat(entity, ContractAttributes.PALPITATIONS) * 20) == 300) {
                entity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 100, 0));
                entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0));
                entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 2));
                entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
            }
        }
    }

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

    public static void delayApply(LivingTickEvent event) {
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

    public static void dull(LivingTickEvent event) {
        if (event.getEntity() instanceof LivingEntity entity &&
                LivingContractInfo.hasAppliedAttributeOnWeapon(entity, ContractAttributes.DULL) &&
                entity.tickCount % 20 == 0) {
            ItemStack stack = entity.getItemInHand(InteractionHand.MAIN_HAND);
            Util.setEnchantmentsLevelOne(stack);
        }
    }

    public static EventResult midas(LivingEntity victim, DamageSource source) {
        if (source.getEntity() instanceof LivingEntity entity &&
                LivingContractInfo.hasAppliedAttributeOnWeapon(entity, ContractAttributes.MIDAS) &&
                entity.level() instanceof ServerLevel serverLevel) {
            if (random.nextFloat() >= LivingContractInfo.getStatOnWeapon(entity, ContractAttributes.MIDAS) / 100) return EventResult.pass();
            ItemEntity itemEntity = new ItemEntity(serverLevel,
                    victim.getX(),
                    victim.getY(),
                    victim.getZ(),
                    new ItemStack(Util.randomValuable(), 1));
            serverLevel.addFreshEntity(itemEntity);
        }
        return EventResult.pass();
    }

    public static void leech(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity target &&
                LivingContractInfo.hasAppliedAttributeOnWeapon(attacker, ContractAttributes.LEECH)) {
            ((TE) attacker).increaseTemperature(LivingContractInfo.getStatOnWeapon(attacker, ContractAttributes.LEECH), false);
            ((TE) target).decreaseTemperature(LivingContractInfo.getStatOnWeapon(attacker, ContractAttributes.LEECH), false);
        }
    }

////    public static void illusions(LivingDamageEvent.Post event) {
//        if (event.getSource().getEntity() instanceof LivingEntity &&
//                event.getEntity() instanceof Player player &&
//                event.getEntity().level() instanceof ServerLevel level &&
//                LivingContractInfo.hasAppliedAttribute(player, ContractAttributes.ILLUSIONS)) {
//            for (int i = 0; i < LivingContractInfo.getStat(player, ContractAttributes.ILLUSIONS); i++) {
//                PlayerIllusionEntity illusion = new PlayerIllusionEntity(null, level);
//                illusion.moveOrInterpolateTo(player.position(), 0, 0);
//                Util.teleportEntityRandomly(level, 16, illusion);
//                level.addFreshEntity(illusion);
//                illusion.setOwnerUUID(player.getUUID());
//                DataHelper.setData(player, "illusion_UUIDs", DataHelper.getString(player, "illusion_UUIDs") + illusion.getStringUUID() + ";");
//            }
//        }
//    }

    public static void spin(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity target &&
                LivingContractInfo.hasAppliedAttributeOnWeapon(attacker, ContractAttributes.SPIN)) {
            if (random.nextFloat() >= LivingContractInfo.getStatOnWeapon(attacker, ContractAttributes.SPIN) / 100) return;
            target.setYHeadRot(target.getYHeadRot() + 180);
            target.setYRot(target.getYRot() + 180);
        }
    }

    public static void uppercut(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity target &&
                LivingContractInfo.hasAppliedAttributeOnWeapon(attacker, ContractAttributes.UPPERCUT)) {
            if (random.nextFloat() >= LivingContractInfo.getStatOnWeapon(attacker, ContractAttributes.UPPERCUT) / 100) return;
            target.setXRot(-90);
        }
    }

    public static void attributesPre(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity target) {
            if (target.getAttributes().hasAttribute(AttributeRegistry.DEFENCE))
                event.setNewDamage((float) (event.getNewDamage() * (1 - target.getAttributeValue(AttributeRegistry.DEFENCE))));

            if (event.getSource().isDirect() && attacker.getAttributes().hasAttribute(AttributeRegistry.MELEE_DAMAGE))
                event.setNewDamage((float) (event.getNewDamage() * (1 + attacker.getAttributeValue(AttributeRegistry.MELEE_DAMAGE))));

            if (event.getSource().is(DamageTypes.MAGIC) && attacker.getAttributes().hasAttribute(AttributeRegistry.SPELL_DAMAGE)) // TODO: make spell tag and add all required spells
                event.setNewDamage((float) (event.getNewDamage() * (1 + attacker.getAttributeValue(AttributeRegistry.SPELL_DAMAGE))));
        }
    }

    public static void attributesPost(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity target &&
                target.level() instanceof ServerLevel serverLevel) {
            if (attacker.getAttributes().hasAttribute(AttributeRegistry.LIFE_STEAL))
                attacker.heal((float) (event.getInflictedDamage() * attacker.getAttributeValue(AttributeRegistry.LIFE_STEAL)));

            if (target.getAttributes().hasAttribute(AttributeRegistry.THORNS)) {
                float damage = (float) (event.getInflictedDamage() * target.getAttributeValue(AttributeRegistry.THORNS));
                if (damage > 0 && !event.getSource().is(DamageTypes.THORNS)) attacker.hurtServer(serverLevel, attacker.damageSources().thorns(attacker), damage);
            }

            if (target.getAttributes().hasAttribute(AttributeRegistry.POISON) && target.getAttributeValue(AttributeRegistry.POISON) > 0) {
                attacker.addEffect(new MobEffectInstance(MobEffects.POISON, (int) target.getAttributeValue(AttributeRegistry.POISON) * 20), target);
            }

            if (attacker.getAttributes().hasAttribute(AttributeRegistry.TEMPERATURE_STEAL)) {
                float steal = (float) (event.getInflictedDamage() * attacker.getAttributeValue(AttributeRegistry.TEMPERATURE_STEAL));
                ((TE) attacker).increaseTemperature(steal, false);
                ((TE) target).decreaseTemperature(steal, false);
            }

        }
    }

    public static void contractDamage(LivingDamageEvent.Pre event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity victim) {
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
            float inner = ((TE) attacker).getInnerTemp();
            if (LivingContractInfo.hasAppliedAttribute(attacker, ContractAttributes.CHILLY) && inner < 0) {
                add += LivingContractInfo.getStat(attacker, ContractAttributes.CHILLY) * inner / -200;
            }
            if (LivingContractInfo.hasAppliedAttributeOnWeapon(attacker, ContractAttributes.PIERCING)) {
                float damage = event.getOriginalDamage();
                float reducedDamage = CombatRules.getDamageAfterAbsorb(victim, damage, event.getSource(),
                        victim.getArmorValue(), (float) victim.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
                add += LivingContractInfo.getStat(attacker, ContractAttributes.PIERCING) * ((damage - reducedDamage) / damage) / 100;
            }

            float comboAdd = ComboWeapon.getDamageBonus(event.getSource());
            float comboAddBonus = 0;
            if (LivingContractInfo.hasAppliedAttribute(attacker, ContractAttributes.SEQUENCE))
                comboAddBonus = comboAdd * LivingContractInfo.getStat(attacker, ContractAttributes.SEQUENCE) / 100f;
            event.setNewDamage((event.getNewDamage() + comboAdd) * (1 + add) + comboAddBonus);
        }
    }

    public static void cowardly(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof LivingEntity living) {
            if (LivingContractInfo.hasAppliedAttribute(living, ContractAttributes.COWARDLY) && living.getHealth() < (living.getMaxHealth() * 0.5)) {
                event.setNewDamage(event.getNewDamage() * (1 - LivingContractInfo.getStat(living, ContractAttributes.COWARDLY) / 100));
            }
        }
    }

    public static void sticky(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker &&
                event.getEntity() instanceof LivingEntity target &&
                LivingContractInfo.hasAppliedAttributeOnWeapon(attacker, ContractAttributes.STICKY)) {
            Vec3 delta = target.position().subtract(attacker.position()).normalize().scale(LivingContractInfo.getStatOnWeapon(attacker, ContractAttributes.STICKY) / 100);
            if (attacker instanceof Player player) {
                MovePlayerEvent.EVENT.invoker().accept(new MovePlayerEvent(delta, player.getUUID()));
            } else attacker.setDeltaMovement(delta);
        }
    }

    public static void critical(CriticalHitEvent event) {
        if (LivingContractInfo.hasAppliedAttributeOnWeapon(event.getEntity(), ContractAttributes.CRITICAL)) {
            event.setDamageMultiplier(event.getDamageMultiplier() + LivingContractInfo.getStatOnWeapon(event.getEntity(), ContractAttributes.CRITICAL) / 100);
        }
    }

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

    public static void listCodex(Player player) {
        if (player.tickCount % 20 != 0) return;

        for (CodexTab tab : Codex.TABS) {
            if (tab instanceof ListCodexTab listTab) {
                for (CodexEntry entry : listTab.entries) {
                    if (entry instanceof ListCodexEntry listEntry &&
                        !CodexEntry.playerHasEntry(player, entry) &&
                        listEntry.function != null &&
                        listEntry.function.apply(new EntryContext(player.level(), player))) {

                        CodexEntry.addEntryToPlayer(player, entry);
                    }
                }
            }
        }
    }

    public static void listCodex(AdvancementProgressEvent event) {
        String[] s = event.getCriterionName().split(":");
        if (s.length < 2) return;
        String entry = s[1];
        if (Codex.TRACKED_LIST_ENTRIES.contains(entry)) {
            CodexEntry.addEntryToPlayer(event.getEntity(), entry);
        }
    }
}
