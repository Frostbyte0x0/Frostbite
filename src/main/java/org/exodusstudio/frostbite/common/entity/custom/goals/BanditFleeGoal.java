package org.exodusstudio.frostbite.common.entity.custom.goals;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.player.Player;
import org.exodusstudio.frostbite.common.entity.custom.BanditEntity;

public class BanditFleeGoal extends AvoidEntityGoal<Player> {
    private final BanditEntity bandit = (BanditEntity) mob;

    public BanditFleeGoal(BanditEntity mob, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier) {
        super(mob, Player.class, maxDistance, walkSpeedModifier, sprintSpeedModifier);
    }

    public void start() {
        super.start();
        bandit.setFleeing();
    }

    public void stop() {
        super.stop();
        bandit.setIdle();
    }

    @Override
    public void tick() {
        super.tick();
        if (path != null) {
            bandit.getLookControl().setLookAt(path.getNextNode().x, path.getNextNode().y, path.getNextNode().z, 30, 30);
        }
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !bandit.getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
    }
}
