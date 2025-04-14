package org.exodusstudio.frostbite.common.entity.custom.murdershrooms;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.SporeCloudEntity;
import org.exodusstudio.frostbite.common.registry.EffectRegistry;

public abstract class AbstractMurdershroom extends Monster {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationStateTimeout = 0;
    protected final Holder<MobEffect> effect;

    public AbstractMurdershroom(EntityType<? extends Monster> entityType, Level level, Holder<MobEffect> effect) {
        super(entityType, level);
        this.effect = effect;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MurdershroomReleaseSpores(this, 1f, 3, 100));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 20D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.4D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    private void setupAnimationStates() {
        if (this.idleAnimationStateTimeout <= 0) {
            this.idleAnimationStateTimeout = 20;
            this.idleAnimationState.start(this.tickCount);
        }
        else {
            --this.idleAnimationStateTimeout;
        }
    }

    public void releaseSpores() {
        if (this.level() instanceof ServerLevel serverLevel) {
            SporeCloudEntity sporeCloud = new SporeCloudEntity(serverLevel, this.getX(), this.getY() + 0.5f, this.getZ());
            sporeCloud.setOwner(this);
            sporeCloud.setRadius(2.0F);
            sporeCloud.setDuration(200);
            sporeCloud.addEffect(new MobEffectInstance(effect, 20*120));
            serverLevel.addFreshEntity(sporeCloud);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            this.setupAnimationStates();
        }
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effectInstance) {
        return (!effectInstance.is(EffectRegistry.DECAY) &&
                !effectInstance.is(EffectRegistry.IRRITATION) &&
                !effectInstance.is(EffectRegistry.PARANOIA) &&
                !effectInstance.is(EffectRegistry.CORRUPTION) &&
                !effectInstance.is(EffectRegistry.MOLD) &&
                !effectInstance.is(EffectRegistry.FATIGUE) &&
                !effectInstance.is(EffectRegistry.TWITCHING));
    }

    private static class MurdershroomReleaseSpores extends Goal {
        private final AbstractMurdershroom murdershroom;
        private LivingEntity target;
        private final float speedModifier;
        private final float releaseDistance;
        private final float cooldown;
        private float ticksSinceLastRelease;
        private double posX;
        private double posY;
        private double posZ;

        public MurdershroomReleaseSpores(AbstractMurdershroom murdershroom, float speedModifier, float releaseDistance, float cooldown) {
            this.murdershroom = murdershroom;
            this.speedModifier = speedModifier;
            this.releaseDistance = releaseDistance;
            this.cooldown = cooldown;
        }

        @Override
        public void tick() {
            if (ticksSinceLastRelease >= cooldown && this.target != null) {
                this.murdershroom.getNavigation().moveTo(target, speedModifier);
                if (this.murdershroom.distanceToSqr(target.getX(), target.getY(), target.getZ()) <= releaseDistance) {
                    murdershroom.releaseSpores();
                    ticksSinceLastRelease = -1;
                }
            }
            else {
                this.murdershroom.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
            }
            ticksSinceLastRelease++;
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = murdershroom.getTarget();
            if (livingentity != null && livingentity.isAlive()) {
                this.target = livingentity;
                if (RandomSource.create().nextFloat() <= (1 / 20f)) {
                    findRandomPosition();
                }
                return true;
            }
            return false;
        }

        protected void findRandomPosition() {
            Vec3 vec3 = DefaultRandomPos.getPos(this.murdershroom, 10, 4);
            if (vec3 != null && vec3.distanceTo(this.target.position()) > 5f) {
                this.posX = vec3.x;
                this.posY = vec3.y;
                this.posZ = vec3.z;
            }
        }
    }
}
