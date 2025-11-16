package org.exodusstudio.frostbite.common.entity.custom.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.exodusstudio.frostbite.common.entity.custom.BorealBearEntity;

public class BorealBearAttackGoal extends Goal {
    private final BorealBearEntity bear;
    private final double speedModifier;
    private int ticksUntilNextPathRecalculation;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;

    public BorealBearAttackGoal(BorealBearEntity bear, double speedModifier) {
        this.bear = bear;
        this.speedModifier = speedModifier;
    }

    @Override
    public boolean canUse() {
        return bear.getTarget() != null;
    }

    @Override
    public void start() {
        super.start();
        this.ticksUntilNextPathRecalculation = 0;
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity livingentity = bear.getTarget();
        if (livingentity != null) {
            bear.getLookControl().setLookAt(livingentity, 30, 30);
            ticksUntilNextPathRecalculation = Math.max(ticksUntilNextPathRecalculation - 1, 0);
            if (bear.getSensing().hasLineOfSight(livingentity) && ticksUntilNextPathRecalculation <= 0 &&
                    (pathedTargetX == 0 && pathedTargetY == 0 && pathedTargetZ == 0 ||
                            livingentity.distanceToSqr(pathedTargetX, pathedTargetY, pathedTargetZ) >= 1 ||
                            bear.getRandom().nextFloat() < 0.05)) {
                pathedTargetX = livingentity.getX();
                pathedTargetY = livingentity.getY();
                pathedTargetZ = livingentity.getZ();
                ticksUntilNextPathRecalculation = 4 + bear.getRandom().nextInt(7);
                double d0 = bear.distanceToSqr(livingentity);
                if (d0 > 1024) {
                    ticksUntilNextPathRecalculation += 10;
                } else if (d0 > 256) {
                    ticksUntilNextPathRecalculation += 5;
                }

                if (!bear.getNavigation().moveTo(livingentity, speedModifier)) {
                    ticksUntilNextPathRecalculation += 15;
                }

                ticksUntilNextPathRecalculation = adjustedTickDelay(ticksUntilNextPathRecalculation);
            }

            bear.setAttacking(bear.distanceToSqr(livingentity) < 4);
        }
    }
}
