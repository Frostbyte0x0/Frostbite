package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import org.exodusstudio.frostbite.common.entity.custom.elves.CasterElfEntity;

public class CasterElfCastGoal extends RangedAttackGoal {
    private final CasterElfEntity elf;

    public CasterElfCastGoal(CasterElfEntity elf, double speedModifier, int attackInterval, float attackRadius) {
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

    @Override
    public void tick() {
        super.tick();
        if (elf.getTarget() != null) {
            elf.getLookControl().setLookAt(elf.getTarget(), 30.0F, 30.0F);
            elf.lookAt(elf.getTarget(), 30.0F, 30.0F);
        }

    }
}
