package org.exodusstudio.frostbite.common.entity.client.states;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class BorealBearRenderState extends LivingEntityRenderState {
    public final AnimationState attackAnimationState = new AnimationState();
    public boolean isAttacking;
}
