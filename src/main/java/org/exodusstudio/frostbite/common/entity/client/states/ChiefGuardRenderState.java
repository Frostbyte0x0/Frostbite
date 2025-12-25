package org.exodusstudio.frostbite.common.entity.client.states;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.AnimationState;
import org.exodusstudio.frostbite.common.entity.custom.guards.ChiefGuardEntity;

public class ChiefGuardRenderState extends HumanoidRenderState {
    public final AnimationState currentAnimationState = new AnimationState();
    public final AnimationState lastAnimationState = new AnimationState();
    public String currentState = "idle";
    public String lastState = "idle";
    public int ticksSinceLastChange = ChiefGuardEntity.BLEND_TICKS;
}
