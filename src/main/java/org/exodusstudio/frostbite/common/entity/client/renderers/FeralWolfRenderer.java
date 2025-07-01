package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.FeralWolfModel;
import org.exodusstudio.frostbite.common.entity.client.states.FeralWolfRenderState;
import org.exodusstudio.frostbite.common.entity.client.states.LevitatingJellyfishRenderState;
import org.exodusstudio.frostbite.common.entity.custom.FeralWolfEntity;
import org.exodusstudio.frostbite.common.entity.custom.LevitatingJellyfishEntity;

public class FeralWolfRenderer extends MobRenderer<FeralWolfEntity, FeralWolfRenderState, FeralWolfModel> {
    public FeralWolfRenderer(EntityRendererProvider.Context context) {
        super(context, new FeralWolfModel(context.bakeLayer(ModModelLayers.FERAL_WOLF)), 0.45f);
    }

    @Override
    public FeralWolfRenderState createRenderState() {
        return new FeralWolfRenderState();
    }

    @Override
    public void extractRenderState(FeralWolfEntity feralWolf, FeralWolfRenderState state, float p_363384_) {
        super.extractRenderState(feralWolf, state, p_363384_);
        state.isFrozen = feralWolf.isFrozen();
    }

    @Override
    public void render(FeralWolfRenderState state, PoseStack stack, MultiBufferSource bufferSource, int p_115313_) {
        super.render(state, stack, bufferSource, p_115313_);

    }

    @Override
    protected void scale(FeralWolfRenderState state, PoseStack stack) {
        super.scale(state, stack);
        float scale = 1.5f;
        stack.scale(scale, scale, scale);
    }

    @Override
    public ResourceLocation getTextureLocation(FeralWolfRenderState renderState) {
        return renderState.isFrozen ? ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/feral_wolf/frozen_feral_wolf.png") :
               ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/feral_wolf/feral_wolf.png");
    }
}
