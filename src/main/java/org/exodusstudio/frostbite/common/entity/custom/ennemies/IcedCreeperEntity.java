package org.exodusstudio.frostbite.common.entity.custom.ennemies;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.util.TE;
import org.exodusstudio.frostbite.common.util.TemperatureEntity;

public class IcedCreeperEntity extends Creeper implements TemperatureEntity {
    protected final int explosionRadius = 4;

    public IcedCreeperEntity(EntityType<? extends Creeper> ignored, Level level) {
        super(EntityRegistry.ICED_CREEPER.get(), level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new SwellGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.FOLLOW_RANGE, 15)
                .add(Attributes.MOVEMENT_SPEED, 0.225);
    }

    @Override
    protected void explodeCreeper() {
        Level var2 = this.level();

        if (var2 instanceof ServerLevel serverlevel) {
            this.dead = true;
            serverlevel.explode(this, this.getX(), this.getY(), this.getZ(), this.getExplosionRadius(), Level.ExplosionInteraction.MOB);
            this.triggerOnDeathMobEffects(serverlevel, RemovalReason.KILLED);
            this.discard();
        }

        for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(getExplosionRadius()))) {
            if (entity != this && distanceToSqr(entity) <= (getExplosionRadius() * getExplosionRadius())) {
                ((TE) entity).decreaseTemperature(20, false);
            }
        }
    }

    public float getExplosionRadius() {
        return this.explosionRadius * (getStrengthModifier() + 0.5f);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public LivingEntity getInstance() {
        return this;
    }
}
