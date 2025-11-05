package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.Util;
import org.joml.Vector3f;

import java.util.List;

public class FireSliceEntity extends Projectile {
    private static final EntityDataAccessor<Vector3f> DATA_SHOOT_DIRECTION =
            SynchedEntityData.defineId(FireSliceEntity.class, EntityDataSerializers.VECTOR3);

    public FireSliceEntity(EntityType<? extends Entity> ignored, Level level) {
        super(EntityRegistry.FIRE_SLICE.get(), level);
    }

    public FireSliceEntity(Level level, Vec3 shootDirection) {
        super(EntityRegistry.FIRE_SLICE.get(), level);
        setShootDirection(shootDirection);
        float[] angles = Util.getXYRot(shootDirection);
        setXRot(angles[0]);
        setYRot(angles[1]);
    }

    @Override
    public void tick() {
        super.tick();
        this.move(MoverType.SELF, getShootDirection());

        if (this.horizontalCollision || this.verticalCollision) {
            this.discard();
        }

        if (this.tickCount > 120) {
            this.discard();
        }

        if (level() instanceof ServerLevel serverLevel) {
            List<LivingEntity> entities = serverLevel.getEntitiesOfClass(
                    LivingEntity.class,
                    this.getBoundingBox(),
                    entity -> entity != this.getOwner()
            );
            for (LivingEntity entity : entities) {
                entity.hurtServer(serverLevel, this.damageSources().magic(), 4);
                entity.setRemainingFireTicks(getRemainingFireTicks() + 80);
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_SHOOT_DIRECTION, Vec3.ZERO.toVector3f());
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putDouble("ShootDirectionX", getShootDirection().x);
        output.putDouble("ShootDirectionY", getShootDirection().y);
        output.putDouble("ShootDirectionZ", getShootDirection().z);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        double x = input.getDoubleOr("ShootDirectionX", 0);
        double y = input.getDoubleOr("ShootDirectionY", 0);
        double z = input.getDoubleOr("ShootDirectionZ", 0);
        setShootDirection(new Vec3(x, y, z));
    }

    public void setShootDirection(Vec3 direction) {
        this.entityData.set(DATA_SHOOT_DIRECTION, direction.toVector3f());
    }

    public Vec3 getShootDirection() {
        return new Vec3(this.entityData.get(DATA_SHOOT_DIRECTION));
    }
}
