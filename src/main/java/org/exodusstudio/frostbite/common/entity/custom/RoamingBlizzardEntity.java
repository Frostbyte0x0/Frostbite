package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.custom.goals.OwnerHurtByTargetGoal;
import org.exodusstudio.frostbite.common.entity.custom.goals.OwnerHurtTargetGoal;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;
import org.exodusstudio.frostbite.common.util.Ownable;

import javax.annotation.Nullable;
import java.util.List;

public class RoamingBlizzardEntity extends Monster implements Ownable {
    @Nullable
    private EntityReference<LivingEntity> owner;
    private final Vec3 windDir = new Vec3(
            random.nextFloat() * 2 * Math.PI,
            (random.nextFloat() - 1) * Math.PI,
            random.nextFloat() * 2 * Math.PI)
            .normalize().scale(0.2);

    public RoamingBlizzardEntity(EntityType<? extends Monster> ignored, Level level) {
        super(EntityRegistry.ROAMING_BLIZZARD.get(), level);
        Frostbite.temperatureStorage.setTemperatures(this, List.of(-40f, -60f));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40D)
                .add(Attributes.FOLLOW_RANGE, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.owner = EntityReference.read(input, "owner");
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        EntityReference.store(this.owner, output, "owner");
    }

    @Nullable
    public LivingEntity getOwner() {
        return EntityReference.get(this.owner, this.level(), LivingEntity.class);
    }

    public void setOwner(LivingEntity owner) {
        this.owner = new EntityReference<>(owner);
    }

    @Override
    public void restoreFrom(Entity entity) {
        super.restoreFrom(entity);
        if (entity instanceof RoamingBlizzardEntity blizzard) {
            this.owner = blizzard.owner;
        }
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected int getBaseExperienceReward(ServerLevel p_376894_) {
        return 0;
    }

    @Override
    public void aiStep() {
        if (level().isClientSide) {
            for (int i = 0; i < 20; ++i) {
                level().addParticle(
                        ColorParticleOption.create(ParticleRegistry.ROAMING_BLIZZARD_PARTICLE.get(),
                                random.nextInt(80) + 80,
                                random.nextInt(30) + 30,
                                random.nextInt(80) + 150
                        ),
                        this.getRandomX(0.7F) - windDir.x,
                        this.getRandomY() - windDir.y,
                        this.getRandomZ(0.7F) - windDir.z,
                        windDir.x,
                        windDir.y,
                        windDir.z);
            }
        }

        super.aiStep();
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public void tick() {
        if (level().getGameTime() % 20 == 0) {
            for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox())
                    .stream().filter((e) -> !(e instanceof RoamingBlizzardEntity)).toList()) {
                Frostbite.temperatureStorage.decreaseTemperature(entity, 8, false);
            }
        }

        if (Frostbite.temperatureStorage.getTemperature(this, true) >= -10) {
            die(level().damageSources().onFire());
        }

        super.tick();
    }
}
