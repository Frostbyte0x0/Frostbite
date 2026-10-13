package org.exodusstudio.frostbite.common.entity.custom.projectiles;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.Util;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class ImpactZoneEntity extends Entity {
    public static final double DIAMETER = 5.0D;
    private static final int LIFETIME = 60;
    private static final int DAMAGE_INTERVAL = 5;
    private static final double PULL_STRENGTH = 0.28D;
    private static final double DAMAGE_RADIUS_SQUARED = DIAMETER * DIAMETER / 4.0D;

    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNER_UUID =
            SynchedEntityData.defineId(ImpactZoneEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);

    public ImpactZoneEntity(EntityType<? extends ImpactZoneEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_OWNER_UUID, Optional.empty());
    }

    @Override
    public void tick() {
        if (tickCount >= LIFETIME) {
            discard();
            return;
        }

        if (level() instanceof ServerLevel server) {
            Vec3 center = position().add(0.0D, 0.15D, 0.0D);
            for (int i = 0; i < 10; i++) {
                Vec3 offset = randomSpherePoint(server, DIAMETER / 2.0D);
                Vec3 particlePosition = center.add(offset);
                Vec3 velocity = center.subtract(particlePosition).normalize().scale(0.18D);
                server.sendParticles(ParticleTypes.REVERSE_PORTAL,
                        particlePosition.x, particlePosition.y, particlePosition.z,
                        1, velocity.x, velocity.y, velocity.z, 0.0D);
            }

            if (tickCount % DAMAGE_INTERVAL == 0) {
                LivingEntity owner = getOwner();
                for (LivingEntity target : server.getEntitiesOfClass(
                        LivingEntity.class, Util.squareAABB(center, DIAMETER))) {
                    if (target == owner || target.isSpectator()
                            || target.distanceToSqr(center) > DAMAGE_RADIUS_SQUARED) continue;

                    Vec3 pull = center.subtract(target.position()).normalize().scale(PULL_STRENGTH);
                    target.push(pull.x, pull.y * 0.35D, pull.z);

                    if (owner instanceof Player player) {
                        target.hurtServer(server, target.damageSources().playerAttack(player), 2.0F);
                    } else if (owner != null) {
                        target.hurtServer(server, target.damageSources().mobAttack(owner), 2.0F);
                    }
                }
            }
        }

        super.tick();
    }

    public void setOwner(@Nullable LivingEntity owner) {
        if (owner == null) {
            entityData.set(DATA_OWNER_UUID, Optional.empty());
        } else {
            entityData.set(DATA_OWNER_UUID, Optional.of(EntityReference.of(owner)));
        }
    }

    @Nullable
    public LivingEntity getOwner() {
        return entityData.get(DATA_OWNER_UUID)
                .map(reference -> reference.getEntity(level(), LivingEntity.class))
                .orElse(null);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        input.getString("OwnerUUID").ifPresent(value -> setOwnerUUID(UUID.fromString(value)));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        UUID ownerUuid = getOwnerUUID();
        if (ownerUuid != null) output.putString("OwnerUUID", ownerUuid.toString());
    }

    private void setOwnerUUID(@Nullable UUID uuid) {
        if (uuid == null) {
            entityData.set(DATA_OWNER_UUID, Optional.empty());
        } else {
            entityData.set(DATA_OWNER_UUID, Optional.of(EntityReference.of(uuid)));
        }
    }

    @Nullable
    private UUID getOwnerUUID() {
        return entityData.get(DATA_OWNER_UUID)
                .map(EntityReference::getUUID)
                .orElse(null);
    }

    private Vec3 randomSpherePoint(ServerLevel level, double radius) {
        double x;
        double y;
        double z;
        do {
            x = level.getRandom().nextDouble() * 2.0D - 1.0D;
            y = level.getRandom().nextDouble() * 2.0D - 1.0D;
            z = level.getRandom().nextDouble() * 2.0D - 1.0D;
        } while (x * x + y * y + z * z > 1.0D);

        return new Vec3(x, y, z).normalize().scale(radius);
    }
}
