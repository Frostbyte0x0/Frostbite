package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.ChiefGuardModel;
import org.exodusstudio.frostbite.common.entity.client.states.GuardRenderState;
import org.exodusstudio.frostbite.common.entity.custom.guards.ChiefGuardEntity;

public class ChiefGuardRenderer extends MobRenderer<ChiefGuardEntity, GuardRenderState, ChiefGuardModel> {
    public ChiefGuardRenderer(EntityRendererProvider.Context context) {
        super(context, new ChiefGuardModel(context.bakeLayer(ModModelLayers.CHIEF_GUARD)), 0.45f);
    }

    @Override
    public GuardRenderState createRenderState() {
        return new GuardRenderState();
    }

    @Override
    public void extractRenderState(ChiefGuardEntity guard, GuardRenderState state, float p_361157_) {
        super.extractRenderState(guard, state, p_361157_);
        state.currentAnimationState.copyFrom(guard.currentAnimationState);
        state.lastAnimationState.copyFrom(guard.lastAnimationState);
        state.ticksSinceLastChange = guard.getTicksSinceLastChange();
        state.currentState = guard.getCurrentState();
        state.lastState = guard.getLastState();
    }

    @Override
    public Identifier getTextureLocation(GuardRenderState renderState) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/chief_guard/chief_guard.png");
    }
}
