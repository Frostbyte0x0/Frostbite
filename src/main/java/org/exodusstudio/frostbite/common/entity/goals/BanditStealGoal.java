package org.exodusstudio.frostbite.common.entity.goals;

import org.exodusstudio.frostbite.common.entity.custom.ennemies.BanditEntity;

public class BanditStealGoal extends ApproachGoal {
    private final BanditEntity bandit;

    public BanditStealGoal(BanditEntity bandit, double speedModifier) {
        super(bandit, speedModifier);
        this.bandit = bandit;
    }

    @Override
    public boolean canUse() {
        return bandit.getMainHandItem().isEmpty() && bandit.getTarget() != null;
    }

    @Override
    public void start() {
        super.start();
        bandit.setWalking();
    }

    @Override
    public void stop() {
        super.stop();
        bandit.setIdle();
    }

    @Override
    protected void action() {
        if (bandit.distanceToSqr(bandit.getTarget()) < 1.5) {
            bandit.setStealing();
        }
    }
}
