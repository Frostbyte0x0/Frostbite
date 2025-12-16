package org.exodusstudio.frostbite.common.entity.custom.bard;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import org.exodusstudio.frostbite.common.entity.custom.monk.MonkAI;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.MemoryModuleTypeRegistry;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.exodusstudio.frostbite.common.entity.custom.bard.BardEntity.ATTACK_COOLDOWN;


public class BardAI {
    private static final List<SensorType<? extends Sensor<? super BardEntity>>> SENSOR_TYPES =
            List.of(SensorType.NEAREST_PLAYERS, EntityRegistry.BARD_SENSOR.get());
    private static final List<MemoryModuleType<?>> MEMORY_TYPES = List.of(
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get());

    protected static Brain<?> makeBrain(BardEntity ignored, Dynamic<?> ops) {
        Brain.Provider<BardEntity> provider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<BardEntity> brain = provider.makeBrain(ops);
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        setupCooldowns(brain);
        return brain;
    }

    private static void initCoreActivity(Brain<BardEntity> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim<Mob>(0.8F),
                new MoveToTargetSink(500, 700)));
    }

    private static void initFightActivity(Brain<BardEntity> brain) {
        brain.addActivityWithConditions(Activity.FIGHT, ImmutableList.of(Pair.of(0, new Attack())),
                Set.of(Pair.of(MemoryModuleType.NEAREST_ATTACKABLE, MemoryStatus.VALUE_PRESENT)));
    }

    private static void initIdleActivity(Brain<BardEntity> brain) {
        brain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(Pair.of(2, new LookAtTargetSink(45, 90)),
                        Pair.of(4, new RunOne<>(ImmutableList.of(
                                Pair.of(new DoNothing(5, 20), 2))))),
                Set.of());
    }

    public static void updateActivity(BardEntity bard) {
        bard.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }

    public static void setupCooldowns(Brain<BardEntity> brain) {
        brain.setMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), ATTACK_COOLDOWN);
    }

    static class Attack extends Behavior<BardEntity> {
        Attack() {
            super(Map.of(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), MemoryStatus.VALUE_PRESENT), 1);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel serverLevel, BardEntity bard) {
            return bard.getBrain().getMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get()).get() <= 0;
        }

        @Override
        protected boolean canStillUse(ServerLevel serverLevel, BardEntity bard, long l) {
            return bard.isPlaying();
        }

        @Override
        protected void start(ServerLevel serverLevel, BardEntity bard, long l) {
            bard.setPlaying(true);
        }

        @Override
        protected void stop(ServerLevel serverLevel, BardEntity monk, long l) {
            monk.getBrain().setMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(),
                    monk.getRandom().nextFloat() < 0.5 ? (ATTACK_COOLDOWN) : (ATTACK_COOLDOWN / 2));
        }
    }
}
