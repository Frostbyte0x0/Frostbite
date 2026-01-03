package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.ShamanModel;
import org.exodusstudio.frostbite.common.entity.client.states.StateRenderState;
import org.exodusstudio.frostbite.common.entity.custom.shaman.ShamanEntity;

public class ShamanRenderer extends MobRenderer<ShamanEntity, StateRenderState, ShamanModel> {
    public ShamanRenderer(EntityRendererProvider.Context context) {
        super(context, new ShamanModel(context.bakeLayer(ModModelLayers.SHAMAN)), 0.45f);
    }

    @Override
    public StateRenderState createRenderState() {
        return new StateRenderState();
    }

    @Override
    public void extractRenderState(ShamanEntity guard, StateRenderState state, float p_361157_) {
        super.extractRenderState(guard, state, p_361157_);
        state.currentAnimationState.copyFrom(guard.currentAnimationState);
        state.lastAnimationState.copyFrom(guard.lastAnimationState);
        state.ticksSinceLastChange = guard.getTicksSinceLastChange();
        state.currentState = guard.getState();
        state.lastState = guard.getLastState();
    }

    @Override
    public Identifier getTextureLocation(StateRenderState renderState) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/shaman/shaman.png");
    }
}
