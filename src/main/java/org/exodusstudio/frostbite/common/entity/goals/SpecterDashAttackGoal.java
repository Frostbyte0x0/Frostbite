package org.exodusstudio.frostbite.common.entity.goals;

import org.exodusstudio.frostbite.common.entity.custom.ennemies.SpecterEntity;

public class SpecterDashAttackGoal extends FlyingChargeAttackGoal<SpecterEntity> {
    public SpecterDashAttackGoal(SpecterEntity monster, float speedModifier) {
        super(monster, speedModifier);
    }

    @Override
    public void start() {
        super.start();
        monster.setAttacking(true);
    }

    @Override
    public void stop() {
        super.stop();
        monster.setAttacking(false);
    }
}
