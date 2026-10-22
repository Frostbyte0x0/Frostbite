package org.exodusstudio.frostbite.common.entity.goals;

import org.exodusstudio.frostbite.common.entity.custom.ennemies.FeralWolfEntity;

public class FeralWolfBiteGoal extends ApproachGoal {
    private final FeralWolfEntity feralWolf;

    public FeralWolfBiteGoal(FeralWolfEntity feralWolf, double speedModifier) {
        super(feralWolf, speedModifier);
        this.feralWolf = feralWolf;
    }

    @Override
    protected void action() {
        feralWolf.setBiting(feralWolf.distanceToSqr(feralWolf.getTarget()) < 4);
    }
}
