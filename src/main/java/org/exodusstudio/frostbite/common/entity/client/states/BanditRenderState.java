package org.exodusstudio.frostbite.common.entity.client.states;

import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.world.entity.AnimationState;
import org.exodusstudio.frostbite.common.entity.custom.BanditEntity;

public class BanditRenderState extends HoldingEntityRenderState {
    public final AnimationState currentAnimationState = new AnimationState();
    public final AnimationState lastAnimationState = new AnimationState();
    public String currentState = "idle";
    public String lastState = "idle";
    public int ticksSinceLastChange = BanditEntity.BLEND_TICKS;
}
