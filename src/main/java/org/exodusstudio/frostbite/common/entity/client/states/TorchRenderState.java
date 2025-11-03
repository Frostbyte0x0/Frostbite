package org.exodusstudio.frostbite.common.entity.client.states;

import net.minecraft.client.renderer.entity.state.ZombieRenderState;
import net.minecraft.world.entity.AnimationState;

public class TorchRenderState extends ZombieRenderState {
    public final AnimationState stealingAnimationState = new AnimationState();
    public boolean isSlicing = false;
}
