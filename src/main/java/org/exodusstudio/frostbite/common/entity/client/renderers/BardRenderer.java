package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.BardModel;
import org.exodusstudio.frostbite.common.entity.client.states.BardRenderState;
import org.exodusstudio.frostbite.common.entity.custom.bard.BardEntity;

public class BardRenderer extends MobRenderer<BardEntity, BardRenderState, BardModel> {
    public BardRenderer(EntityRendererProvider.Context context) {
        super(context, new BardModel(context.bakeLayer(ModModelLayers.BARD)), 0.45f);
    }

    @Override
    public BardRenderState createRenderState() {
        return new BardRenderState();
    }

    @Override
    public void extractRenderState(BardEntity bard, BardRenderState state, float p_361157_) {
        super.extractRenderState(bard, state, p_361157_);
        state.playAnimationState.copyFrom(bard.playAnimationState);
    }

    @Override
    public Identifier getTextureLocation(BardRenderState renderState) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/bard/bard.png");
    }
}
