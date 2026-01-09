package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.common.entity.client.states.StateRenderState;
import org.exodusstudio.frostbite.common.entity.custom.helper.StateMonsterEntity;

public class StateMonsterRenderer<E extends StateMonsterEntity, S extends StateRenderState, M extends HumanoidModel<S>> extends MobRenderer<E, S, M> {
    final Identifier texture;

    public StateMonsterRenderer(EntityRendererProvider.Context context, M model, float p_174306_, Identifier texture) {
        super(context, model, p_174306_);
        this.texture = texture;
    }

    @Override
    public Identifier getTextureLocation(S state) {
        return texture;
    }

    @Override
    public void extractRenderState(E e, S state, float p_361157_) {
        super.extractRenderState(e, state, p_361157_);
        state.currentAnimationState.copyFrom(e.currentAnimationState);
        state.lastAnimationState.copyFrom(e.lastAnimationState);
        state.ticksSinceLastChange = e.getTicksSinceLastChange();
        state.currentState = e.getCurrentState();
        state.lastState = e.getLastState();
    }

    @Override
    public S createRenderState() {
        return (S) new StateRenderState();
    }
}
