package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.TorchModel;
import org.exodusstudio.frostbite.common.entity.client.states.TorchRenderState;
import org.exodusstudio.frostbite.common.entity.custom.TorchEntity;

public class TorchRenderer extends MobRenderer<TorchEntity, TorchRenderState, TorchModel> {
    public TorchRenderer(EntityRendererProvider.Context context) {
        super(context, new TorchModel(context.bakeLayer(ModModelLayers.TORCH)), 0.45f);
    }

    @Override
    public TorchRenderState createRenderState() {
        return new TorchRenderState();
    }

    @Override
    public void extractRenderState(TorchEntity torch, TorchRenderState state, float p_361157_) {
        super.extractRenderState(torch, state, p_361157_);
        state.stealingAnimationState.copyFrom(torch.slicingAnimationState);
        state.isSlicing = torch.isSlicing();
    }

    @Override
    public ResourceLocation getTextureLocation(TorchRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/torch/torch.png");
    }
}
