package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

import java.util.Optional;

public class FrozenRemnantsEntity extends Mob {
    protected SimpleContainer inventory;
    private static final EntityDataAccessor<Integer> DATA_OWNER_ID;
    private static final EntityDataAccessor<Boolean> DATA_IS_ON_SCREEN;
    private static final EntityDataAccessor<Float> DATA_HEAD_PITCH;

    public FrozenRemnantsEntity(EntityType<? extends Mob> entityType, Level level) {
        super(EntityRegistry.FROZEN_REMNANTS.get(), level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_OWNER_ID, 0);
        builder.define(DATA_IS_ON_SCREEN, true);
        builder.define(DATA_HEAD_PITCH, 0f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("ownerID", this.getOwner().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setOwner(compound.getInt("ownerID"));
    }

    public void setOnScreen(boolean isOnScreen) {
        this.getEntityData().set(DATA_IS_ON_SCREEN, isOnScreen);
    }

    public boolean isOnScreen() {
        return this.getEntityData().get(DATA_IS_ON_SCREEN);
    }

    public float getHeadPitch() {
        return this.getEntityData().get(DATA_HEAD_PITCH);
    }

    public void setHeadPitch(float headPitch) {
        this.getEntityData().set(DATA_HEAD_PITCH, headPitch);
    }

    public void setOwner(Entity owner) {
        if (owner != null) {
            this.getEntityData().set(DATA_OWNER_ID, owner.getId());
        }
    }

    public void setOwner(int id) {
        this.getEntityData().set(DATA_OWNER_ID, id);
    }

    public Entity getOwner() {
        return this.level().getEntity(this.getEntityData().get(DATA_OWNER_ID));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8D);
    }

    @Override
    public void tick() {
        super.tick();
        Entity owner = this.getOwner();
        if (owner == null || !owner.isAlive()) {
            return;
        }

        if (!isOnScreen()) {
            this.setYRot(calculateYRot().orElse(0f));
            this.setHeadPitch(calculateHeadPitch().orElse(0f));
        }
    }

    public Optional<Float> calculateHeadPitch() {
        Entity owner = this.getOwner();
        if (owner == null || !owner.isAlive()) {
            return Optional.empty();
        }
        double d0 = owner.getX() - this.getX();
        double d1 = owner.getEyeY() - this.getEyeY();
        double d2 = owner.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        return !(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d3) > (double)1.0E-5F) ? Optional.empty() : Optional.of((float)(-(Mth.atan2(d1, d3) * (double)180.0F / (double)(float)Math.PI)));
    }

    public Optional<Float> calculateYRot() {
        Entity owner = this.getOwner();
        double d0 = owner.getX() - this.getX();
        double d1 = owner.getZ() - this.getZ();
        return !(Math.abs(d1) > (double)1.0E-5F) && !(Math.abs(d0) > (double)1.0E-5F) ? Optional.empty() : Optional.of((float)(Mth.atan2(d1, d0) * (double)180.0F / (double)(float)Math.PI) - 90.0F);
    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float damage) {
        if (damageSource.is(DamageTypes.FALL)) {
            return false;
        }
        Player player = Minecraft.getInstance().player;
        assert player != null;
        for (int i = 0; i < 180; i++) {
            player.level().addAlwaysVisibleParticle(
                    ParticleTypes.DRIPPING_WATER,
                    this.getX() - random.nextFloat() + 0.5f,
                    this.getY() + 1.5f * random.nextFloat() + 0.5f,
                    this.getZ() - random.nextFloat() + 0.5f,
                    0,
                    0,
                    0);

        }

        this.level().playSound(null, this.getOnPos(), SoundEvents.LAVA_EXTINGUISH, SoundSource.HOSTILE, 1f, this.level().getRandom().nextFloat() * 0.1F + 0.9F);
        this.discard();
        return false;
    }

    static {
        DATA_OWNER_ID = SynchedEntityData.defineId(FrozenRemnantsEntity.class, EntityDataSerializers.INT);
        DATA_IS_ON_SCREEN = SynchedEntityData.defineId(FrozenRemnantsEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_HEAD_PITCH = SynchedEntityData.defineId(FrozenRemnantsEntity.class, EntityDataSerializers.FLOAT);
    }
}
