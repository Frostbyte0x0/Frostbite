package org.exodusstudio.frostbite.common.event;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingUseTotemEvent;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.event.entity.player.UseItemOnBlockEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.HeaterBlock;
import org.exodusstudio.frostbite.common.block.HeaterStorage;
import org.exodusstudio.frostbite.common.commands.SpawnLastStandCommand;
import org.exodusstudio.frostbite.common.entity.custom.FrozenRemnantsEntity;
import org.exodusstudio.frostbite.common.item.custom.last_stand.LastStand;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Frostbite.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {
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
    public static void fog(ViewportEvent.RenderFog event) {
        if (event.getMode() == FogRenderer.FogMode.FOG_TERRAIN) {
            event.setNearPlaneDistance(-50f);
            event.setFarPlaneDistance(100f);
        } else if (event.getMode() == FogRenderer.FogMode.FOG_SKY) {
            event.setNearPlaneDistance(-50f);
            event.setFarPlaneDistance(100f);
        }

        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void fogColour(ViewportEvent.ComputeFogColor event) {
        event.setRed(73 / 255f);
        event.setGreen(106 / 255f);
        event.setBlue(184 / 255f);
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
    }

    @SubscribeEvent
    public static void livingDamagedEvent(LivingDamageEvent.Post event) {
        if (event.getEntity().isDeadOrDying() && event.getEntity() instanceof Player player) {
            if (player.level() instanceof ServerLevel serverLevel && FrozenRemnantsEntity.shouldSpawnFrozenRemnants(serverLevel)) {
                FrozenRemnantsEntity frozenRemnants = new FrozenRemnantsEntity(EntityRegistry.FROZEN_REMNANTS.get(), serverLevel);
                frozenRemnants.setOwner(player);
                frozenRemnants.moveTo(player.position(), 0.0F, 0.0F);
                NonNullList<ItemStack> items = NonNullList.create();
                items.addAll(player.getInventory().items);
                items.addAll(player.getInventory().armor);
                items.addAll(player.getInventory().offhand);
                frozenRemnants.setItems(items);
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
//                Frostbite.savedHeaters = new ArrayList<>(Frostbite.savedHeaters.stream().filter(heater ->
//                                !(level.getBlockState(heater.getPos()).getBlock() instanceof HeaterBlock) ||
//                                !(heater.getDimensionName().equals(level.dimension().location().toString())))
//                        .toList());
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
        if (event.getPlayer().getItemInHand(event.getHand()).is(Items.FLINT_AND_STEEL) &&
                event.getLevel() instanceof ServerLevel serverLevel &&
                serverLevel.getBlockState(event.getPos()).getBlock() instanceof HeaterBlock block &&
                Frostbite.savedHeaters.stream().noneMatch(heater ->
                        heater.getPos().equals(event.getPos()) &&
                        heater.getDimensionName().equals(serverLevel.dimension().location().toString()))) {
            Frostbite.savedHeaters.add(new HeaterStorage(event.getPos(), block, serverLevel.dimension().location().toString()));
            event.cancelWithResult(InteractionResult.FAIL);
        }
    }
//
//    @SubscribeEvent
//    public static void heaterDestroyed(VanillaGameEvent event) {
//        if (event.getVanillaEvent().getRegisteredName().equals(GameEvent.BLOCK_DESTROY.getRegisteredName())) {
//            BlockPos pos = BlockPos.containing(event.getEventPosition());
//            if (event.getLevel().getBlockState(pos).getBlock() instanceof HeaterBlock) {
//                Frostbite.savedHeaters = new ArrayList<>(Frostbite.savedHeaters.stream().filter(heater ->
//                                !heater.getPos().equals(pos) ||
//                                        !heater.getDimensionName().equals(event.getLevel().dimension().location().toString()))
//                        .toList());
//            }
//        }
//    }

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
}
