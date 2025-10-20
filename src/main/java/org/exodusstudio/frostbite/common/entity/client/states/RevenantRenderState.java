package org.exodusstudio.frostbite.common.entity.client.states;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.AnimationState;

public class RevenantRenderState extends HumanoidRenderState {
    public boolean isAggressive;
    public boolean isRising;
    public boolean isRecovering;
    public final AnimationState risingAnimationState = new AnimationState();
}
