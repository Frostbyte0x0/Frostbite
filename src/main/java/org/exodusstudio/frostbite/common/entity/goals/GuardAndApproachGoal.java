package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.exodusstudio.frostbite.common.entity.custom.guards.GuardEntity;

public class GuardAndApproachGoal extends MeleeAttackGoal {
    GuardEntity guard;

    public GuardAndApproachGoal(GuardEntity mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(mob, speedModifier, followingTargetEvenIfNotSeen);
        this.guard = mob;
    }

    @Override
    public void start() {
        super.start();
        guard.setGuarding();
    }

    @Override
    public boolean canUse() {
        return guard.getTarget() != null && guard.getRandom().nextFloat() < 0.1f && !guard.isAttacking() && !guard.isGuarding() && guard.getGuardCooldown() <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return guard.getTicksSinceLastChange() < 70 || guard.getRandom().nextFloat() > 0.025f;
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        if (this.canPerformAttack(target)) {
            stop();
        }
    }

    @Override
    public void stop() {
        super.stop();
        if (guard.getTarget() != null && guard.getTarget().distanceToSqr(guard) < 9) {
            guard.setAttacking();
        } else {
            guard.setIdle();
        }
    }
}
