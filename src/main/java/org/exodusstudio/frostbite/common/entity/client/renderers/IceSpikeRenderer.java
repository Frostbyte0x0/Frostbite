package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.IceSpikeModel;
import org.exodusstudio.frostbite.common.entity.client.states.IceSpikeRenderState;
import org.exodusstudio.frostbite.common.entity.custom.IceSpikeEntity;

public class IceSpikeRenderer extends EntityRenderer<IceSpikeEntity, IceSpikeRenderState> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(
            Frostbite.MOD_ID, "textures/entity/ice_spike/ice_spike.png");
    private final IceSpikeModel model;

    public IceSpikeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new IceSpikeModel(context.bakeLayer(ModModelLayers.ICE_SPIKE));
    }

    public void render(IceSpikeRenderState state, PoseStack stack, MultiBufferSource source, int p_114533_) {
        float f = state.biteProgress;
        if (f != 0 && state.isRising) {
            stack.pushPose();
            stack.mulPose(Axis.YP.rotationDegrees(90.0F - state.yRot));
            stack.scale(-1.0F, -1.0F, 1.0F);
            stack.translate(0.0F, -1.501F, 0.0F);
            this.model.setupAnim(state);
            VertexConsumer vertexconsumer = source.getBuffer(this.model.renderType(TEXTURE_LOCATION));
            this.model.renderToBuffer(stack, vertexconsumer, p_114533_, OverlayTexture.NO_OVERLAY);
            stack.popPose();
        }

        if (!state.isRising) {
            stack.pushPose();
            stack.translate(0, 0.2, 0);
            stack.mulPose(Axis.YN.rotationDegrees(state.yRot));
            stack.mulPose(Axis.XN.rotationDegrees(90 - state.xRot));
            stack.translate(0, -1.2, 0);
            VertexConsumer vertexconsumer = source.getBuffer(this.model.renderType(TEXTURE_LOCATION));
            this.model.renderToBuffer(stack, vertexconsumer, p_114533_, OverlayTexture.NO_OVERLAY);
            stack.popPose();
        }

        super.render(state, stack, source, p_114533_);
    }

    public IceSpikeRenderState createRenderState() {
        return new IceSpikeRenderState();
    }

    public void extractRenderState(IceSpikeEntity spike, IceSpikeRenderState state, float p_363764_) {
        super.extractRenderState(spike, state, p_363764_);
        state.yRot = spike.getYRot(state.partialTick);
        state.xRot = spike.getXRot(state.partialTick);
        state.biteProgress = spike.getAnimationProgress(p_363764_);
        state.isRising = spike.isRising();
    }
}
