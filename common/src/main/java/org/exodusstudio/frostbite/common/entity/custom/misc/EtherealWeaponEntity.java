package org.exodusstudio.frostbite.common.entity.custom.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class EtherealWeaponEntity extends Entity {
    public final AnimationState animationState = new AnimationState();
    public static final int ATTACK_DELAY = 15;
    public static final int LIFESPAN = 30;

    public EtherealWeaponEntity(EntityType<? extends Entity> type, Level level) {
        super(type, level);
        animationState.startIfStopped(tickCount);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {}

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float v) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {}

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {}

    public float getRange() {
        return 1;
    }

    @Override
    public void tick() {
        super.tick();

        if (tickCount == ATTACK_DELAY) {
            if (level() instanceof ServerLevel serverLevel) {
                if (this instanceof EtherealHammerEntity)
                    serverLevel.explode(null, getX(), getY(), getZ(), 1.5f, Level.ExplosionInteraction.MOB);

                Vec3 v = Vec3.ZERO;
                if (this instanceof EtherealSwordEntity)
                    v = getViewVector(0).scale(-1);

                serverLevel.getEntitiesOfClass(Entity.class,
                                getBoundingBox().inflate(getRange()).move(v))
                        .forEach(entity -> {
                            if (entity != this) {
                                entity.hurtServer(serverLevel, damageSources().generic(), 4);
                                if (this instanceof EtherealHandsEntity) {
                                    entity.push(entity.position().subtract(position()));
                                    if (entity instanceof Player) {
                                        Minecraft.getInstance().player.push(entity.position().subtract(position()));
                                    }
                                }
                            }
                        });
            }
        }

        if (tickCount > LIFESPAN) {
            discard();
        }
    }
}
