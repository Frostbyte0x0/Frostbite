package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.GuardModel;
import org.exodusstudio.frostbite.common.entity.client.states.StateRenderState;
import org.exodusstudio.frostbite.common.entity.custom.guards.GuardEntity;

public class GuardRenderer extends MobRenderer<GuardEntity, StateRenderState, GuardModel> {
    public GuardRenderer(EntityRendererProvider.Context context) {
        super(context, new GuardModel(context.bakeLayer(ModModelLayers.GUARD)), 0.45f);
    }

    @Override
    public StateRenderState createRenderState() {
        return new StateRenderState();
    }

    @Override
    public void extractRenderState(GuardEntity guard, StateRenderState state, float partialTick) {
        super.extractRenderState(guard, state, partialTick);
        state.currentAnimationState.copyFrom(guard.currentAnimationState);
        state.lastAnimationState.copyFrom(guard.lastAnimationState);
        state.ticksSinceLastChange = guard.getTicksSinceLastChange();
        state.currentState = guard.getCurrentState();
        state.lastState = guard.getLastState();
    }

    @Override
    public Identifier getTextureLocation(StateRenderState ignored) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/guard/guard.png");
    }
}
