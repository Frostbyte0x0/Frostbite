package org.exodusstudio.frostbite.common.entity.custom.guards;

import com.mojang.serialization.Dynamic;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.helper.StateBossMonster;
import org.exodusstudio.frostbite.common.entity.goals.GuardBodyRotationControl;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.MemoryModuleTypeRegistry;
import org.exodusstudio.frostbite.common.util.TargetingEntity;
import org.exodusstudio.frostbite.common.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ChiefGuardEntity extends StateBossMonster<ChiefGuardEntity> implements TargetingEntity {
    private static final EntityDataAccessor<Boolean> DATA_FREEZE_BODY_ROT =
            SynchedEntityData.defineId(ChiefGuardEntity.class, EntityDataSerializers.BOOLEAN);
    private static final Component NAME_COMPONENT = Component.translatable("entity.chief_guard.boss_bar");
    private final ServerBossEvent bossEvent = (ServerBossEvent)
            new ServerBossEvent(NAME_COMPONENT, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(false);
    public static final int ATTACK_COOLDOWN = 60;
    public static final int SUMMON_COOLDOWN = 300;
    public static final int DASH_COOLDOWN = 60;
    public static final int BLEND_TICKS = 10;
    public static final int ATTACK_ANIM_TIME = 40;
    public static final int PARRY_ANIM_TIME = 20;
    public static final int SUMMON_ANIM_TIME = 40;
    private static final Map<MemoryModuleType<Integer>, Integer> cooldowns = Map.of(
            MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), ATTACK_COOLDOWN,
            MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get(), SUMMON_COOLDOWN,
            MemoryModuleTypeRegistry.DASH_COOLDOWN.get(), DASH_COOLDOWN
    );

    public ChiefGuardEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.CHIEF_GUARD.get(), level, BLEND_TICKS,
                (ServerBossEvent) new ServerBossEvent(NAME_COMPONENT, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(false),
                ChiefGuardAI::updateActivity,
                cooldowns);
        setIdle();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 400)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FREEZE_BODY_ROT, false);
    }

    @Override
    protected @NotNull Brain<?> makeBrain(Dynamic<?> dynamic) {
        return (new ChiefGuardAI()).makeBrain(this, dynamic);
    }

    @Override
    public void tick() {
        super.tick();

        if (getAttackableFromBrain() != null && level() instanceof ServerLevel serverLevel) {
            float t = currentAnimationState.getTimeInMillis(tickCount) / 50f;

            if (isAttacking() && (t == 18 || t == 30 || t == 42)) {
                doAttack(serverLevel);
            }

            if (isSummoning() && t == 12) {
                wakeUpNearbyGuards(serverLevel);
            }

            if (isParrying() && t == 14) {
                doAttack(serverLevel);
            }
        }

        if (isParrying() && getTicksSinceLastChange() > PARRY_ANIM_TIME) {
            setIdle();
        }

        if (level() instanceof ServerLevel) {
            boolean f = brain.getActiveNonCoreActivity().orElse(Activity.IDLE).equals(Activity.FIGHT);
            setFreezeRotation(f);
        }

        if (isAttacking()) {
            if (getTicksSinceLastChange() > ATTACK_ANIM_TIME) {
                setIdle();
            }
        }

        if (getAttackableFromBrain() != null) {
            getLookControl().setLookAt(getAttackableFromBrain(), 30, 30);
        }

        if (tickCount % 20 == 0) {
            if (!brain.getActiveNonCoreActivity().orElse(Activity.IDLE).equals(Activity.IDLE)) {
                boolean match = brain.getRunningBehaviors().stream().anyMatch(b ->
                        (b instanceof ChiefGuardAI.Dash) || (b instanceof ChiefGuardAI.Guard) ||
                                (b instanceof ChiefGuardAI.Attack) || (b instanceof ChiefGuardAI.Summon));
                getNavigation().setSpeedModifier((!match) ? 1.75 : 0.75);
            } else {
                getNavigation().setSpeedModifier(0.75);
            }
        }

    }

    public void doAttack(ServerLevel serverLevel) {
        serverLevel.getEntitiesOfClass(LivingEntity.class, getAttackAABB())
                .forEach(entity -> {
                    if (entity != this) entity.hurtServer(serverLevel, damageSources().mobAttack(this), 16);
                });
    }

    public AABB getAttackAABB() {
        return Util.squareAABB(position(), 2.5)
                .move(getViewVector(0).normalize().scale(2).add(0, 1.5, 0));
    }

    public void dash() {
        LivingEntity target = getAttackableFromBrain();
        if (target != null) {
            Vec3 dir = target.position().subtract(position());
            this.addDeltaMovement(dir.normalize().add(0, 0.25, 0).multiply(1.75, 1.25, 1.75));
        }
    }

    public void wakeUpNearbyGuards(ServerLevel serverLevel) {
        serverLevel.getEntitiesOfClass(GuardEntity.class, Util.squareAABB(position(), 25))
                .forEach(g -> {
                    if (!g.isAwake() && random.nextFloat() < 0.4) {
                        g.setWakingUp();
                        g.setTarget(getAttackableFromBrain());
                    }
                });
    }

    @Override
    public float getSecondsToDisableBlocking() {
        return 5;
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity entity) {
        return entity.distanceToSqr(this) < 9;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity attacker) {
            if (canTargetEntity(attacker)) {
                this.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, attacker);
            }
        }

        if (isGuarding() && source.getDirectEntity() instanceof LivingEntity) {
            Vec3 dir = source.getDirectEntity().position().subtract(position());
            double angle = Math.atan2(dir.z, dir.x) * (180 / Math.PI);
            double diff = (((angle + 360) % 360) - ((yBodyRot + 360) % 360) + 360) % 360;
            if (diff <= 180) {
                setParrying();
                getBrain().getRunningBehaviors().forEach(behavior -> {
                    if (behavior instanceof ChiefGuardAI.Guard) {
                        ((ChiefGuardAI.Guard) behavior).doStop(level, this, tickCount);
                    }
                });
                return false;
            } else {
                amount *= 2;
            }
        }

        return super.hurtServer(level, source, amount);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new GuardBodyRotationControl(this);
    }

    public void setAttacking() {
        this.entityData.set(DATA_LAST_STATE, getCurrentState());
        this.entityData.set(DATA_STATE, "attacking");
    }

    public boolean isAttacking() {
        return getCurrentState().equals("attacking");
    }

    public void setGuarding() {
        this.entityData.set(DATA_LAST_STATE, getCurrentState());
        this.entityData.set(DATA_STATE, "guarding");
    }

    public boolean isGuarding() {
        return getCurrentState().equals("guarding");
    }

    public void setDashing() {
        this.entityData.set(DATA_LAST_STATE, getCurrentState());
        this.entityData.set(DATA_STATE, "dashing");
    }

    public void setParrying() {
        this.entityData.set(DATA_LAST_STATE, getCurrentState());
        this.entityData.set(DATA_STATE, "parrying");
    }

    public boolean isParrying() {
        return getCurrentState().equals("parrying");
    }

    public void setSummoning() {
        this.entityData.set(DATA_LAST_STATE, getCurrentState());
        this.entityData.set(DATA_STATE, "summoning");
    }

    public boolean isSummoning() {
        return getCurrentState().equals("summoning");
    }

    public boolean shouldFreezeRotation() {
        return this.getEntityData().get(DATA_FREEZE_BODY_ROT);
    }

    public void setFreezeRotation(boolean f) {
        this.entityData.set(DATA_FREEZE_BODY_ROT, f);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
