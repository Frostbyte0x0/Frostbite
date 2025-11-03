package org.exodusstudio.frostbite.common.entity.custom.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.exodusstudio.frostbite.common.entity.custom.TorchEntity;

public class TorchSliceGoal extends Goal {
    private final TorchEntity torch;
    private final double speedModifier;
    private int ticksUntilNextPathRecalculation;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;

    public TorchSliceGoal(TorchEntity torch, double speedModifier) {
        this.torch = torch;
        this.speedModifier = speedModifier;
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity livingentity = torch.getTarget();
        if (livingentity != null) {
            torch.getLookControl().setLookAt(livingentity, 30, 30);
            ticksUntilNextPathRecalculation = Math.max(ticksUntilNextPathRecalculation - 1, 0);
            if (torch.getSensing().hasLineOfSight(livingentity) && ticksUntilNextPathRecalculation <= 0 &&
                    (pathedTargetX == 0 && pathedTargetY == 0 && pathedTargetZ == 0 ||
                            livingentity.distanceToSqr(pathedTargetX, pathedTargetY, pathedTargetZ) >= 1 ||
                            torch.getRandom().nextFloat() < 0.05)) {
                pathedTargetX = livingentity.getX();
                pathedTargetY = livingentity.getY();
                pathedTargetZ = livingentity.getZ();
                ticksUntilNextPathRecalculation = 4 + torch.getRandom().nextInt(7);
                double d0 = torch.distanceToSqr(livingentity);
                if (d0 > 1024) {
                    ticksUntilNextPathRecalculation += 10;
                } else if (d0 > 256) {
                    ticksUntilNextPathRecalculation += 5;
                }

                if (!torch.getNavigation().moveTo(livingentity, speedModifier)) {
                    ticksUntilNextPathRecalculation += 15;
                }

                ticksUntilNextPathRecalculation = adjustedTickDelay(ticksUntilNextPathRecalculation);
            }

            if (torch.distanceToSqr(livingentity) < 10 && torch.getSensing().hasLineOfSight(livingentity)) {
                torch.setSlicing(true);
                // spawn fire slice
            } else {
                torch.setSlicing(false);
            }

        }
    }

    @Override
    public boolean canUse() {
        return torch.getTarget() != null;
    }
}
