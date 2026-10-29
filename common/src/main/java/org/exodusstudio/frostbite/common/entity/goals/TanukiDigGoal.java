package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import org.exodusstudio.frostbite.common.entity.custom.animals.TanukiEntity;

public class TanukiDigGoal extends Goal {
    final TanukiEntity tanuki;

    public TanukiDigGoal(TanukiEntity tanuki) {
        this.tanuki = tanuki;
    }

    @Override
    public boolean canUse() {
        return !tanuki.isSitting();
    }
}
