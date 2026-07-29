package org.exodusstudio.frostbite.common.entity.custom.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Optional;
import java.util.UUID;

public class PlayerIllusionEntity extends LivingEntity {
    private static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNER_UUID =
            SynchedEntityData.defineId(PlayerIllusionEntity.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    private static final EntityDataAccessor<Vector3fc> DATA_OFFSET =
            SynchedEntityData.defineId(PlayerIllusionEntity.class, EntityDataSerializers.VECTOR3);

    public PlayerIllusionEntity(EntityType<? extends Entity> ignored, Level level) {
        super(EntityRegistry.PLAYER_ILLUSION.get(), level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_OWNER_UUID, Optional.empty());
        builder.define(DATA_OFFSET, new Vector3f());
    }

    @Override
    public void tick() {
        super.tick();
        if (getOwner() != null && getOwner().isAlive()) setDeltaMovement(getOwner().getDeltaMovement());
        else discard();
        if (tickCount > 200) discard();
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
        return false;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean hurtServer(ServerLevel l, DamageSource damageSource, float v) {
        for (int i = 0; i < 80; i++) {
            l.sendParticles(
                    ParticleRegistry.SWIRLING_LEAF_PARTICLE.get(),
                    getX() + 0.5f * l.getRandom().nextDouble(), // - Math.sin(damageSource.getEntity().yHeadRot * Math.PI / 180) / 1.5f,
                    getY() + 0.5f * l.getRandom().nextDouble() + 1.25f,
                    getZ() + 0.5f * l.getRandom().nextDouble(), // + Math.cos(p.yHeadRot * Math.PI / 180) / 1.5f,
                    1,
                    (0.5 - l.getRandom().nextDouble()) * 0.3,
                    (0.5 - l.getRandom().nextDouble()) * 0.3,
                    (0.5 - l.getRandom().nextDouble()) * 0.3,
                    0.1);
        }
        l.playSound(null, BlockPos.containing(position()), SoundEvents.LAVA_EXTINGUISH, SoundSource.HOSTILE, 1f, l.getRandom().nextFloat() * 0.1F + 0.9F);
//        this.discard();
        return true;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {
        super.readAdditionalSaveData(valueInput);
        this.setOwnerUUID(UUID.fromString(valueInput.getString("ownerUUID").get()));
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {
        super.addAdditionalSaveData(valueOutput);
        valueOutput.putString("ownerUUID", this.getOwnerUUID().toString());
    }

    public void setOwnerUUID(UUID uuid) {
        this.getEntityData().set(DATA_OWNER_UUID, Optional.of(EntityReference.of(uuid)));
    }

    @Override
    public InterpolationHandler getInterpolation() {
        return null;
    }

    public UUID getOwnerUUID() {
        if (this.getEntityData().get(DATA_OWNER_UUID).isEmpty()) {
            return null;
        }
        return this.getEntityData().get(DATA_OWNER_UUID).get().getUUID();
    }

    public Entity getOwner() {
        if (this.getOwnerUUID() == null) {
            return null;
        }
        return level().getEntity(this.getOwnerUUID());
    }
}
