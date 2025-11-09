package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class IceSpikeEntity extends EvokerFangs {
    private static final EntityDataAccessor<String> DATA_TRACKED_UUID =
            SynchedEntityData.defineId(IceSpikeEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Boolean> DATA_RISING =
            SynchedEntityData.defineId(IceSpikeEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_DELAY_TICKS =
            SynchedEntityData.defineId(IceSpikeEntity.class, EntityDataSerializers.INT);

    public IceSpikeEntity(EntityType<? extends IceSpikeEntity> p_36923_, Level p_36924_) {
        super(p_36923_, p_36924_);
    }

    public IceSpikeEntity(
            Level level,
            double x,
            double y,
            double z,
            float yRot,
            float xRot,
            int warmupDelay,
            LivingEntity owner,
            Boolean isRising
    ) {
        this(EntityRegistry.ICE_SPIKE.get(), level);
        this.setDelayTicks(warmupDelay);
        this.setOwner(owner);
        this.setYRot(yRot);
        this.setXRot(xRot);
        this.setPos(x, y, z);
        this.setRising(isRising);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_TRACKED_UUID, "");
        builder.define(DATA_RISING, true);
        builder.define(DATA_DELAY_TICKS, 0);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        if (input.getString("tracked_uuid").isPresent()) {
            setTrackedUUID(input.getString("tracked_uuid").get());
        }
        setRising(input.getBooleanOr("rising", true));
        setDelayTicks(input.getIntOr("delay_ticks", 0));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        valueOutput.putString("tracked_uuid", getTrackedUUID());
        valueOutput.putBoolean("rising", isRising());
        valueOutput.putInt("delay_ticks", getDelayTicks());
    }

    @Override
    public void handleEntityEvent(byte p_36935_) {
        if (p_36935_ == 4) {
            this.clientSideAttackStarted = true;
            if (!isSilent()) {
                level().playLocalSound(getX(), getY(), getZ(), SoundEvents.AMETHYST_BLOCK_BREAK, getSoundSource(), 1.0F, random.nextFloat() * 0.2F + 0.85F, false);
            }
        }
    }

    @Override
    public void tick() {
        move(MoverType.SELF, getDeltaMovement());

        if (isRising()) {
            super.tick();
        } else {
            if (getDelayTicks() == 0 && !isRising()) {
                setDeltaMovement(getLookAngle().scale(1.5));
                level().playSound(this, getX(), getY(), getZ(), SoundEvents.TRIDENT_THROW.value(), getSoundSource(), 1.0F, random.nextFloat() * 0.2F + 0.85F);
            }
            setDelayTicks(getDelayTicks() - 1);

            if (onGround() || this.horizontalCollision || getY() > level().getMaxY() + 40) {
                level().playSound(this, getX(), getY(), getZ(), SoundEvents.GLASS_BREAK, getSoundSource(), 1.0F, random.nextFloat() * 0.2F + 0.85F);
                discard();
            }

            setYRot(getYRotD().orElse(getYRot()));
            setXRot(getXRotD().orElse(getXRot()));

            for (LivingEntity livingentity : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(0.2, 0.0F, 0.2))) {
                dealDamageTo(livingentity);
            }
        }
    }

    private void dealDamageTo(LivingEntity target) {
        LivingEntity livingentity = this.getOwner();
        if (target.isAlive() && target != livingentity) {
            if (livingentity == null) {
                if (level() instanceof ServerLevel serverLevel) {
                    target.hurtServer(serverLevel, this.damageSources().magic(), 4);
                }
            } else {
                if (livingentity.isAlliedTo(target)) {
                    return;
                }

                DamageSource damagesource = this.damageSources().indirectMagic(this, livingentity);
                Level var5 = this.level();
                if (var5 instanceof ServerLevel serverlevel) {
                    if (target.hurtServer(serverlevel, damagesource, 6.0F)) {
                        EnchantmentHelper.doPostAttackEffects(serverlevel, target, damagesource);
                    }
                }
            }
        }

    }

    public Optional<Float> getXRotD() {
        if (getTrackedEntity(level()) != null && !isRising() && getDelayTicks() >= 0) {
            double d0 = getTrackedEntity(level()).getX() - getX();
            double d1 = getTrackedEntity(level()).getEyeY() - getY() - 0.25;
            double d2 = getTrackedEntity(level()).getZ() - getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            return !(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d3) > (double)1.0E-5F) ? Optional.empty() :
                    Optional.of((float) -(Mth.atan2(d1, d3) * 180f / Math.PI));
        }

        return Optional.empty();
    }

    public Optional<Float> getYRotD() {
        if (getTrackedEntity(level()) != null && !isRising() && getDelayTicks() >= 0) {
            double d0 = getTrackedEntity(level()).getX() - getX();
            double d1 = getTrackedEntity(level()).getZ() - getZ();
            return !(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d0) > (double)1.0E-5F) ? Optional.empty() :
                    Optional.of((float) (Mth.atan2(d1, d0) * 180f / Math.PI - 90f));
        }

        return Optional.empty();
    }

    public LivingEntity getTrackedEntity(Level level) {
        if (entityData.get(DATA_TRACKED_UUID).isEmpty()) {
            return null;
        }
        return (LivingEntity) level.getEntity(UUID.fromString(entityData.get(DATA_TRACKED_UUID)));
    }

    public String getTrackedUUID() {
        return entityData.get(DATA_TRACKED_UUID);
    }

    public void setTrackedEntity(@Nullable LivingEntity entity) {
        if (entity == null) {
            entityData.set(DATA_TRACKED_UUID, "");
            return;
        }
        entityData.set(DATA_TRACKED_UUID, entity.getUUID().toString());
    }

    public void setTrackedUUID(String uuid) {
        entityData.set(DATA_TRACKED_UUID, uuid);
    }

    public Boolean isRising() {
        return entityData.get(DATA_RISING);
    }

    public void setRising(Boolean rising) {
        entityData.set(DATA_RISING, rising);
    }

    public Integer getDelayTicks() {
        return entityData.get(DATA_DELAY_TICKS);
    }

    public void setDelayTicks(int delay) {
        entityData.set(DATA_DELAY_TICKS, delay);
    }
}
