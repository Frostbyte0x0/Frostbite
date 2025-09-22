package org.exodusstudio.frostbite.common.entity.custom.goals;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import org.exodusstudio.frostbite.common.util.Ownable;

public class OwnerHurtTargetGoal extends TargetGoal {
    private final Monster monster;
    private LivingEntity ownerLastHurt;
    private int timestamp;

    public OwnerHurtTargetGoal(Monster monster) {
        super(monster, false);
        this.monster = monster;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        LivingEntity livingentity = ((Ownable) this.monster).getOwner();
        if (livingentity == null) {
            return false;
        } else {
            this.ownerLastHurt = livingentity.getLastHurtMob();
            int i = livingentity.getLastHurtMobTimestamp();
            return i != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT);
        }
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurt);
        LivingEntity livingentity = ((Ownable) this.monster).getOwner();
        if (livingentity != null) {
            this.timestamp = livingentity.getLastHurtMobTimestamp();
        }

        super.start();
    }
}
