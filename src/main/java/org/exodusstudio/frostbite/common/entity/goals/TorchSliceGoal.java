package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.TorchEntity;

public class TorchSliceGoal extends RangedAttackGoal {
    private final TorchEntity torch;

    public TorchSliceGoal(TorchEntity torch, double speedModifier, int attackInterval, float attackRadius) {
        super(torch, speedModifier, attackInterval, attackRadius);
        this.torch = torch;
    }
//
//    @Override
//    public void tick() {
//        super.tick();
//
//        LivingEntity target = torch.getTarget();
//        if (target != null) {
//            torch.getLookControl().setLookAt(target, 30, 30);
//            ticksUntilNextPathRecalculation = Math.max(ticksUntilNextPathRecalculation - 1, 0);
//            if (torch.getSensing().hasLineOfSight(target) && ticksUntilNextPathRecalculation <= 0 &&
//                    (pathedTargetX == 0 && pathedTargetY == 0 && pathedTargetZ == 0 ||
//                            target.distanceToSqr(pathedTargetX, pathedTargetY, pathedTargetZ) >= 1 ||
//                            torch.getRandom().nextFloat() < 0.05)) {
//                pathedTargetX = target.getX();
//                pathedTargetY = target.getY();
//                pathedTargetZ = target.getZ();
//                ticksUntilNextPathRecalculation = 4 + torch.getRandom().nextInt(7);
//                double d0 = torch.distanceToSqr(target);
//                if (d0 > 1024) {
//                    ticksUntilNextPathRecalculation += 10;
//                } else if (d0 > 256) {
//                    ticksUntilNextPathRecalculation += 5;
//                }
//
//                if (!torch.getNavigation().moveTo(target, speedModifier)) {
//                    ticksUntilNextPathRecalculation += 15;
//                }
//
//                ticksUntilNextPathRecalculation = adjustedTickDelay(ticksUntilNextPathRecalculation);
//            }
//
//            if (torch.distanceToSqr(target) < 10 && torch.getSensing().hasLineOfSight(target) && torch.getSlicingCooldown() == 0) {
//                torch.setSlicing(true);
//
//                torch.setSlicingCooldown(TorchEntity.SLICING_COOLDOWN_TICKS);
//            } else {
//                torch.setSlicing(torch.getSlicingCooldown() == 0);
//            }
//        }
//    }

    @Override
    public void stop() {
        super.stop();
        torch.setSlicing(false);
    }


//    @Override
//    public boolean canUse() {
//        return torch.getTarget() != null;
//    }
}
