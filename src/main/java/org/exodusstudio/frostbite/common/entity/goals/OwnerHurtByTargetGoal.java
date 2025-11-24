package org.exodusstudio.frostbite.common.entity.goals;

import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import org.exodusstudio.frostbite.common.util.Ownable;

public class OwnerHurtByTargetGoal extends TargetGoal {
    private final Monster monster;
    private LivingEntity ownerLastHurtBy;
    private int timestamp;

    public OwnerHurtByTargetGoal(Monster monster) {
        super(monster, false);
        this.monster = monster;
        this.setFlags(EnumSet.of(Flag.TARGET));
    }

    public boolean canUse() {
        LivingEntity livingentity = ((Ownable) this.monster).getOwner();
        if (livingentity == null) {
            return false;
        } else {
            this.ownerLastHurtBy = livingentity.getLastHurtByMob();
            int i = livingentity.getLastHurtByMobTimestamp();
            return i != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT);
        }
    }

    public void start() {
        this.mob.setTarget(this.ownerLastHurtBy);
        LivingEntity livingentity = ((Ownable) this.monster).getOwner();
        if (livingentity != null) {
            this.timestamp = livingentity.getLastHurtByMobTimestamp();
        }

        super.start();
    }
}
