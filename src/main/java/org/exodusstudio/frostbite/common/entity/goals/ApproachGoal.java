package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public class ApproachGoal extends Goal {
    private final PathfinderMob mob;
    private final double speedModifier;
    private int ticksUntilNextPathRecalculation;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;

    public ApproachGoal(PathfinderMob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
    }

    @Override
    public void start() {
        super.start();
        this.ticksUntilNextPathRecalculation = 0;
    }
    
    @Override
    public boolean canUse() {
        return mob.getTarget() != null;
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity livingentity = mob.getTarget();
        if (livingentity != null) {
            mob.getLookControl().setLookAt(livingentity, 30, 30);
            ticksUntilNextPathRecalculation = Math.max(ticksUntilNextPathRecalculation - 1, 0);
            if (mob.getSensing().hasLineOfSight(livingentity) && ticksUntilNextPathRecalculation <= 0 &&
                    (pathedTargetX == 0 && pathedTargetY == 0 && pathedTargetZ == 0 ||
                            livingentity.distanceToSqr(pathedTargetX, pathedTargetY, pathedTargetZ) >= 1 ||
                            mob.getRandom().nextFloat() < 0.05)) {
                pathedTargetX = livingentity.getX();
                pathedTargetY = livingentity.getY();
                pathedTargetZ = livingentity.getZ();
                ticksUntilNextPathRecalculation = 4 + mob.getRandom().nextInt(7);
                double d0 = mob.distanceToSqr(livingentity);
                if (d0 > 1024) {
                    ticksUntilNextPathRecalculation += 10;
                } else if (d0 > 256) {
                    ticksUntilNextPathRecalculation += 5;
                }

                if (!mob.getNavigation().moveTo(livingentity, speedModifier)) {
                    ticksUntilNextPathRecalculation += 15;
                }

                ticksUntilNextPathRecalculation = adjustedTickDelay(ticksUntilNextPathRecalculation);
            }

            action();
        }
    }

    protected void action() {

    }
}
