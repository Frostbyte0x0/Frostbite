package org.exodusstudio.frostbite.common.entity.client.states;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class EtherealRenderState extends EntityRenderState {
    public final AnimationState animationState = new AnimationState();
    public float yRot;
}
