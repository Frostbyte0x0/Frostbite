package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.BanditHeldItemLayer;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.BanditModel;
import org.exodusstudio.frostbite.common.entity.client.states.BanditRenderState;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.BanditEntity;

public class BanditRenderer extends MobRenderer<BanditEntity, BanditRenderState, BanditModel> {
    public BanditRenderer(EntityRendererProvider.Context context) {
        super(context, new BanditModel(context.bakeLayer(ModModelLayers.BANDIT)), 0.45f);
        this.addLayer(new BanditHeldItemLayer(this));
    }

    @Override
    public BanditRenderState createRenderState() {
        return new BanditRenderState();
    }

    @Override
    public void extractRenderState(BanditEntity bandit, BanditRenderState state, float p_361157_) {
        super.extractRenderState(bandit, state, p_361157_);
        HoldingEntityRenderState.extractHoldingEntityRenderState(bandit, state, itemModelResolver);
        state.currentAnimationState.copyFrom(bandit.currentAnimationState);
        state.lastAnimationState.copyFrom(bandit.lastAnimationState);
        state.ticksSinceLastChange = bandit.getTicksSinceLastChange();
        state.currentState = bandit.getCurrentState();
        state.lastState = bandit.getLastState();
    }

    @Override
    public ResourceLocation getTextureLocation(BanditRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/bandit/bandit.png");
    }
}
