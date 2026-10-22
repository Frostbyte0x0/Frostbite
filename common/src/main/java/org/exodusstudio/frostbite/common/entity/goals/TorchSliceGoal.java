package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.TorchEntity;

public class TorchSliceGoal extends RangedAttackGoal {
    private final TorchEntity torch;

    public TorchSliceGoal(TorchEntity torch, double speedModifier, int attackInterval, float attackRadius) {
        super(torch, speedModifier, attackInterval, attackRadius);
        this.torch = torch;
    }

    @Override
    public void stop() {
        super.stop();
        torch.setCurrentState("idle");
    }
}
