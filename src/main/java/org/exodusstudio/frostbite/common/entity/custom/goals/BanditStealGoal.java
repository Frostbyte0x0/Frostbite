package org.exodusstudio.frostbite.common.entity.custom.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.exodusstudio.frostbite.common.entity.custom.BanditEntity;

public class BanditStealGoal extends Goal {
    private final BanditEntity bandit;
    private final double speedModifier;
    private int ticksUntilNextPathRecalculation;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;

    public BanditStealGoal(BanditEntity bandit, double speedModifier) {
        this.bandit = bandit;
        this.speedModifier = speedModifier;
    }

    @Override
    public boolean canUse() {
        return bandit.getMainHandItem().isEmpty() && bandit.getTarget() != null;
    }

    @Override
    public void start() {
        super.start();
        this.ticksUntilNextPathRecalculation = 0;
        bandit.setWalking();
    }

    @Override
    public void stop() {
        super.stop();
        bandit.setIdle();
    }

    @Override
    public void tick() {
        super.tick();

        LivingEntity livingentity = bandit.getTarget();
        if (livingentity != null) {
            bandit.getLookControl().setLookAt(livingentity, 30, 30);
            ticksUntilNextPathRecalculation = Math.max(ticksUntilNextPathRecalculation - 1, 0);
            if (bandit.getSensing().hasLineOfSight(livingentity) && ticksUntilNextPathRecalculation <= 0 &&
                    (pathedTargetX == 0 && pathedTargetY == 0 && pathedTargetZ == 0 ||
                            livingentity.distanceToSqr(pathedTargetX, pathedTargetY, pathedTargetZ) >= 1 ||
                            bandit.getRandom().nextFloat() < 0.05)) {
                pathedTargetX = livingentity.getX();
                pathedTargetY = livingentity.getY();
                pathedTargetZ = livingentity.getZ();
                ticksUntilNextPathRecalculation = 4 + bandit.getRandom().nextInt(7);
                double d0 = bandit.distanceToSqr(livingentity);
                if (d0 > 1024) {
                    ticksUntilNextPathRecalculation += 10;
                } else if (d0 > 256) {
                    ticksUntilNextPathRecalculation += 5;
                }

                if (!bandit.getNavigation().moveTo(livingentity, speedModifier)) {
                    ticksUntilNextPathRecalculation += 15;
                }

                ticksUntilNextPathRecalculation = adjustedTickDelay(ticksUntilNextPathRecalculation);
            }

            if (bandit.distanceToSqr(livingentity) < 1.5) {
                bandit.setStealing();
            }
        }
    }
}
