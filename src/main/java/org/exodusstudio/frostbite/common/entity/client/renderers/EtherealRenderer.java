package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.exodusstudio.frostbite.common.entity.client.states.EtherealAnimationState;

public class EtherealRenderer<T extends Entity, M extends EntityModel<EtherealAnimationState>> extends EntityRenderer<T, EtherealAnimationState> {
    private final M model;

    public EtherealRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = createModel(context);
    }

    public M createModel(EntityRendererProvider.Context context) {
        return null;
    }

    public void render(EtherealAnimationState renderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_113824_) {
        poseStack.pushPose();
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderTypes.entityTranslucent(this.getTextureLocation()));
        this.model.setupAnim(renderState);
        this.model.renderToBuffer(poseStack, vertexconsumer, p_113824_, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.submit(renderState, poseStack, multiBufferSource, p_113824_);
    }

    public Identifier getTextureLocation() {
        return null;
    }

    @Override
    public EtherealAnimationState createRenderState() {
        return new EtherealAnimationState();
    }
}
