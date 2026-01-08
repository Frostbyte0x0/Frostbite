package org.exodusstudio.frostbite.common.entity.custom.helper;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class StateMonsterEntity extends Monster {
    protected static final EntityDataAccessor<String> DATA_LAST_STATE =
            SynchedEntityData.defineId(StateMonsterEntity.class, EntityDataSerializers.STRING);
    protected static final EntityDataAccessor<String> DATA_STATE =
            SynchedEntityData.defineId(StateMonsterEntity.class, EntityDataSerializers.STRING);
    protected static final EntityDataAccessor<Integer> DATA_TICKS_SINCE_LAST_CHANGE =
            SynchedEntityData.defineId(StateMonsterEntity.class, EntityDataSerializers.INT);
    public final AnimationState currentAnimationState = new AnimationState();
    public final AnimationState lastAnimationState = new AnimationState();
    public final int BLEND_TICKS;

    protected StateMonsterEntity(EntityType<? extends Monster> p_21368_, Level p_21369_, int blendTicks) {
        super(p_21368_, p_21369_);
        this.BLEND_TICKS = blendTicks;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_STATE, "idle");
        builder.define(DATA_LAST_STATE, "idle");
        builder.define(DATA_TICKS_SINCE_LAST_CHANGE, BLEND_TICKS);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        if (DATA_STATE.equals(accessor)) {
            this.lastAnimationState.copyFrom(currentAnimationState);
            this.currentAnimationState.stop();
            this.currentAnimationState.startIfStopped(tickCount);
            setTicksSinceLastChange(0);
            this.refreshDimensions();
        }

        super.onSyncedDataUpdated(accessor);
    }

    @Override
    public void tick() {
        super.tick();

        incrementTicksSinceLastChange();

        if (getTicksSinceLastChange() > BLEND_TICKS) {
            lastAnimationState.stop();
        }
    }

    public boolean isIdle() {
        return this.getEntityData().get(DATA_STATE).equals("idle");
    }

    public void setIdle() {
        this.getEntityData().set(DATA_LAST_STATE, getCurrentState());
        this.getEntityData().set(DATA_STATE, "idle");
    }

    public String getCurrentState() {
        return this.getEntityData().get(DATA_STATE);
    }

    public void setCurrentState(String state) {
        this.getEntityData().set(DATA_STATE, state);
    }

    public String getLastState() {
        return this.getEntityData().get(DATA_LAST_STATE);
    }

    public void setLastState(String state) {
        this.getEntityData().set(DATA_LAST_STATE, state);
    }

    public int getTicksSinceLastChange() {
        return this.getEntityData().get(DATA_TICKS_SINCE_LAST_CHANGE);
    }

    public void setTicksSinceLastChange(int ticks) {
        this.getEntityData().set(DATA_TICKS_SINCE_LAST_CHANGE, ticks);
    }

    public void incrementTicksSinceLastChange() {
        setTicksSinceLastChange(getTicksSinceLastChange() + 1);
    }
}
