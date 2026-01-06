package org.exodusstudio.frostbite.common.entity.custom.misc;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WhirlpoolEntity extends Entity {
    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNER_UUID =
            SynchedEntityData.defineId(WhirlpoolEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    private static final EntityDataAccessor<Float> DATA_CHARGE =
            SynchedEntityData.defineId(WhirlpoolEntity.class, EntityDataSerializers.FLOAT);
    private static final int DPS = 4;
    private static final int LIFETIME = 200;

    public WhirlpoolEntity(EntityType<? extends Entity> ignored, Level p_19705_) {
        super(EntityRegistry.WHIRLPOOL.get(), p_19705_);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_OWNER_UUID, Optional.empty());
        builder.define(DATA_CHARGE, 10f);
    }

    @Override
    public void tick() {
        if (firstTick && level().isClientSide()) {
            this.level().addParticle(ParticleRegistry.WHIRLPOOL_PARTICLE.get(), this.getX(), this.getY() + 0.2, this.getZ(), 0, 0, 0);
        }

        if (this.tickCount % 5 == 0) {
            if (this.level() instanceof ServerLevel serverLevel) {
                List<LivingEntity> list1 = serverLevel.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
                if (!list1.isEmpty()) {
                    for (LivingEntity livingentity : list1) {
                        if (livingentity == getOwner()) continue;
                        livingentity.hurtServer(serverLevel, this.damageSources().fellOutOfWorld(), DPS / 4f);
                        livingentity.addDeltaMovement(position().subtract(livingentity.position()).normalize().scale(0.5f));
                        addCharge(DPS / 4f);
                    }
                }
            }
        }

        if (getOwner() != null) {
            getOwner().setDeltaMovement(0, 0, 0);
            getOwner().setPos(this.getX(), this.getY(), this.getZ());
        }

        if (getCharge() >= 40f || this.tickCount >= LIFETIME) {
            release();
            this.discard();
        }

        super.tick();
    }

    public void release() {
        if (this.level() instanceof ServerLevel serverLevel) {
            List<LivingEntity> list1 = serverLevel.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(5));
            if (!list1.isEmpty()) {
                for (LivingEntity livingentity : list1) {
                    if (livingentity == getOwner()) continue;
                    livingentity.hurtServer(serverLevel, this.damageSources().fellOutOfWorld(), getCharge() / 2f);
                    livingentity.addDeltaMovement(livingentity.position().subtract(position()).normalize().scale(2f));
                }
            }
        }
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        setCharge(valueInput.getFloatOr("Charge", 0f));
        if (valueInput.getString("OwnerUUID").isPresent()) setOwnerUUID(UUID.fromString(valueInput.getString("OwnerUUID").get()));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        valueOutput.putFloat("Charge", getCharge());
        if (getOwnerUUID() != null) valueOutput.putString("OwnerUUID", getOwnerUUID().toString());
    }

    public @Nullable LivingEntity getOwner() {
        if (this.level() instanceof ServerLevel serverLevel && this.getOwnerUUID() != null) {
            return (LivingEntity) serverLevel.getEntity(this.getOwnerUUID());
        }
        return null;
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        if (uuid == null) {
            this.getEntityData().set(DATA_OWNER_UUID, Optional.empty());
            return;
        }
        this.getEntityData().set(DATA_OWNER_UUID, Optional.of(EntityReference.of(uuid)));
    }

    public @Nullable UUID getOwnerUUID() {
        if (this.entityData.get(DATA_OWNER_UUID).isEmpty()) return null;
        return this.entityData.get(DATA_OWNER_UUID).get().getUUID();
    }

    public float getCharge() {
        return this.entityData.get(DATA_CHARGE);
    }

    public void addCharge(float charge) {
        this.entityData.set(DATA_CHARGE, getCharge() + charge);
    }

    public void setCharge(float charge) {
        this.entityData.set(DATA_CHARGE, charge);
    }
}
