package org.exodusstudio.frostbite.common.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
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
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.HeaterBlock;
import org.exodusstudio.frostbite.common.block.HeaterStorage;
import org.exodusstudio.frostbite.common.commands.SpawnLastStandCommand;
import org.exodusstudio.frostbite.common.entity.custom.FrozenRemnantsEntity;
import org.exodusstudio.frostbite.common.item.last_stand.LastStand;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ItemRegistry;
import org.exodusstudio.frostbite.common.structures.FTOPortal;
import org.exodusstudio.frostbite.common.structures.OTFPortal;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = Frostbite.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {
    public static float shroudedBlendLerp = 0;

    @SubscribeEvent
    public static void resetPortalCount(ServerStoppingEvent event) {
        OTFPortal.count = 0;
        FTOPortal.count = 0;
        shroudedBlendLerp = 0;
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
    public static void fog(ViewportEvent.RenderFog event) {
        Player player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;
        if (player == null || !player.isAlive() || player.isSpectator() || level == null) return;

        if (event.getMode() == FogRenderer.FogMode.FOG_TERRAIN || event.getMode() == FogRenderer.FogMode.FOG_SKY) {
//            int[] smoothColour = smoothColour(player, level);
//
//            int delta = getColourDifference(smoothColour,
//                    new int[] { ARGB.red(8434839), ARGB.green(8434839), ARGB.blue(8434839) });
//
//            int range = 13;

            //if (delta <= range) {
                //float lerp = 1 - (float) delta / range;
            float near = Mth.lerp(shroudedBlendLerp, event.getNearPlaneDistance(), -50f);
            float far = Mth.lerp(shroudedBlendLerp, event.getFarPlaneDistance(), 100f);

            event.setNearPlaneDistance(near);
            event.setFarPlaneDistance(far);
            //}
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void fogColour(ViewportEvent.ComputeFogColor event) {
        Player player = Minecraft.getInstance().player;
        ClientLevel level = Minecraft.getInstance().level;
        if (player == null || !player.isAlive() || player.isSpectator() || level == null) return;

        computeShroudedBlendLerp(level, player, event.getPartialTick());

        // 8434839
        // int colour = level.calculateBlockTint(player.blockPosition().below(), Biome::getGrassColor);


//        int[] smoothColour = smoothColour(player, level);
//
//        int delta = getColourDifference(smoothColour,
//                new int[] { ARGB.red(8434839), ARGB.green(8434839), ARGB.blue(8434839) });
//
//        //Frostbite.LOGGER.debug(String.valueOf(delta));
//
//        int range = 13;

        //if (delta <= range) {
        //float lerp = 1 - (float) delta / range;
        float red = Mth.lerp(shroudedBlendLerp, event.getRed(), 73 / 255f);
        float green = Mth.lerp(shroudedBlendLerp, event.getGreen(), 106 / 255f);
        float blue = Mth.lerp(shroudedBlendLerp, event.getBlue(), 184 / 255f);

        event.setRed(red);
        event.setGreen(green);
        event.setBlue(blue);
        //}


//        event.setRed(73 / 255f);
//        event.setGreen(106 / 255f);
//        event.setBlue(184 / 255f);
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

        } //else if (stack.is(ItemRegistry.FROSTBITTEN_GEM) &&
//                state.getBlock() instanceof ReinforcedBlackIceReceptacleBlock block &&
//                !state.getValue(BlockStateProperties.ACTIVE)) {
//            stack.consume(1, event.getPlayer());
//            BlockState state1 = state.setValue(BlockStateProperties.ACTIVE, true);
//            event.getLevel().setBlock(event.getPos(), state1, 3);
//            event.getLevel().playSound(event.getPlayer(), event.getPos(), SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0F, 1.0F);
//        }
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

    public static int[] smoothColour(Player player, ClientLevel level) {
        int colourX0 = level.calculateBlockTint(player.blockPosition().west(),  Biome::getGrassColor);
        int colourX1 = level.calculateBlockTint(player.blockPosition().east(),  Biome::getGrassColor);
        int colourY0 = level.calculateBlockTint(player.blockPosition().below(), Biome::getGrassColor);
        int colourY1 = level.calculateBlockTint(player.blockPosition().above(), Biome::getGrassColor);
        int colourZ0 = level.calculateBlockTint(player.blockPosition().north(), Biome::getGrassColor);
        int colourZ1 = level.calculateBlockTint(player.blockPosition().south(), Biome::getGrassColor);

        int redX = (int) Mth.lerp(player.getX() % 1, ARGB.red(colourX0), ARGB.red(colourX1));
        int redY = (int) Mth.lerp(player.getY() % 1, ARGB.red(colourY0), ARGB.red(colourY1));
        int redZ = (int) Mth.lerp(player.getZ() % 1, ARGB.red(colourZ0), ARGB.red(colourZ1));

        int greenX = (int) Mth.lerp(player.getX() % 1, ARGB.green(colourX0), ARGB.green(colourX1));
        int greenY = (int) Mth.lerp(player.getY() % 1, ARGB.green(colourY0), ARGB.green(colourY1));
        int greenZ = (int) Mth.lerp(player.getZ() % 1, ARGB.green(colourZ0), ARGB.green(colourZ1));

        int blueX = (int) Mth.lerp(player.getX() % 1, ARGB.blue(colourX0), ARGB.blue(colourX1));
        int blueY = (int) Mth.lerp(player.getY() % 1, ARGB.blue(colourY0), ARGB.blue(colourY1));
        int blueZ = (int) Mth.lerp(player.getZ() % 1, ARGB.blue(colourZ0), ARGB.blue(colourZ1));

        int red = (redX + redY + redZ) / 3;
        int green = (greenX + greenY + greenZ) / 3;
        int blue = (blueX + blueY + blueZ) / 3;

        return new int[] {
            red,
            green,
            blue,
        };
    }

    public static int getColourDifference(int[] colour1, int[] colour2) {
        return Math.abs(colour1[0] - colour2[0]) +
               Math.abs(colour1[1] - colour2[1]) +
               Math.abs(colour1[2] - colour2[2]);
    }

    public static void computeShroudedBlendLerp(ClientLevel level, Player player, double partialTicks) {
        String name = level.getBiome(player.blockPosition()).toString();

        partialTicks /= 200;
        if (name.contains("shrouded_forest")) {
            shroudedBlendLerp = (float) Mth.clamp(shroudedBlendLerp + partialTicks, 0, 1);
        } else {
            shroudedBlendLerp = (float) Mth.clamp(shroudedBlendLerp - partialTicks, 0, 1);
        }
    }
}
