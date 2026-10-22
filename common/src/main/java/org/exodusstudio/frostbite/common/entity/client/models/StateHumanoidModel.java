package org.exodusstudio.frostbite.common.entity.client.models;

import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import org.exodusstudio.frostbite.common.entity.client.states.StateRenderState;
import org.exodusstudio.frostbite.common.entity.custom.guards.ChiefGuardEntity;
import org.exodusstudio.frostbite.common.util.Util;

import java.util.Map;

public class StateHumanoidModel<S extends StateRenderState> extends HumanoidModel<S> {
    protected Map<String, KeyframeAnimation> animations;

    public StateHumanoidModel(ModelPart root) {
        super(root);
    }

    protected void applyAnimation(S state) {
        if (state.ticksSinceLastChange < ChiefGuardEntity.BLEND_TICKS) {
            KeyframeAnimation currentAnimation = animations.get(state.currentState);
            KeyframeAnimation lastAnimation = animations.get(state.lastState);
            Util.blendAnimations(
                    state.ticksSinceLastChange,
                    ChiefGuardEntity.BLEND_TICKS,
                    state.partialTick,
                    state.ageInTicks,
                    lastAnimation, state.lastAnimationState,
                    currentAnimation, state.currentAnimationState);
        } else {
            animations.get(state.currentState).apply(state.currentAnimationState, state.ageInTicks);
        }
    }
}
