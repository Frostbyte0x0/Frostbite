package org.exodusstudio.frostbite.common.entity.goals;

import org.exodusstudio.frostbite.common.entity.custom.ennemies.BorealBearEntity;

public class BorealBearAttackGoal extends ApproachGoal {
    private final BorealBearEntity bear;

    public BorealBearAttackGoal(BorealBearEntity bear, double speedModifier) {
        super(bear, speedModifier);
        this.bear = bear;
    }

    @Override
    protected void action() {
        bear.setAttacking(bear.distanceToSqr(bear.getTarget()) < 4);
    }
}
