package org.exodusstudio.frostbite.common.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.custom.illusory.IllusoryEndermanEntity;
import org.exodusstudio.frostbite.common.entity.custom.illusory.IllusoryZombieEntity;
import org.exodusstudio.frostbite.common.network.PlayerHeartDataHandler;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;

import java.util.function.Supplier;

@EventBusSubscriber(modid = Frostbite.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {
    private static final RandomSource random = RandomSource.create();

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Pre event) {
        //updateTemperature(event.getEntity());
    }

    @SubscribeEvent
    public static void playerHeartEventAdded(MobEffectEvent.Added event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (event.getEffectInstance().is(EffectRegistry.DECAY)) {
                PacketDistributor.sendToPlayer(player, new PlayerHeartDataHandler(true));
            }
        }
    }

    @SubscribeEvent
    public static void playerHeartEventRemoved(MobEffectEvent.Remove event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            if (event.getEffectInstance() != null && event.getEffectInstance().is(EffectRegistry.DECAY)) {
                PacketDistributor.sendToPlayer(player, new PlayerHeartDataHandler(false));
            }
        }
    }



    @SubscribeEvent
    public static void changeTargetEvent(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof IllusoryEndermanEntity || event.getEntity() instanceof IllusoryZombieEntity) {
            if (((Monster) event.getEntity()).getTarget() != null) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void tick(PlayerTickEvent.Pre event) {
        if (event.getEntity().hasEffect(EffectRegistry.PARANOIA)) {
            if (random.nextFloat() < 0.01f) {
                if (!event.getEntity().level().isClientSide) {
                    ServerLevel serverlevel = (ServerLevel) event.getEntity().level();

                    spawnMonsterRandomlyAroundPlayer(() -> new IllusoryZombieEntity(serverlevel), serverlevel, event.getEntity(), 10, 60, 20);
                }
            }
        }
    }

    public static <T extends Monster> void spawnMonsterRandomlyAroundPlayer(Supplier<T> monsterSupplier, ServerLevel serverLevel, Player player, int minDist, int maxDist, int tries) {
        for (int i = 0; i < tries; i++) {
            BlockPos blockpos = player.blockPosition().offset(
                    random.nextIntBetweenInclusive(minDist, maxDist) * plusOrMinus(),
                    random.nextIntBetweenInclusive(0, 4) * plusOrMinus(),
                    random.nextIntBetweenInclusive(minDist, maxDist) * plusOrMinus());

            if (player.level().getBlockState(blockpos).isEmpty()
                    && player.level().getBlockState(blockpos.above()).isEmpty()
                    && !player.level().getBlockState(blockpos.below()).isEmpty()) {
                T monster = monsterSupplier.get();
                monster.moveTo(blockpos, 0.0F, 0.0F);
                monster.finalizeSpawn(serverLevel, player.level().getCurrentDifficultyAt(blockpos), EntitySpawnReason.EVENT, null);
                monster.setTarget(player);

                serverLevel.addFreshEntityWithPassengers(monster);
                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, blockpos, GameEvent.Context.of(player));
            }
        }
    }

    public static int plusOrMinus() {
        if (random.nextBoolean()) {
            return 1;
        }
        return -1;
    }
}
