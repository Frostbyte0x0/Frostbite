package org.exodusstudio.frostbite.common.entity.custom.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import org.exodusstudio.frostbite.common.entity.custom.RevenantEntity;

public class RevenantRecoverGoal extends Goal {
    RevenantEntity revenant;

    public RevenantRecoverGoal(RevenantEntity revenant) {
        this.revenant = revenant;
    }

    @Override
    public void tick() {
        super.tick();
        if (revenant.tickCount % 10 == 0) {
            revenant.setHealth(revenant.getHealth() + 1);
        }
    }

    @Override
    public void start() {
        super.start();
        revenant.setRecovering(true);
    }

    @Override
    public void stop() {
        super.stop();
        revenant.setRecovering(false);
    }

    @Override
    public boolean canUse() {
        return revenant.getHealth() <= 1;
    }

    @Override
    public boolean canContinueToUse() {
        return revenant.getHealth() <= 20;
    }
}
