package org.exodusstudio.frostbite.common.entity.custom.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FlyingChargeAttackGoal<T extends Monster> extends Goal {
    T monster;
    float speedModifier;
    
    public FlyingChargeAttackGoal(T monster, float speedModifier) {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.monster = monster;
        this.speedModifier = speedModifier;
    }

    public boolean canUse() {
        LivingEntity livingentity = monster.getTarget();
        return livingentity != null &&
                livingentity.isAlive() &&
                !monster.getMoveControl().hasWanted() &&
                monster.getRandom().nextInt(reducedTickDelay(7)) == 0 &&
                monster.distanceToSqr(livingentity) > (double) 4.0F;
    }

    public boolean canContinueToUse() {
        return monster.getMoveControl().hasWanted() && monster.getTarget() != null && monster.getTarget().isAlive();
    }

    public void start() {
        LivingEntity livingentity = monster.getTarget();
        if (livingentity != null) {
            Vec3 vec3 = livingentity.getEyePosition();
            monster.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, 1.0F);
        }
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity livingentity = monster.getTarget();
        if (livingentity != null) {
            if (monster.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                monster.doHurtTarget(getServerLevel(monster.level()), livingentity);
            } else {
                double d0 = monster.distanceToSqr(livingentity);
                if (d0 < (double)9.0F) {
                    Vec3 vec3 = livingentity.getEyePosition();
                    monster.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, speedModifier);
                }
            }
        }
    }
}

