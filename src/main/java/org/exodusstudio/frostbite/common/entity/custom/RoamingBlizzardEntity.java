package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;

public class RoamingBlizzardEntity extends Monster {
    public RoamingBlizzardEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.ROAMING_BLIZZARD.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 8D)
                .add(Attributes.FOLLOW_RANGE, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public void aiStep() {
        if (this.level().isClientSide) {
            for (int i = 0; i < 20; ++i) {
                this.level().addParticle(
                        ColorParticleOption.create(ParticleRegistry.BLIZZARD_PARTICLE.get(), ARGB.color(
                                random.nextInt(80) + 80,
                                random.nextInt(30) + 30,
                                random.nextInt(80) + 150
                        )),
                        this.getRandomX(0.4F),
                        this.getRandomY() - 0.25F,
                        this.getRandomZ(0.4F),
                        (this.random.nextDouble() - 0.5F) * 2,
                        -this.random.nextDouble(),
                        (this.random.nextDouble() - 0.5F) * 2);
            }
        }

        super.aiStep();
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    protected void pushEntities() {
        for (Entity entity : this.level().getEntities(this, this.getBoundingBox(), RIDABLE_MINECARTS)) {
            if (this.distanceToSqr(entity) <= 0.2) {
                entity.push(this);
            }
        }

    }

    @Override
    public void tick() {
        if (level().getGameTime() % 20 == 0) {
            for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())) {
                Frostbite.temperatureStorage.decreaseTemperature(entity, 8, false);
            }
        }

        super.tick();
    }
}
