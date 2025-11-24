package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import org.exodusstudio.frostbite.common.entity.custom.elves.ElfEntity;
import org.exodusstudio.frostbite.common.entity.custom.elves.HealerElfEntity;

public class HealerElfHealGoal extends RangedAttackGoal {
    private final HealerElfEntity elf;

    public HealerElfHealGoal(HealerElfEntity elf, double speedModifier, int attackInterval, float attackRadius) {
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
        elf.tryStartAttacking();
        elf.setAggressive(true);
        elf.level().getEntitiesOfClass(ElfEntity.class, elf.getBoundingBox().inflate(20)).forEach(e -> {
            if (e != elf) {
                elf.alliesToHeal.add(e);
                e.setHealerPosition(elf.blockPosition());
            }
        });
    }

    @Override
    public void stop() {
        super.stop();
        elf.setAggressive(false);
        elf.alliesToHeal.forEach(e -> e.setHealerPosition(null));
        elf.alliesToHeal.clear();
    }
}
