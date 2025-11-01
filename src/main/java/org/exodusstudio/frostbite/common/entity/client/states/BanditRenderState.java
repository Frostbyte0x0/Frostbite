package org.exodusstudio.frostbite.common.entity.client.states;

import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class BanditRenderState extends HoldingEntityRenderState {
    public final AnimationState stealingAnimationState = new AnimationState();
}
