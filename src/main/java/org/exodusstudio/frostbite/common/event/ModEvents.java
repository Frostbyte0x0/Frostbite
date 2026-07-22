package org.exodusstudio.frostbite.common.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
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
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.AdvancementEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
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
import org.exodusstudio.frostbite.client.gui.CodexScreen;
import org.exodusstudio.frostbite.common.block.HeaterBlock;
import org.exodusstudio.frostbite.common.commands.GiveFragmentCommand;
import org.exodusstudio.frostbite.common.commands.SpawnLastStandCommand;
import org.exodusstudio.frostbite.common.commands.WeatherCommand;
import org.exodusstudio.frostbite.common.component.CooldownData;
import org.exodusstudio.frostbite.common.contracts.Contract;
import org.exodusstudio.frostbite.common.contracts.ContractAttribute;
import org.exodusstudio.frostbite.common.contracts.ContractAttributes;
import org.exodusstudio.frostbite.common.contracts.PlayerContractInfo;
import org.exodusstudio.frostbite.common.entity.custom.misc.FrozenRemnantsEntity;
import org.exodusstudio.frostbite.common.entity.custom.monk.MonkEntity;
import org.exodusstudio.frostbite.common.item.contract.ContractFragmentItem;
import org.exodusstudio.frostbite.common.item.weapons.ComboWeapon;
import org.exodusstudio.frostbite.common.item.weapons.SeriousAttackWeapon;
import org.exodusstudio.frostbite.common.item.weapons.elf.ModeWeapon;
import org.exodusstudio.frostbite.common.network.StaffPayload;
import org.exodusstudio.frostbite.common.particle.options.StringParticleOption;
import org.exodusstudio.frostbite.common.registry.*;
import org.exodusstudio.frostbite.common.structures.FTOPortal;
import org.exodusstudio.frostbite.common.structures.OTFPortal;
import org.exodusstudio.frostbite.common.util.*;
import org.exodusstudio.frostbite.common.weather.WeatherInfo;
import org.joml.Vector3f;

import java.util.*;

import static org.exodusstudio.frostbite.common.util.Util.isFrostbite;

@EventBusSubscriber(modid = Frostbite.MOD_ID)
public class ModEvents {
    public static RandomSource random = RandomSource.create();

    @SubscribeEvent
    public static void reset(ServerStoppedEvent event) {
        OTFPortal.canSpawn = true;
        FTOPortal.canSpawn = true;
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
                        j = WeatherInfo.WHITEOUT_DURATION.sample(serverLevel.getRandom());
                        Frostbite.weatherInfo.setWhiteouting();
                    } else {
                        j = WeatherInfo.WHITEOUT_DELAY.sample(serverLevel.getRandom());
                        Frostbite.weatherInfo.setSnowing();
                    }

                    if (k > 0) {
                        if (--k == 0) {
                            flag2 = !flag2;
                        }
                    } else if (flag2) {
                        k = WeatherInfo.BLIZZARD_DURATION.sample(serverLevel.getRandom());
                        Frostbite.weatherInfo.setBlizzarding();
                    } else {
                        k = WeatherInfo.BLIZZARD_DELAY.sample(serverLevel.getRandom());
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
    public static void spicyStew(FinalizeSpawnEvent event) {
        if (event.getEntity() instanceof TemperatureEntity temperatureEntity) {
            ((TE) temperatureEntity).setInnerTemp(temperatureEntity.getSpawnTemperature());
            ((TE) temperatureEntity).setOuterTemp(temperatureEntity.getSpawnTemperature());
        }
    }

    @SubscribeEvent
    public static void food(LivingEntityUseItemEvent.Start event) {
        Contract c;
        // TODO: change to tags
        if (event.getEntity() instanceof Player player && (c = player.getData(AttachmentRegistry.PLAYER_CONTRACT_INFO.get()).contract()) != null) {
            if (c.hasAttribute(ContractAttributes.DIABETIC) && Util.isSweet(event.getItem())) {
                event.setCanceled(true);
            } else if (c.hasAttribute(ContractAttributes.VEGETARIAN) && Util.isMeat(event.getItem())) {
                event.setCanceled(true);
            }
        }
    }

    @SuppressWarnings("DataFlowIssue")
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
    public static void ambience(PlayerTickEvent.Post event) {
        for (SoundRegistry.Ambience ambience : SoundRegistry.Ambience.AMBIENCES) {
            if (ambience.shouldPlay.apply(event.getEntity().level()) && event.getEntity().level() instanceof ClientLevel level) {
                if (level.getGameTime() + ambience.fadeLength - ambience.startTime > ambience.length) {
                    ambience.startTime = level.getGameTime();
                    level.playLocalSound(event.getEntity(), ambience.soundEvent.get(), SoundSource.AMBIENT, ambience.volume, 1);
                }
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
                String chargeAttackRenderable = entry.getValue().getFirst();
                Renderable.removeRenderable(user, chargeAttackRenderable);
            }

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
                Frostbite.addedBosses.putAll(Frostbite.bossesToAdd);
                Frostbite.bossesToAdd.clear();
            }
        });
        TemperatureManager.getInstance().updateEntityTemperatures(entities);
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

