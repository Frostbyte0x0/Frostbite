package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.Vec3;

public class FlyingMoveControl extends MoveControl {
    final Monster monster;
    
    public FlyingMoveControl(Monster monster) {
        super(monster);
        this.monster = monster;
    }

    public void tick() {
        if (this.operation == Operation.MOVE_TO) {
            Vec3 vec3 = new Vec3(this.wantedX - monster.getX(), this.wantedY - monster.getY(), this.wantedZ - monster.getZ());
            double d0 = vec3.length();
            if (d0 < monster.getBoundingBox().getSize()) {
                this.operation = Operation.WAIT;
                monster.setDeltaMovement(monster.getDeltaMovement().scale(0.5F));
            } else {
                monster.setDeltaMovement(monster.getDeltaMovement().add(vec3.scale(this.speedModifier * 0.1 / d0)));
                if (monster.getTarget() == null) {
                    Vec3 vec31 = monster.getDeltaMovement();
                    monster.setYRot(-((float) Mth.atan2(vec31.x, vec31.z)) * (180F / (float)Math.PI));
                } else {
                    double d2 = monster.getTarget().getX() - monster.getX();
                    double d1 = monster.getTarget().getZ() - monster.getZ();
                    monster.setYRot(-((float)Mth.atan2(d2, d1)) * (180F / (float)Math.PI));
                }
                monster.yBodyRot = monster.getYRot();
            }
        }
    }
}
