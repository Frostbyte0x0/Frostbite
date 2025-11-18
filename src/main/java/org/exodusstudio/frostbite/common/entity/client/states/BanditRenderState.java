package org.exodusstudio.frostbite.common.entity.client.states;

import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class BanditRenderState extends HoldingEntityRenderState {
    public final AnimationState stealingAnimationState = new AnimationState();
    public final AnimationState walkingAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState fleeingAnimationState = new AnimationState();
    public String currentState = "idle";
}
