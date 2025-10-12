package org.exodusstudio.frostbite.common.entity.custom;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.registry.EntityRegistry;

public class IcedCreeperEntity extends Creeper {
    protected int explosionRadius = 4;

    public IcedCreeperEntity(EntityType<? extends Creeper> ignored, Level level) {
        super(EntityRegistry.ICED_CREEPER.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 30)
                .add(Attributes.FOLLOW_RANGE, 15)
                .add(Attributes.MOVEMENT_SPEED, 0);
    }

    @Override
    protected void explodeCreeper() {
        super.explodeCreeper();
        for (LivingEntity entity : level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(explosionRadius))) {
            if (entity != this && distanceToSqr(entity) <= (explosionRadius * explosionRadius)) {
                Frostbite.temperatureStorage.decreaseTemperature(entity, 20, false);
            }
        }
    }

    @Override
    public boolean canFreeze() {
        return false;
    }
}
