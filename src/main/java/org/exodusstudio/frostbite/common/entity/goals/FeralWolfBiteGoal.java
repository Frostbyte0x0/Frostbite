package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.FeralWolfEntity;

public class FeralWolfBiteGoal extends Goal {
    private final FeralWolfEntity feralWolf;
    private final double speedModifier;
    private int ticksUntilNextPathRecalculation;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;

    public FeralWolfBiteGoal(FeralWolfEntity feralWolf, double speedModifier) {
        this.feralWolf = feralWolf;
        this.speedModifier = speedModifier;
    }

    @Override
    public boolean canUse() {
        return feralWolf.getTarget() != null;
    }

    @Override
    public void start() {
        super.start();
        this.ticksUntilNextPathRecalculation = 0;
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity livingentity = feralWolf.getTarget();
        if (livingentity != null) {
            feralWolf.getLookControl().setLookAt(livingentity, 30, 30);
            ticksUntilNextPathRecalculation = Math.max(ticksUntilNextPathRecalculation - 1, 0);
            if (feralWolf.getSensing().hasLineOfSight(livingentity) && ticksUntilNextPathRecalculation <= 0 &&
                    (pathedTargetX == 0 && pathedTargetY == 0 && pathedTargetZ == 0 ||
                            livingentity.distanceToSqr(pathedTargetX, pathedTargetY, pathedTargetZ) >= 1 ||
                            feralWolf.getRandom().nextFloat() < 0.05)) {
                pathedTargetX = livingentity.getX();
                pathedTargetY = livingentity.getY();
                pathedTargetZ = livingentity.getZ();
                ticksUntilNextPathRecalculation = 4 + feralWolf.getRandom().nextInt(7);
                double d0 = feralWolf.distanceToSqr(livingentity);
                if (d0 > 1024) {
                    ticksUntilNextPathRecalculation += 10;
                } else if (d0 > 256) {
                    ticksUntilNextPathRecalculation += 5;
                }

                if (!feralWolf.getNavigation().moveTo(livingentity, speedModifier)) {
                    ticksUntilNextPathRecalculation += 15;
                }

                ticksUntilNextPathRecalculation = adjustedTickDelay(ticksUntilNextPathRecalculation);
            }

            feralWolf.setBiting(feralWolf.distanceToSqr(livingentity) < 4);
        }
    }
}
