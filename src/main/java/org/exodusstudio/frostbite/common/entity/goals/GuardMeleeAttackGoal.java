package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.exodusstudio.frostbite.common.entity.custom.guards.GuardEntity;

public class GuardMeleeAttackGoal extends MeleeAttackGoal {
    GuardEntity guard;

    public GuardMeleeAttackGoal(GuardEntity mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
        this.guard = mob;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        if (this.canPerformAttack(target)) {
            guard.setAttacking();
            stop();
        }
    }

    @Override
    public boolean canUse() {
        return super.canUse() && guard.getRandom().nextFloat() < 0.05f && !guard.isAttacking() && !guard.isGuarding() && guard.getAttackCooldown() <= 0;
    }
}
