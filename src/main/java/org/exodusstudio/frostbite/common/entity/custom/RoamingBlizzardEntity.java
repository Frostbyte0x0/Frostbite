package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
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
                this.level().addParticle(ParticleRegistry.SNOWFLAKE_PARTICLE.get(),
                        this.getRandomX(0.3F),
                        this.getRandomY() - 0.25F,
                        this.getRandomZ(0.3F),
                        (this.random.nextDouble() - 0.5F) * 2,
                        -this.random.nextDouble(),
                        (this.random.nextDouble() - 0.5F) * 2);
            }
        }

        super.aiStep();
    }
}
