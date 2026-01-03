package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.HeavyGuardModel;
import org.exodusstudio.frostbite.common.entity.client.states.StateRenderState;
import org.exodusstudio.frostbite.common.entity.custom.guards.HeavyGuardEntity;

public class HeavyGuardRenderer extends MobRenderer<HeavyGuardEntity, StateRenderState, HeavyGuardModel> {
    public HeavyGuardRenderer(EntityRendererProvider.Context context) {
        super(context, new HeavyGuardModel(context.bakeLayer(ModModelLayers.HEAVY_GUARD)), 0.45f);
    }

    @Override
    public StateRenderState createRenderState() {
        return new StateRenderState();
    }

    @Override
    public void extractRenderState(HeavyGuardEntity guard, StateRenderState state, float partialTick) {
        super.extractRenderState(guard, state, partialTick);
        state.currentAnimationState.copyFrom(guard.currentAnimationState);
        state.lastAnimationState.copyFrom(guard.lastAnimationState);
        state.ticksSinceLastChange = guard.getTicksSinceLastChange();
        state.currentState = guard.getCurrentState();
        state.lastState = guard.getLastState();
    }

    @Override
    protected void scale(StateRenderState renderState, PoseStack poseStack) {
        super.scale(renderState, poseStack);
        poseStack.scale(1.25f, 1.25f, 1.25f);
    }

    @Override
    public Identifier getTextureLocation(StateRenderState renderState) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/heavy_guard/heavy_guard.png");
    }
}
