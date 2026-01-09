package org.exodusstudio.frostbite.common.entity.custom.shaman;

import com.mojang.serialization.Dynamic;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.IcedCreeperEntity;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.IcedSkeletonEntity;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.IcedZombieEntity;
import org.exodusstudio.frostbite.common.entity.custom.helper.StateBossMonster;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealHammerEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealHandsEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealSwordEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealWeaponEntity;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.MemoryModuleTypeRegistry;
import org.exodusstudio.frostbite.common.util.TargetingEntity;
import org.exodusstudio.frostbite.common.util.Util;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ShamanEntity extends StateBossMonster<ShamanEntity> implements TargetingEntity {
    private static final EntityDataAccessor<Boolean> DATA_SHOWING_SHIELD =
            SynchedEntityData.defineId(ShamanEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_SHIELD_HEALTH =
            SynchedEntityData.defineId(ShamanEntity.class, EntityDataSerializers.FLOAT);
    private static final Component SHAMAN_NAME_COMPONENT = Component.translatable("entity.shaman.boss_bar");
    public static final int ETHEREAL_COOLDOWN = 90;
    public static final int WHIRLPOOL_COOLDOWN = 90;
    public static final int SUMMON_COOLDOWN = 90;
    public static final int CURSE_COOLDOWN = 90;
    public static final int ETHEREAL_DURATION = 30;
    public static final int WHIRLPOOL_DURATION = 210;
    public static final int SUMMON_DURATION = 30;
    public static final int CURSE_DURATION = 30;
    public static final int WEAKENED_DURATION = 100;
    public static final int BLEND_TICKS = 10;
    private static final int SHIELD_DISTANCE = 8;
    private static final Map<MemoryModuleType<Integer>, Integer> cooldowns = Map.of(
            MemoryModuleTypeRegistry.ATTACK_COOLDOWN.get(), ETHEREAL_COOLDOWN,
            MemoryModuleTypeRegistry.WHIRLPOOL_COOLDOWN.get(), WHIRLPOOL_COOLDOWN,
            MemoryModuleTypeRegistry.CURSE_COOLDOWN.get(), CURSE_COOLDOWN,
            MemoryModuleTypeRegistry.SUMMON_COOLDOWN.get(), SUMMON_COOLDOWN
    );

    public ShamanEntity(EntityType<? extends ShamanEntity> ignored, Level level) {
        super(EntityRegistry.SHAMAN.get(), level, BLEND_TICKS,
                (ServerBossEvent) new ServerBossEvent(SHAMAN_NAME_COMPONENT, BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(false),
                ShamanAI::updateActivity,
                cooldowns);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 300)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_SHOWING_SHIELD, false);
        builder.define(DATA_SHIELD_HEALTH, 100f);
    }

    @Override
    protected @NotNull Brain<?> makeBrain(Dynamic<?> dynamic) {
        return (new ShamanAI()).makeBrain(this, dynamic);
    }

    public Entity getInstance() {
        return this;
    }

    @Override
    public void tick() {
        super.tick();
        
        if (tickCount % 20 == 0) {
            setShowingShield(!level().getEntities(this, Util.squareAABB(position().add(0, 1.5, 0), SHIELD_DISTANCE),
                    entity -> entity instanceof LivingEntity && canTargetEntity(entity)).isEmpty());
        }

        if (getAttackableFromBrain() != null && level() instanceof ServerLevel serverLevel) {
            float t = currentAnimationState.getTimeInMillis(tickCount) / 50f;

            if (isEtherealing() && t == 10) {
                etherealAttack(getAttackableFromBrain(), serverLevel);
            } else if (isSummoning() && t == 10) {
                summonAttack();
            } else if (isWhirlpooling() && t == 10) {
                whirlpoolAttack(getAttackableFromBrain(), serverLevel);
            } else if (isCursing() && (t == 10 || t == 15 || t == 20)) {
                curseAttack(getAttackableFromBrain(), serverLevel);
            } else if (isWeakened() && getTicksSinceLastChange() >= WEAKENED_DURATION) {
                setIdle();
                setShieldHealth(80);
            }
        }
    }

    public void etherealAttack(LivingEntity target, ServerLevel serverLevel) {
        Vec3 v = target.position().subtract(position()).normalize();
        EtherealWeaponEntity weapon;

        if (getRandom().nextFloat() < 0.333) {
            weapon = new EtherealHammerEntity(null, serverLevel);
        } else if (getRandom().nextFloat() < 0.5) {
            weapon = new EtherealHandsEntity(null, serverLevel);
        } else {
            weapon = new EtherealSwordEntity(null, serverLevel);
        }

        weapon.setPos(target.position().add(0, 1, 0));
        float[] angles = Util.getXYRot(v);
        weapon.setXRot(angles[0]);
        weapon.setYRot(angles[1]);
        serverLevel.addFreshEntity(weapon);
    }

    public void summonAttack() {
        if (!(level() instanceof ServerLevel serverLevel)) return;
        for (int i = 0; i < 8; i++) {
            Util.spawnMonsterRandomlyAroundEntity(() -> new IcedCreeperEntity(null, serverLevel),
                    serverLevel, this, 5, 10);
            Util.spawnMonsterRandomlyAroundEntity(() -> new IcedSkeletonEntity(null, serverLevel),
                    serverLevel, this, 5, 10);
            Util.spawnMonsterRandomlyAroundEntity(() -> new IcedZombieEntity(null, serverLevel),
                    serverLevel, this, 5, 10);
        }
    }

    public void whirlpoolAttack(LivingEntity target, ServerLevel serverLevel) {
        WhirlpoolEntity whirlpool = new WhirlpoolEntity(null, serverLevel);
        whirlpool.setPos(position());
        whirlpool.setOwnerUUID(getUUID());
        serverLevel.addFreshEntity(whirlpool);
    }

    public void curseAttack(LivingEntity target, ServerLevel serverLevel) {
        CurseBallEntity curse = new CurseBallEntity(null, serverLevel);
        Vec3 v = target.position().subtract(position()).normalize();
        curse.setPos(position().add(v));
        curse.setOwnerUUID(getUUID());
        curse.setLaunchDirection(v);
        serverLevel.addFreshEntity(curse);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (source.getDirectEntity() instanceof Projectile) {
            damage *= 0.2f;
        }

        if (isShowingShield() && isShieldWorking()) {
            damageShield(damage);
            return false;
        }
        if (isWeakened()) {
            damage *= 1.5f;
        }
        return super.hurtServer(level, source, damage);
    }

    public boolean isShowingShield() {
        return this.entityData.get(DATA_SHOWING_SHIELD) && isShieldWorking();
    }

    public void setShowingShield(boolean showingShield) {
        this.entityData.set(DATA_SHOWING_SHIELD, showingShield);
    }

    public float getShieldHealth() {
        return this.entityData.get(DATA_SHIELD_HEALTH);
    }

    public boolean isShieldWorking() {
        return this.entityData.get(DATA_SHIELD_HEALTH) > 0;
    }

    public void setShieldHealth(float shieldHealth) {
        this.entityData.set(DATA_SHIELD_HEALTH, shieldHealth);
    }

    public void damageShield(float damage) {
        this.entityData.set(DATA_SHIELD_HEALTH, getShieldHealth() - damage);
        if (getShieldHealth() < 0) {
            setShieldHealth(0);
            setWeakened();
        }
    }

    public boolean isSummoning() {
        return getCurrentState().equals("summoning");
    }

    public void setSummoning() {
        this.setLastState(getCurrentState());
        this.setCurrentState("summoning");
    }

    public boolean isWhirlpooling() {
        return getCurrentState().equals("whirlpooling");
    }

    public void setWhirlpooling() {
        this.setLastState(getCurrentState());
        this.setCurrentState("whirlpooling");
    }

    public boolean isCursing() {
        return getCurrentState().equals("cursing");
    }

    public void setCursing() {
        this.setLastState(getCurrentState());
        this.setCurrentState("cursing");
    }

    public boolean isWeakened() {
        return getCurrentState().equals("weakened");
    }

    public void setWeakened() {
        this.setLastState(getCurrentState());
        this.setCurrentState("weakened");
    }

    public boolean isEtherealing() {
        return getCurrentState().equals("etherealing");
    }

    public void setEtherealing() {
        this.setLastState(getCurrentState());
        this.setCurrentState("etherealing");
    }

    public int getTicksSinceLastChange() {
        return this.entityData.get(DATA_TICKS_SINCE_LAST_CHANGE);
    }

    public void setTicksSinceLastChange(int ticks) {
        this.entityData.set(DATA_TICKS_SINCE_LAST_CHANGE, ticks);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
