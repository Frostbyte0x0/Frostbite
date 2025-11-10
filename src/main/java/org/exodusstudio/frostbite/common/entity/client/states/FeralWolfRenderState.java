package org.exodusstudio.frostbite.common.entity.client.states;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class FeralWolfRenderState extends LivingEntityRenderState {
    public final AnimationState bitingAnimationState = new AnimationState();
    public boolean isFrozen;
    public boolean isBiting;
}
