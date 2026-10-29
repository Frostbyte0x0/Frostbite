package org.exodusstudio.frostbite.common.entity.custom.helper;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

public class StateBossMonster<E extends StateMonsterEntity> extends StateMonsterEntity {
    private final ServerBossEvent bossEvent;
    protected final Consumer<E> updateBrain;
    protected final Map<MemoryModuleType<Integer>, Integer> cooldowns;

    protected StateBossMonster(EntityType<? extends Monster> type, Level level, int blendTicks, ServerBossEvent bossEvent, Consumer<E> updateBrain, Map<MemoryModuleType<Integer>, Integer> cooldowns) {
        super(type, level, blendTicks);
        this.bossEvent = bossEvent;
        this.updateBrain = updateBrain;
        this.cooldowns = cooldowns;
    }

    @Override
    protected void customServerAiStep(ServerLevel serverLevel) {
        super.customServerAiStep(serverLevel);
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        updateBrain.accept(getEntity());
        ((Brain<E>) getBrain()).tick(serverLevel, getEntity());
    }

    @Nullable
    public LivingEntity getAttackableFromBrain() {
        return this.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE).orElse(null);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer serverPlayer) {
        super.startSeenByPlayer(serverPlayer);
        this.bossEvent.addPlayer(serverPlayer);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossEvent.removePlayer(serverPlayer);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_STATE.equals(accessor)) {
            if (isIdle()) {
                if (getBrain().getMemory(MemoryModuleType.LOOK_TARGET).isPresent()) {
                    getBrain().setMemory(MemoryModuleType.WALK_TARGET,
                            new WalkTarget(getBrain().getMemory(MemoryModuleType.LOOK_TARGET).get(), 1, 2));
                }
            }
        }

        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public void tick() {
        super.tick();

        cooldowns.forEach((memoryModuleType, cooldown) -> {
            if (!isDeadOrDying() && getBrain().getMemory(memoryModuleType).isPresent()) {
                this.getBrain().setMemory(memoryModuleType, getBrain().getMemory(memoryModuleType).get() - 1);
                if (getBrain().getMemory(memoryModuleType).get() < -10) {
                    getBrain().setMemory(memoryModuleType, cooldown);
                }
            }
        });
    }

    protected E getEntity() {
        try {
            return (E) this;
        } catch (ClassCastException e) {
            throw new RuntimeException("Failed to cast StateBossMonster to generic type E", e);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
}
