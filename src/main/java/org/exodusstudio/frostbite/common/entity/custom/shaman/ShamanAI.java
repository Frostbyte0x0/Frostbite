package org.exodusstudio.frostbite.common.entity.custom.shaman;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.MemoryModuleTypeRegistry;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.exodusstudio.frostbite.common.entity.custom.shaman.ShamanEntity.*;


public class ShamanAI {
    private static final List<SensorType<? extends Sensor<? super ShamanEntity>>> SENSOR_TYPES =
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
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(),
            MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get(),
            MemoryModuleTypeRegistry.CURSE_COOLDOWN.get(),
            MemoryModuleTypeRegistry.WHIRLPOOL_COOLDOWN.get());

    protected static Brain<?> makeBrain(ShamanEntity ignored, Dynamic<?> ops) {
        Brain.Provider<ShamanEntity> provider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<ShamanEntity> brain = provider.makeBrain(ops);
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        setupCooldowns(brain);
        return brain;
    }

    private static void initCoreActivity(Brain<ShamanEntity> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                new Swim<Mob>(0.8F),
                SetEntityLookTargetSometimes.create(EntityType.PLAYER, 10, UniformInt.of(10, 30)),
                new MoveToTargetSink(500, 700)));
    }

    private static void initFightActivity(Brain<ShamanEntity> brain) {
        brain.addActivityWithConditions(Activity.FIGHT, ImmutableList.of(
                Pair.of(0, new Ethereal()),
                Pair.of(0, new Whirlpool()),
                Pair.of(0, new Summon()),
                Pair.of(0, new Curse())),
                Set.of(Pair.of(MemoryModuleType.NEAREST_ATTACKABLE, MemoryStatus.VALUE_PRESENT)));
    }

    private static void initIdleActivity(Brain<ShamanEntity> brain) {
        brain.addActivityWithConditions(Activity.IDLE,
                ImmutableList.of(
                        Pair.of(4, new RunOne<>(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), ImmutableList.of(
                                Pair.of(RandomStroll.stroll(1), 1),
                                Pair.of(new DoNothing(50, 100), 1),
                                Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), 2))))),
                ImmutableSet.of()
        );
    }

    public static void updateActivity(ShamanEntity shaman) {
        shaman.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }

    public static void setupCooldowns(Brain<ShamanEntity> brain) {
        brain.setMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), ETHEREAL_COOLDOWN);
        brain.setMemory(MemoryModuleTypeRegistry.WHIRLPOOL_COOLDOWN.get(), WHIRLPOOL_COOLDOWN);
        brain.setMemory(MemoryModuleTypeRegistry.CURSE_COOLDOWN.get(), CURSE_COOLDOWN);
        brain.setMemory(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get(), SUMMON_COOLDOWN);
    }

    // Summon
    // Ethereal
    // Whirlpool: drain staff, pulls enemies in, aoe damage, slow
    // Curse: long term curse that nibbles health and temp, life+temp drain or massive slowness
    // Cursed shield passive, once near, appears and follows player, have to fight it ranged

    static class Summon extends Behavior<ShamanEntity> {
        Summon() {
            super(Map.of(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get(), MemoryStatus.VALUE_PRESENT), SUMMON_DURATION);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel serverLevel, ShamanEntity shaman) {
            return shaman.getBrain().getMemory(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get()).get() <= 0;
        }

        @Override
        protected boolean canStillUse(ServerLevel serverLevel, ShamanEntity shaman, long l) {
            return true;
        }

        @Override
        protected void start(ServerLevel serverLevel, ShamanEntity shaman, long l) {
            shaman.setSummoning();
        }

        @Override
        protected void stop(ServerLevel serverLevel, ShamanEntity shaman, long l) {
            shaman.getBrain().setMemory(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get(), SUMMON_COOLDOWN);
            shaman.setIdle();
        }
    }

    static class Whirlpool extends Behavior<ShamanEntity> {
        Whirlpool() {
            super(Map.of(MemoryModuleTypeRegistry.WHIRLPOOL_COOLDOWN.get(), MemoryStatus.VALUE_PRESENT), WHIRLPOOL_DURATION);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, ShamanEntity shaman) {
            return shaman.isIdle() && shaman.getAttackableFromBrain() != null
                    && shaman.getBrain().getMemory(MemoryModuleTypeRegistry.WHIRLPOOL_COOLDOWN.get()).get() <= 0
                    && shaman.distanceToSqr(shaman.getAttackableFromBrain()) <= 9
                    && shaman.getRandom().nextFloat() < 0.025f;
        }

        @Override
        protected boolean canStillUse(ServerLevel level, ShamanEntity shaman, long gameTime) {
            return true;
        }

        @Override
        protected void start(ServerLevel serverLevel, ShamanEntity shaman, long l) {
            shaman.setWhirlpooling();
        }

        @Override
        protected void tick(ServerLevel level, ShamanEntity shaman, long gameTime) {
            super.tick(level, shaman, gameTime);
            if (shaman.getAttackableFromBrain() != null) {
                shaman.getLookControl().setLookAt(shaman.getAttackableFromBrain(), 30, 30);
            }
        }

        @Override
        protected void stop(ServerLevel serverLevel, ShamanEntity shaman, long l) {
            shaman.getBrain().setMemory(MemoryModuleTypeRegistry.WHIRLPOOL_COOLDOWN.get(), WHIRLPOOL_COOLDOWN);
            shaman.setIdle();
        }
    }

    static class Curse extends Behavior<ShamanEntity> {
        Curse() {
            super(Map.of(MemoryModuleTypeRegistry.CURSE_COOLDOWN.get(), MemoryStatus.VALUE_PRESENT), CURSE_DURATION);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel serverLevel, ShamanEntity shaman) {
            return shaman.getBrain().getMemory(MemoryModuleTypeRegistry.CURSE_COOLDOWN.get()).get() <= 0 && shaman.isIdle();
        }

        @Override
        protected boolean canStillUse(ServerLevel level, ShamanEntity shaman, long gameTime) {
            return true;
        }

        @Override
        protected void start(ServerLevel serverLevel, ShamanEntity shaman, long l) {
            shaman.setCursing();
        }

        @Override
        protected void tick(ServerLevel level, ShamanEntity shaman, long gameTime) {
            super.tick(level, shaman, gameTime);
            if (shaman.getAttackableFromBrain() != null) {
                shaman.getLookControl().setLookAt(shaman.getAttackableFromBrain(), 30, 30);
            }
        }

        @Override
        protected void stop(ServerLevel serverLevel, ShamanEntity shaman, long l) {
            shaman.getBrain().setMemory(MemoryModuleTypeRegistry.CURSE_COOLDOWN.get(), CURSE_COOLDOWN);
            shaman.setIdle();
        }
    }

    static class Ethereal extends Behavior<ShamanEntity> {
        Ethereal() {
            super(Map.of(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), MemoryStatus.VALUE_PRESENT), ETHEREAL_DURATION);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel serverLevel, ShamanEntity shaman) {
            return shaman.getBrain().getMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get()).get() <= 0 && shaman.isIdle();
        }

        @Override
        protected boolean canStillUse(ServerLevel level, ShamanEntity shaman, long gameTime) {
            return true;
        }

        @Override
        protected void start(ServerLevel serverLevel, ShamanEntity shaman, long l) {
            shaman.setEtherealing();
        }

        @Override
        protected void tick(ServerLevel level, ShamanEntity shaman, long gameTime) {
            super.tick(level, shaman, gameTime);
            if (shaman.getAttackableFromBrain() != null) {
                shaman.getLookControl().setLookAt(shaman.getAttackableFromBrain(), 30, 30);
            }
        }

        @Override
        protected void stop(ServerLevel serverLevel, ShamanEntity shaman, long l) {
            shaman.getBrain().setMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), ETHEREAL_COOLDOWN);
            shaman.setIdle();
        }
    }
}
