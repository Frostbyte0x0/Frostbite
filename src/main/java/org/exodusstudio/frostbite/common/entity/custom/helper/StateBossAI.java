package org.exodusstudio.frostbite.common.entity.custom.helper;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

import java.util.List;

public class StateBossAI<E extends StateBossMonster<E>> {
    private static final List<SensorType<? extends Sensor<? super StateBossMonster<?>>>> SENSOR_TYPES =
            List.of(SensorType.NEAREST_PLAYERS, EntityRegistry.TARGET_ENTITY_SENSOR.get());
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
            MemoryModuleType.NEAREST_ATTACKABLE);

    public Brain<?> makeBrain(E ignored, Dynamic<?> ops) {
        Brain.Provider<E> provider = Brain.provider(getMemoryTypes(), getSensorTypes());
        Brain<E> brain = provider.makeBrain(ops);
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        setupCooldowns(brain);
        return brain;
    }

    protected List<MemoryModuleType<?>> getMemoryTypes() {
        return MEMORY_TYPES;
    }

    protected List<SensorType<? extends Sensor<? super StateBossMonster<?>>>> getSensorTypes() {
        return SENSOR_TYPES;
    }

    protected void initCoreActivity(Brain<E> brain) {
    }

    protected void initFightActivity(Brain<E> brain) {
    }

    protected void initIdleActivity(Brain<E> brain) {
        brain.addActivityWithConditions(Activity.IDLE,
                ImmutableList.of(
                        Pair.of(4, new RunOne<>(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), ImmutableList.of(
                                Pair.of(RandomStroll.stroll(1), 1),
                                Pair.of(new DoNothing(50, 100), 1),
                                Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), 2))))),
                ImmutableSet.of()
        );
    }

    protected void setupCooldowns(Brain<E> brain) {
    }
}
