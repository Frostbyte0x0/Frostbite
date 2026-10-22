package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

import java.util.function.Consumer;

public class ActionStrollGoal extends WaterAvoidingRandomStrollGoal {
    final Consumer<PathfinderMob> startAction;
    final Consumer<PathfinderMob> stopAction;

    public ActionStrollGoal(PathfinderMob mob, double p_25988_, Consumer<PathfinderMob> startAction, Consumer<PathfinderMob> stopAction) {
        super(mob, p_25988_, 0.1f);
        this.startAction = startAction;
        this.stopAction = stopAction;
    }

    @Override
    public void start() {
        super.start();
        startAction.accept(mob);
    }

    @Override
    public void tick() {
        super.tick();
        mob.getLookControl().setLookAt(wantedX, mob.getEyeY(), wantedZ, 30, 30);
    }

    @Override
    public void stop() {
        super.stop();
        stopAction.accept(mob);
    }
}
