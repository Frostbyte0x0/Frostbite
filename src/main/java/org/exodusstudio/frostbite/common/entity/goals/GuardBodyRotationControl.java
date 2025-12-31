package org.exodusstudio.frostbite.common.entity.goals;

import net.minecraft.world.entity.ai.control.BodyRotationControl;
import org.exodusstudio.frostbite.common.entity.custom.guards.ChiefGuardEntity;

public class GuardBodyRotationControl extends BodyRotationControl {
    private final ChiefGuardEntity guard;

    public GuardBodyRotationControl(ChiefGuardEntity guard) {
        super(guard);
        this.guard = guard;
    }

    @Override
    public void clientTick() {
        if (guard.shouldFreezeRotation()) {
            guard.yBodyRot = guard.yHeadRot;
        } else {
            super.clientTick();
        }
    }
}
