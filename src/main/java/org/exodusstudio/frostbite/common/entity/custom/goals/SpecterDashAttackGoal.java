package org.exodusstudio.frostbite.common.entity.custom.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.SpecterEntity;

public class SpecterDashAttackGoal extends FlyingChargeAttackGoal<SpecterEntity> {
    public SpecterDashAttackGoal(SpecterEntity monster, float speedModifier) {
        super(monster, speedModifier);
    }

    @Override
    public void start() {
        super.start();
        monster.setAttacking(true);
    }

    @Override
    public void stop() {
        super.stop();
        monster.setAttacking(false);
    }

    public void tick() {
        LivingEntity livingentity = monster.getTarget();
        if (livingentity != null) {
            if (monster.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                monster.doHurtTarget(getServerLevel(monster.level()), livingentity);
            } else {
                double d0 = monster.distanceToSqr(livingentity);
                if (d0 < 10) {
                    Vec3 vec3 = livingentity.getEyePosition();
                    monster.getMoveControl().setWantedPosition(vec3.x, vec3.y, vec3.z, speedModifier);
                }
            }
        }
    }
}
