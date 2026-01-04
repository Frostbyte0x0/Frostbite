package org.exodusstudio.frostbite.common.entity.custom.misc;

import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.Util;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import javax.annotation.Nullable;
import java.util.*;

public class CurseBallEntity extends Entity {
    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNER_UUID =
            SynchedEntityData.defineId(CurseBallEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_CURSED_ENTITY =
            SynchedEntityData.defineId(CurseBallEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    private static final EntityDataAccessor<Vector3fc> DATA_LAUNCH_DIRECTION =
            SynchedEntityData.defineId(CurseBallEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<String> DATA_CURSE =
            SynchedEntityData.defineId(CurseBallEntity.class, EntityDataSerializers.STRING);
    private final static int LAUNCH_DELAY = 5;
    private final static int LAUNCH_TIME = 100;
    private final static float SPEED = 0.75f;
    private final static float RADIUS = 1.25f;
    private final static float SPIN_SPEED = 0.166f;
    private static final Map<String, Holder<MobEffect>> EFFECTS =
            Map.of(
                "leech", EffectRegistry.LEECH_CURSE,
                "static", EffectRegistry.STATIC_CURSE,
                "perpetual", EffectRegistry.PERPETUAL_CURSE
            );
    private static final Map<String, Integer> DURATIONS =
            Map.of(
                "leech", 1200,
                "static", 100,
                "perpetual", -1
            );

    public CurseBallEntity(EntityType<? extends Entity> ignored, Level level) {
        super(EntityRegistry.CURSE_BALL.get(), level);
        setDeltaMovement(0, 0.75, 0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_OWNER_UUID, Optional.empty());
        builder.define(DATA_CURSED_ENTITY, Optional.empty());
        builder.define(DATA_LAUNCH_DIRECTION, new Vector3f(0, 0, 0));
        builder.define(DATA_CURSE, "none");
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        if (input.getString("ownerUUID").isPresent()) {
            this.setOwnerUUID(UUID.fromString(input.getString("ownerUUID").get()));
        }
        if (input.getString("cursedUUID").isPresent()) {
            this.setCursedEntityUUID(UUID.fromString(input.getString("cursedUUID").get()));
        }
        if (input.getString("curse").isPresent()) {
            this.setCurse(input.getString("curse").get());
        }
        Vec3 dir = new Vec3(
                input.getDoubleOr("dirX", 0.0),
                input.getDoubleOr("dirY", 0.0),
                input.getDoubleOr("dirZ", 0.0)
        );
        if (!dir.equals(Vec3.ZERO)) {
            this.setLaunchDirection(dir);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        if (this.getCursedEntityUUID() != null) {
            output.putString("ownerUUID", this.getOwnerUUID().toString());
        }
        if (this.getCursedEntityUUID() != null) {
            output.putString("cursedUUID", this.getCursedEntityUUID().toString());
        }
        output.putString("curse", getCurse());
        output.putDouble("dirX", getLaunchDirection().x);
        output.putDouble("dirY", getLaunchDirection().y);
        output.putDouble("dirZ", getLaunchDirection().z);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount == LAUNCH_DELAY && getCursedEntity() == null) {
            this.setDeltaMovement(this.getLaunchDirection().normalize().scale(SPEED));
            float[] angles = Util.getXYRot(getLaunchDirection());
            setXRot(angles[0]);
            setYRot(angles[1]);
            if (level().getRandom().nextFloat() < 0.333f) {
                setCurse("leech");
            } else if (level().getRandom().nextFloat() < 0.333f) {
                setCurse("static");
            } else {
                setCurse("perpetual");
            }
        }

        if ((horizontalCollision || verticalCollision || tickCount > LAUNCH_TIME) && getCursedEntity() == null) {
            this.discard();
            return;
        }

        if (getCursedEntity() instanceof LivingEntity livingEntity && (!livingEntity.hasEffect(EFFECTS.get(getCurse())) || livingEntity.isDeadOrDying())) {
            this.discard();
            return;
        }

        if (getCursedEntity() == null && tickCount > LAUNCH_DELAY && level() instanceof ServerLevel serverLevel) {
            List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox());
            if (!entities.isEmpty()) {
                setCursedEntityUUID(entities.getFirst().getUUID());
                this.noPhysics = true;
                setDeltaMovement(0, 0, 0);
                entities.getFirst().hurtServer(serverLevel, level().damageSources().generic(), 5);
                entities.getFirst().addEffect(new MobEffectInstance(EFFECTS.get(getCurse()), DURATIONS.get(getCurse()), 0));
            }
        }

        move(MoverType.SELF, this.getDeltaMovement());
        reapplyPosition();
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    @Override
    public final Vec3 getPosition(float partialTick) {
        if (getCursedEntity() == null) {
            return super.getPosition(partialTick);
        }

        float angle = (this.tickCount + partialTick) * SPIN_SPEED;
        float x = (float) Math.cos(angle) * RADIUS;
        float z = (float) Math.sin(angle) * RADIUS;
        return (new Vec3(x, 0, z)).add(getCursedEntity().getPosition(partialTick)).add(0, 0.7, 0);
    }

    public @Nullable Entity getOwner() {
        if (this.getOwnerUUID() == null) {
            return null;
        }
        if (this.level() instanceof ServerLevel serverLevel) {
            return serverLevel.getEntity(this.getOwnerUUID());
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

    public UUID getOwnerUUID() {
        if (this.getEntityData().get(DATA_OWNER_UUID).isEmpty()) {
            return null;
        }
        return this.getEntityData().get(DATA_OWNER_UUID).get().getUUID();
    }

    public @Nullable Entity getCursedEntity() {
        if (this.getCursedEntityUUID() == null) {
            return null;
        }
        return level().getEntity(this.getCursedEntityUUID());
    }

    public void setCursedEntityUUID(@Nullable UUID uuid) {
        if (uuid == null) {
            this.getEntityData().set(DATA_CURSED_ENTITY, Optional.empty());
            return;
        }
        this.getEntityData().set(DATA_CURSED_ENTITY, Optional.of(EntityReference.of(uuid)));
    }

    public UUID getCursedEntityUUID() {
        if (this.getEntityData().get(DATA_CURSED_ENTITY).isEmpty()) {
            return null;
        }
        return this.getEntityData().get(DATA_CURSED_ENTITY).get().getUUID();
    }

    public void setLaunchDirection(Vec3 direction) {
        this.getEntityData().set(DATA_LAUNCH_DIRECTION, direction.toVector3f());
    }

    public Vec3 getLaunchDirection() {
        return new Vec3(this.getEntityData().get(DATA_LAUNCH_DIRECTION));
    }

    public String getCurse() {
        return this.getEntityData().get(DATA_CURSE);
    }

    public void setCurse(String curse) {
        this.getEntityData().set(DATA_CURSE, curse);
    }
}
