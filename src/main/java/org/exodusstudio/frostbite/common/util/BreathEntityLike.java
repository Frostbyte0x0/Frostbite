package org.exodusstudio.frostbite.common.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;

public class BreathEntityLike {
    Vec3 speed;
    AABB aabb;
    int age = 0;
    ServerLevel level;
    LivingEntity owner;

    public BreathEntityLike(Vec3 speed, AABB aabb, ServerLevel level, LivingEntity owner) {
        this.speed = speed;
        this.aabb = aabb;
        this.level = level;
        this.owner = owner;
    }

    public void tick() {
        age++;
        if (age > 40) {
            Frostbite.breathEntityLikesToRemove.add(this);
            return;
        }

        aabb = aabb.move(speed);

        if (level.getGameTime() % 5 == 0) {
            for (LivingEntity entity : level.getEntitiesOfClass(LivingEntity.class, aabb)) {
                if (entity == owner) continue;
                Frostbite.temperatureStorage.decreaseTemperature(entity, 3, false);
                Frostbite.temperatureStorage.decreaseTemperature(entity, 2, true);
            }
        }
    }

    public AABB getAabb() {
        return aabb;
    }
}
