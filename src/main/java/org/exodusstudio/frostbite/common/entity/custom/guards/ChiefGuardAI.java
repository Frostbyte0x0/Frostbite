package org.exodusstudio.frostbite.common.entity.custom.guards;

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
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.MemoryModuleTypeRegistry;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.exodusstudio.frostbite.common.entity.custom.guards.ChiefGuardEntity.ATTACK_COOLDOWN;
import static org.exodusstudio.frostbite.common.entity.custom.guards.ChiefGuardEntity.SUMMON_COOLDOWN;

public class ChiefGuardAI {
    private static final List<SensorType<? extends Sensor<? super ChiefGuardEntity>>> SENSOR_TYPES =
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
            MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get());

    protected static Brain<?> makeBrain(ChiefGuardEntity ignored, Dynamic<?> ops) {
        Brain.Provider<ChiefGuardEntity> provider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<ChiefGuardEntity> brain = provider.makeBrain(ops);
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        setupCooldowns(brain);
        return brain;
    }

    private static void initCoreActivity(Brain<ChiefGuardEntity> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim<Mob>(0.8F),
                new MoveToTargetSink(500, 700)));
    }

    private static void initFightActivity(Brain<ChiefGuardEntity> brain) {
        brain.addActivityWithConditions(Activity.FIGHT, ImmutableList.of(
                Pair.of(0, new Guard()),
                Pair.of(0, new Dash()),
                Pair.of(0, new Attack()),
                Pair.of(0, new Summon())),
                Set.of(Pair.of(MemoryModuleType.NEAREST_ATTACKABLE, MemoryStatus.VALUE_PRESENT)));
    }

    private static void initIdleActivity(Brain<ChiefGuardEntity> brain) {
        brain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(
                Pair.of(2, new LookAtTargetSink(45, 90)),
                Pair.of(4, new RunOne<>(ImmutableList.of(
                        Pair.of(new DoNothing(5, 20), 2))))),
                Set.of());

//        brain.addActivityWithConditions(Activity.IDLE,
//                ImmutableList.of(
//                    Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
//                    Pair.of(0, new AnimalMakeLove(EntityType.FROG)),
//                    Pair.of(1, new FollowTemptation((p_218585_) -> 1.25F)),
//                    Pair.of(2, StartAttacking.create((p_375840_, p_375841_) -> canAttack(p_375841_), (p_376533_, p_218605_) -> p_218605_.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))),
//                    Pair.of(3, TryFindLand.create(6, 1.0F)),
//                    Pair.of(4, new RunOne(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), ImmutableList.of(
//                            Pair.of(RandomStroll.stroll(1.0F), 1),
//                            Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 1),
//                            Pair.of(new Croak(), 3),
//                            Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), 2))))),
//                ImmutableSet.of(
//                        Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT),
//                        Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT))
//        );
    }

    public static void updateActivity(ChiefGuardEntity chief_guard) {
        chief_guard.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }

    public static void setupCooldowns(Brain<ChiefGuardEntity> brain) {
        brain.setMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), ATTACK_COOLDOWN);
        brain.setMemory(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get(), SUMMON_COOLDOWN);
    }

    static class WalkTowardsTarget extends Behavior<ChiefGuardEntity> {
        WalkTowardsTarget() {
            super(Map.of(), 100);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, ChiefGuardEntity owner) {
            return owner.isIdle() && owner.getAttackableFromBrain() != null
                    && owner.distanceToSqr(owner.getAttackableFromBrain()) <= 9
                    && owner.getRandom().nextFloat() < 0.3f;
        }

        @Override
        protected boolean canStillUse(ServerLevel level, ChiefGuardEntity entity, long gameTime) {
            return true;
        }

        @Override
        protected void start(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            chief_guard.setGuarding();
        }

        @Override
        protected void stop(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            chief_guard.setIdle();
        }
    }

    static class Guard extends Behavior<ChiefGuardEntity> {
        Guard() {
            super(Map.of(), 100);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, ChiefGuardEntity owner) {
            return owner.isIdle() && owner.getAttackableFromBrain() != null
                    && owner.distanceToSqr(owner.getAttackableFromBrain()) <= 9
                    && owner.getRandom().nextFloat() < 0.1f;
        }

        @Override
        protected boolean canStillUse(ServerLevel level, ChiefGuardEntity entity, long gameTime) {
            return true;
        }

        @Override
        protected void start(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            chief_guard.setGuarding();
        }

        @Override
        protected void stop(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            chief_guard.setIdle();
        }
    }

    static class Dash extends Behavior<ChiefGuardEntity> {
        Dash() {
            super(Map.of(), 100);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, ChiefGuardEntity owner) {
            return owner.getAttackableFromBrain() != null && owner.isIdle()
                    && owner.distanceToSqr(owner.getAttackableFromBrain()) > 25;
        }

        @Override
        protected boolean canStillUse(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            return !chief_guard.onGround() && chief_guard.getTicksSinceLastChange() > 10;
        }

        @Override
        protected void start(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            chief_guard.setDashing();
            chief_guard.dash();
        }

        @Override
        protected void stop(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            chief_guard.setIdle();
        }
    }

    static class Attack extends Behavior<ChiefGuardEntity> {
        Attack() {
            super(Map.of(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), MemoryStatus.VALUE_PRESENT), ChiefGuardEntity.ATTACK_ANIM_TIME);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel serverLevel, ChiefGuardEntity chief_guard) {
            return chief_guard.getBrain().getMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get()).get() <= 0
                    && chief_guard.getAttackableFromBrain() != null
                    && chief_guard.isIdle()
                    && chief_guard.distanceToSqr(chief_guard.getAttackableFromBrain()) <= 9
                    && chief_guard.getRandom().nextFloat() < 0.5f;
        }

        @Override
        protected boolean canStillUse(ServerLevel level, ChiefGuardEntity entity, long gameTime) {
            return true;
        }

        @Override
        protected void start(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            chief_guard.setAttacking();
        }

        @Override
        protected void stop(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            chief_guard.setIdle();
        }
    }

    static class Summon extends Behavior<ChiefGuardEntity> {
        Summon() {
            super(Map.of(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get(), MemoryStatus.VALUE_PRESENT), ChiefGuardEntity.SUMMON_ANIM_TIME);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel serverLevel, ChiefGuardEntity chief_guard) {
            return chief_guard.getBrain().getMemory(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get()).get() <= 0 && chief_guard.isIdle();
        }

        @Override
        protected boolean canStillUse(ServerLevel level, ChiefGuardEntity entity, long gameTime) {
            return true;
        }

        @Override
        protected void start(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            chief_guard.setSummoning();
        }

        @Override
        protected void stop(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            chief_guard.getBrain().setMemory(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get(), SUMMON_COOLDOWN);
            chief_guard.setIdle();
        }
    }
}