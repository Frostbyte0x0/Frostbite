package org.exodusstudio.frostbite.common.entity.custom.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;

import java.util.EnumSet;

public class FlyingRandomMoveGoal extends Goal {
    Monster monster;
    float speedModifier;
    
    public FlyingRandomMoveGoal(Monster monster, float speedModifier) {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.speedModifier = speedModifier;
        this.monster = monster;
    }

    public boolean canUse() {
        return !monster.getMoveControl().hasWanted() && monster.getRandom().nextInt(30) == 0;
    }

    public boolean canContinueToUse() {
        return false;
    }

    public void tick() {
        BlockPos blockpos = monster.blockPosition();

        for (int i = 0; i < 3; ++i) {
            BlockPos target = blockpos.offset(monster.getRandom().nextInt(20) - 10, monster.getRandom().nextInt(10) - 6, monster.getRandom().nextInt(20) - 10);

            HitResult hitResult = monster.level().clip(
                    new ClipContext(
                            target.getBottomCenter(),
                            target.offset(0, -15, 0).getBottomCenter(),
                            ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, monster));

            if (hitResult.getType() == HitResult.Type.MISS) {
                target = target.offset(0, -5, 0);
            }

            if (monster.level().isEmptyBlock(target)) {
                monster.getMoveControl().setWantedPosition(target.getX() + 0.5F, target.getY() + 0.5F, target.getZ() + 0.5F, speedModifier);
                if (monster.getTarget() == null) {
                    monster.getLookControl().setLookAt(target.getX() + 0.5F, target.getY() + 0.5F, target.getZ() + 0.5F, 180.0F, 20.0F);
                }
                break;
            }
        }

    }
}
