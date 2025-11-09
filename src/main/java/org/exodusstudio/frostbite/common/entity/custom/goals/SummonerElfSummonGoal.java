package org.exodusstudio.frostbite.common.entity.custom.goals;

import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import org.exodusstudio.frostbite.common.entity.custom.elves.SummonerElfEntity;

public class SummonerElfSummonGoal extends RangedAttackGoal {
    private final SummonerElfEntity elf;

    public SummonerElfSummonGoal(SummonerElfEntity elf, double speedModifier, int attackInterval, float attackRadius) {
        super(elf, speedModifier, attackInterval, attackRadius);
        this.elf = elf;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && elf.getTarget() != null && elf.getCooldownTicks() <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canUse() && elf.getTarget() != null && elf.isAttacking();
    }

    @Override
    public void start() {
        super.start();
        elf.setAggressive(true);
        elf.tryStartAttacking();
    }

    @Override
    public void stop() {
        super.stop();
        elf.setAggressive(false);
    }
}
