package org.exodusstudio.frostbite.common.entity.client.states;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class LevitatingJellyfishRenderState extends LivingEntityRenderState {
    public final AnimationState swimmingAnimationState = new AnimationState();
    public final AnimationState idleAnimationState = new AnimationState();
    public float xBodyRot;
    public float zBodyRot;
    public int moveCooldown;
}
