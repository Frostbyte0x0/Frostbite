package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.BanditEntity;

public class BanditStrollGoal extends WaterAvoidingRandomStrollGoal {
    BanditEntity bandit;

    public BanditStrollGoal(BanditEntity bandit, double p_25988_) {
        super(bandit, p_25988_, 0.001f);
        this.bandit = bandit;
    }

    @Override
    public void start() {
        super.start();
        bandit.setWalking();
    }

    @Override
    public void tick() {
        super.tick();
        bandit.getLookControl().setLookAt(wantedX, wantedY, wantedZ, 30, 30);
    }

    @Override
    public void stop() {
        super.stop();
        bandit.setIdle();
    }
}
