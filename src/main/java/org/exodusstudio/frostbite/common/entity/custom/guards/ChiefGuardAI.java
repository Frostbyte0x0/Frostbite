package org.exodusstudio.frostbite.common.entity.custom.guards;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
import net.minecraft.world.entity.ai.behavior.Swim;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import org.exodusstudio.frostbite.common.entity.custom.helper.StateBossAI;
import org.exodusstudio.frostbite.common.registry.MemoryModuleTypeRegistry;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.exodusstudio.frostbite.common.entity.custom.guards.ChiefGuardEntity.*;

public class ChiefGuardAI extends StateBossAI<ChiefGuardEntity>  {
    private static final List<MemoryModuleType<?>> MEMORY_TYPES = List.of(
            MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(),
            MemoryModuleTypeRegistry.DASH_COOLDOWN.get(),
            MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get());

    public List<MemoryModuleType<?>> getMemoryTypes() {
        return Stream.concat(super.getMemoryTypes().stream(), MEMORY_TYPES.stream()).toList();
    }

    protected void initCoreActivity(Brain<ChiefGuardEntity> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(
                new Swim<Mob>(0.8F),
                SetEntityLookTargetSometimes.create(EntityType.PLAYER, 10, UniformInt.of(10, 30)),
                new MoveToTargetSink(500, 700)));
    }

    protected void initFightActivity(Brain<ChiefGuardEntity> brain) {
        brain.addActivityWithConditions(Activity.FIGHT, ImmutableList.of(
                Pair.of(1, new Dash()),
                Pair.of(1, new Attack()),
                Pair.of(1, new Guard()),
                Pair.of(1, new Summon())),
                Set.of(Pair.of(MemoryModuleType.NEAREST_ATTACKABLE, MemoryStatus.VALUE_PRESENT)));
    }

    public static void updateActivity(ChiefGuardEntity chief_guard) {
        chief_guard.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }

    protected void setupCooldowns(Brain<ChiefGuardEntity> brain) {
        brain.setMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), ATTACK_COOLDOWN);
        brain.setMemory(MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get(), SUMMON_COOLDOWN);
        brain.setMemory(MemoryModuleTypeRegistry.DASH_COOLDOWN.get(), DASH_COOLDOWN);
    }

    static class Guard extends Behavior<ChiefGuardEntity> {
        Guard() {
            super(Map.of(), 50);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, ChiefGuardEntity owner) {
            return owner.isIdle() && owner.getAttackableFromBrain() != null
                    && owner.distanceToSqr(owner.getAttackableFromBrain()) <= 6
                    && owner.getRandom().nextFloat() < 0.025f;
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
        protected void tick(ServerLevel level, ChiefGuardEntity chief_guard, long gameTime) {
            super.tick(level, chief_guard, gameTime);
            if (chief_guard.getAttackableFromBrain() != null) {
                chief_guard.getLookControl().setLookAt(chief_guard.getAttackableFromBrain(), 30, 30);
            }
        }

        @Override
        protected void stop(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            if (!chief_guard.isParrying()) {
                chief_guard.setAttacking();
            }
        }
    }

    static class Dash extends Behavior<ChiefGuardEntity> {
        Dash() {
            super(Map.of(MemoryModuleTypeRegistry.DASH_COOLDOWN.get(), MemoryStatus.VALUE_PRESENT), 50);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, ChiefGuardEntity owner) {
            return owner.getAttackableFromBrain() != null
                    && owner.isIdle()
                    && owner.getBrain().getMemory(MemoryModuleTypeRegistry.DASH_COOLDOWN.get()).get() <= 0
                    && owner.distanceToSqr(owner.getAttackableFromBrain()) > 25;
        }

        @Override
        protected void tick(ServerLevel level, ChiefGuardEntity owner, long gameTime) {
            super.tick(level, owner, gameTime);
//            float angle = (float) (Math.atan2(dir.z, dir.x) * (180 / Math.PI));
//            owner.setYRot(angle);
            if (owner.getAttackableFromBrain() != null) {
                owner.getLookControl().setLookAt(owner.getAttackableFromBrain(), 30, 30);
            }
        }

        @Override
        protected boolean canStillUse(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            return !chief_guard.onGround() || chief_guard.getTicksSinceLastChange() < 10;
        }

        @Override
        protected void start(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            chief_guard.setDashing();
            chief_guard.dash();
        }

        @Override
        protected void stop(ServerLevel serverLevel, ChiefGuardEntity chief_guard, long l) {
            chief_guard.setAttacking();
            chief_guard.getBrain().setMemory(MemoryModuleTypeRegistry.DASH_COOLDOWN.get(), DASH_COOLDOWN);
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
            chief_guard.getBrain().setMemory(MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), ATTACK_COOLDOWN);
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