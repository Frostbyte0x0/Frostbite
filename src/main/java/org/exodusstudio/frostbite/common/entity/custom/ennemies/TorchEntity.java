package org.exodusstudio.frostbite.common.entity.custom.ennemies;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.projectiles.FireSliceEntity;
import org.exodusstudio.frostbite.common.entity.goals.TorchSliceGoal;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class TorchEntity extends Monster implements RangedAttackMob {
    private static final EntityDataAccessor<String> DATA_STATE =
            SynchedEntityData.defineId(TorchEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DATA_LAST_STATE =
            SynchedEntityData.defineId(TorchEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> DATA_TICKS_SINCE_LAST_CHANGE =
            SynchedEntityData.defineId(TorchEntity.class, EntityDataSerializers.INT);
    public final AnimationState currentAnimationState = new AnimationState();
    public final AnimationState lastAnimationState = new AnimationState();
    public static final int BLEND_TICKS = 5;

    public TorchEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.TORCH.get(), level);
        currentAnimationState.start(tickCount);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new TorchSliceGoal(this, 0.8, 80, 20));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40)
                .add(Attributes.FOLLOW_RANGE, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
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

        if (getTarget() instanceof Player player && isSlicing() && currentAnimationState.getTimeInMillis(tickCount) / 50 == 20) {
            if (this.level() instanceof ServerLevel serverLevel) {
                Vec3 dir = player.position().subtract(this.position()).normalize();
                Vec3 pos = (new Vec3(this.getX(), this.getEyeY() - 0.2, this.getZ())).add(dir);
                FireSliceEntity fireSlice = new FireSliceEntity(serverLevel, dir);
                fireSlice.setOwner(this);
                fireSlice.setPos(pos);

                serverLevel.addFreshEntityWithPassengers(fireSlice);
                serverLevel.gameEvent(GameEvent.ENTITY_PLACE, this.blockPosition(), GameEvent.Context.of(fireSlice));
            }
        }

        setTicksSinceLastChange(getTicksSinceLastChange() + 1);

        if (getTicksSinceLastChange() > BLEND_TICKS) {
            lastAnimationState.stop();
        }

        if (isSlicing() && currentAnimationState.getTimeInMillis(tickCount) / 50 == 60 && !this.level().isClientSide) {
            setCurrentState("idle");
        }
    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
        setCurrentState("slicing");
    }

    public boolean isSlicing() {
        return this.getEntityData().get(DATA_STATE).equals("slicing");
    }

    public String getCurrentState() {
        return this.getEntityData().get(DATA_STATE);
    }

    public void setCurrentState(String state) {
        this.getEntityData().set(DATA_LAST_STATE, getCurrentState());
        this.getEntityData().set(DATA_STATE, state);
    }

    public String getLastState() {
        return this.getEntityData().get(DATA_LAST_STATE);
    }

    public int getTicksSinceLastChange() {
        return this.getEntityData().get(DATA_TICKS_SINCE_LAST_CHANGE);
    }

    public void setTicksSinceLastChange(int ticks) {
        this.getEntityData().set(DATA_TICKS_SINCE_LAST_CHANGE, ticks);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
