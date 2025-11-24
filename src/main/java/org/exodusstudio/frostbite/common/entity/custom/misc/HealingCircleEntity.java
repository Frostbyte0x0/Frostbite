package org.exodusstudio.frostbite.common.entity.custom.misc;

import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.ARGB;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.common.entity.custom.elves.ElfEntity;
import org.exodusstudio.frostbite.common.particle.options.BooleanParticleOption;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;
import org.exodusstudio.frostbite.common.registry.ParticleRegistry;

import java.util.List;

public class HealingCircleEntity extends AreaEffectCloud {
    private static final EntityDataAccessor<Boolean> DATA_IS_BLESSING =
            SynchedEntityData.defineId(HealingCircleEntity.class, EntityDataSerializers.BOOLEAN);
    private final int lifetime = 60;

    public HealingCircleEntity(EntityType<? extends AreaEffectCloud> ignored, Level level) {
        super(EntityRegistry.HEALING_CIRCLE.get(), level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_IS_BLESSING, false);
    }

    @Override
    public void tick() {
        if (getRadius() == 0) {
            setRadius(1.5f);
        }

        if (tickCount == 1) {
            if (level().isClientSide) {
                level().addParticle(
                        ColorParticleOption.create(ParticleRegistry.EXPANDING_CIRCLE_PARTICLE.get(),
                        ARGB.color(
                        lifetime,
                        20,
                        255,
                        70
                        )),
                        getX(), getY(), getZ(), 0, 0, 0);
            }
        }

        if (tickCount == lifetime) {
            List<LivingEntity> list1 = level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(0, 3, 0));
            if (!list1.isEmpty()) {
                for (LivingEntity livingentity : list1) {
                    if (getOwner() instanceof ElfEntity && livingentity instanceof Player) continue;
                    if (isBlessing()) {
                        MobEffectInstance regeneration = new MobEffectInstance(MobEffects.REGENERATION, 600, 0);
                        MobEffectInstance strength = new MobEffectInstance(MobEffects.STRENGTH, 200, 1);
                        MobEffectInstance resistance = new MobEffectInstance(MobEffects.RESISTANCE, 600, 0);
                        livingentity.addEffect(regeneration);
                        livingentity.addEffect(strength);
                        livingentity.addEffect(resistance);
                    } else {
                        livingentity.heal(6);
                    }
                }
            }

            for (int i = 0; i < 50; i++) {
                this.level().addParticle(BooleanParticleOption.create(ParticleRegistry.HEAL_PARTICLE.get(), isBlessing()),
                        getX() + (0.5 - random.nextDouble()) * 2.5,
                        getY() + 0.2f + (0.5 - random.nextDouble()) * 1.5,
                        getZ() + (0.5 - random.nextDouble()) * 2.5,
                        0, 0.1 + random.nextDouble() * 0.1, 0);
            }
        }

        if (this.tickCount > lifetime) this.discard();
    }

    public void setBlessing(boolean isBlessing) {
        this.entityData.set(DATA_IS_BLESSING, isBlessing);
    }

    public boolean isBlessing() {
        return this.entityData.get(DATA_IS_BLESSING);
    }
}
