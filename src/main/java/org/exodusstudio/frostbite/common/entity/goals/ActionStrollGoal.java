package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class ActionStrollGoal extends WaterAvoidingRandomStrollGoal {
    final Consumer<PathfinderMob> startAction;
    final Consumer<PathfinderMob> stopAction;

    public ActionStrollGoal(PathfinderMob mob, double p_25988_, Consumer<PathfinderMob> startAction, Consumer<PathfinderMob> stopAction) {
        super(mob, p_25988_, 0.1f);
        this.startAction = startAction;
        this.stopAction = stopAction;
    }

    public boolean canUse() {
        if (this.mob.hasControllingPassenger()) {
            return false;
        } else {
            if (!this.forceTrigger) {
                if (this.mob.getNoActionTime() >= 100) {
                    return false;
                }

                if (this.mob.getRandom().nextInt(reducedTickDelay(this.interval)) != 0) {
                    return false;
                }
            }

            Vec3 vec3 = this.getPosition();
            if (vec3 == null) {
                return false;
            } else {
                this.wantedX = vec3.x;
                this.wantedY = vec3.y;
                this.wantedZ = vec3.z;
                this.forceTrigger = false;
                return true;
            }
        }
    }

    @Override
    public void start() {
        super.start();
        startAction.accept(mob);
    }

    @Override
    public void tick() {
        super.tick();
        mob.getLookControl().setLookAt(wantedX, wantedY + 1.5, wantedZ, 30, 30);
    }

    @Override
    public void stop() {
        super.stop();
        stopAction.accept(mob);
    }
}
