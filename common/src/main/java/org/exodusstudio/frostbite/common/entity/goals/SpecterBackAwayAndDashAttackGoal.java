package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.SpecterEntity;

public class SpecterBackAwayAndDashAttackGoal extends FlyingChargeAttackGoal<SpecterEntity> {
    public SpecterBackAwayAndDashAttackGoal(SpecterEntity monster, float speedModifier) {
        super(monster, speedModifier);
    }

    @Override
    public void start() {
        super.start();
        if (monster.getState().equals("idle"))
            monster.setState("attacking");
    }

    @Override
    public void stop() {
        super.stop();
        monster.setState("idle");
    }

    @Override
    public void tick() {
        LivingEntity livingentity = monster.getTarget();
        if (livingentity != null) {
            if (monster.getBoundingBox().intersects(livingentity.getBoundingBox())) {
                monster.doHurtTarget(getServerLevel(monster.level()), livingentity);
                monster.setState("backing");
            } else {
                double d = monster.distanceTo(livingentity);
                if (monster.getState().equals("attacking")) {
                    Vec3 vec3 = livingentity.getEyePosition().subtract(monster.getEyePosition());
                    Vec3 wanted = livingentity.getEyePosition().add(vec3);
                    monster.getMoveControl().setWantedPosition(wanted.x, wanted.y, wanted.z, 0.65);
                } else if (monster.getState().equals("backing")) {
                    Vec3 vec3 = monster.getEyePosition().subtract(livingentity.getEyePosition()).normalize();
                    Vec3 wanted = livingentity.getEyePosition().add(vec3.multiply(1, 0.25, 1).scale(10));
                    monster.getMoveControl().setWantedPosition(wanted.x, wanted.y, wanted.z, 0.5);
                    if (d > 10 && monster.getRandom().nextFloat() < 0.05f) monster.setState("attacking");
                }
            }
        }
    }
}
