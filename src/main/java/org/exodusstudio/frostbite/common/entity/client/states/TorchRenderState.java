package org.exodusstudio.frostbite.common.entity.client.states;

import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.world.entity.AnimationState;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.TorchEntity;

public class TorchRenderState extends HoldingEntityRenderState {
    public boolean isSlicing = false;
    public final AnimationState currentAnimationState = new AnimationState();
    public final AnimationState lastAnimationState = new AnimationState();
    public int ticksSinceLastChange = TorchEntity.BLEND_TICKS;
}