    @SubscribeEvent
    public static void codexOpen(InputEvent.Key event) {
        if (KeyMappingRegistry.CODEX.isActiveAndMatches(InputConstants.getKey(event.getKeyEvent()))) {
            while (KeyMappingRegistry.CODEX.consumeClick()) {
                Minecraft.getInstance().gui.setScreen(new CodexScreen());
            }
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
    public static void damageParticle(LivingDamageEvent.Post event) {
        if (event.getSource().getEntity() instanceof Player && event.getEntity() instanceof LivingEntity target && Minecraft.getInstance().level != null) {
            Vector3f speed = new Vector3f(
                    random.nextFloat(),
                    Math.abs(random.nextFloat()),
                    random.nextFloat())
                    .normalize(0.1f);

            Minecraft.getInstance().level.addParticle(
                    StringParticleOption.create(ParticleRegistry.DAMAGE_PARTICLE.get(),
                            String.format("%.1f", event.getInflictedDamage())
                    ),
                    target.getRandomX(0.7F),
                    target.getRandomY() + 1,
                    target.getRandomZ(0.7F),
                    speed.x(),
                    speed.y(),
                    speed.z());

            Frostbite.LOGGER.debug(String.valueOf(event.getInflictedDamage()));
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

    @SubscribeEvent
    public static void contractTooltips(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        if (stack.getItem() instanceof ContractFragmentItem) {
            ContractAttribute a = ContractAttribute.getAttribute(stack);
            if (a == null) return;
            event.getTooltipElements().add(1, Either.left(a.getSmallInfo(player, Either.left(stack))));
            if (Minecraft.getInstance().hasShiftDown()) event.getTooltipElements().add(2, Either.left(a.getExtraInfo(player, Either.left(stack))));
            event.getTooltipElements().remove(Either.left(Component.literal("frostbite:contract_fragment_" + a.id).withStyle(ChatFormatting.DARK_GRAY)));
        }

        Contract c = Contract.getContract(stack);
        if (c == null) return;
        List<ContractAttribute> attributes = c.allAttributes();
        if (attributes.isEmpty()) return;

        for (ContractAttribute a : c.allAttributes()) {
            event.getTooltipElements().add(1, Either.left(a.getSmallInfo(player, Either.left(stack))));
            if (Minecraft.getInstance().hasShiftDown()) event.getTooltipElements().add(2, Either.left(a.getExtraInfo(player, Either.left(stack))));
        }
    }

    @SubscribeEvent
    public static void inventoryContract(ScreenEvent.Render.Post event) {
        Player player = Minecraft.getInstance().player;
        Font font = Minecraft.getInstance().font;
        Level level = Minecraft.getInstance().level;
        GuiGraphicsExtractor graphics = event.getGuiGraphics();
        if (player == null || level == null) return;

        if (event.getScreen() instanceof InventoryScreen || event.getScreen() instanceof CreativeModeInventoryScreen) {
            int screenCenter = graphics.guiHeight() / 2;

            Contract c = PlayerContractInfo.getContract(player);
            if (c == null) return;
            List<ContractAttribute> attributes = c.allAttributes();
            if (attributes.isEmpty()) return;

            int i = -attributes.size() / 2;
            for (ContractAttribute a : attributes) {
                graphics.text(font, a.getExtraInfo(player, Either.right(c)), 0, screenCenter - i * font.lineHeight, 0xFFFFFFFF);
                i++;
                graphics.text(font, a.getSmallInfo(player, Either.right(c)), 0, screenCenter - i * font.lineHeight, 0xFFFFFFFF);
                i++;
            }
        }
    }

    @SubscribeEvent
    public static void comboCharge(RenderGuiLayerEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        Level level = Minecraft.getInstance().level;
        GuiGraphicsExtractor graphics = event.getGuiGraphics();
        if (player == null || level == null) return;
        if (Minecraft.getInstance().gui.hud.isHidden()) return;

        if (event.getName().equals(Identifier.withDefaultNamespace("hotbar"))) {
            int screenCenter = graphics.guiWidth() / 2;
            int barHeight = 16;
            int endY = graphics.guiHeight() - 16 - 3;
            int startY = endY + barHeight;

            for (int i = 0; i < 10; i++) {
                int x;
                ItemStack stack;

                if (i == 9) {
                    stack = player.getOffhandItem();
                    x = player.getMainArm().getOpposite() == HumanoidArm.LEFT ? screenCenter - 91 - 26 : screenCenter + 91 + 10;
                } else {
                    stack = player.getInventory().getItem(i);
                    x = screenCenter - 90 + i * 20 + 2;
                }

                if (!(stack.getItem() instanceof ComboWeapon comboWeapon)) continue;

                int charge = comboWeapon.getCharge(stack);
                float chargeProgress = charge / (float) comboWeapon.chargeRequired();
                graphics.verticalLine(x, startY, endY, 0xFFFFFFFF);
                graphics.verticalLine(x, startY, (int) Mth.lerp(chargeProgress, startY, endY), 0xFF00FF00);

                if (!(comboWeapon instanceof SeriousAttackWeapon seriousAttackWeapon)) continue;

                int cooldown = (int) Math.min(seriousAttackWeapon.getCooldown(), CooldownData.secondsSinceLastUsed(stack, level.getGameTime()));
                float cooldownProgress = cooldown / seriousAttackWeapon.getCooldown();
                graphics.verticalLine(x + 14, startY, endY , 0xFFFFFFFF);
                graphics.verticalLine(x + 14, startY, (int) Mth.lerp(cooldownProgress, startY, endY), 0xFF3898f2);
            }
        }
    }
}