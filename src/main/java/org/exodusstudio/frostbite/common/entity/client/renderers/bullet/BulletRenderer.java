package org.exodusstudio.frostbite.common.entity.client.renderers.bullet;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.common.entity.custom.bullets.AbstractBullet;

public abstract class BulletRenderer<T extends AbstractBullet, S extends ArrowRenderState> extends EntityRenderer<T, S> {
    private final EntityModel<ArrowRenderState> model;

    protected BulletRenderer(EntityRendererProvider.Context context, EntityModel<ArrowRenderState> model) {
        super(context);
        this.model = model;
    }

    public void render(S renderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int p_113824_) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.xRot));
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderTypes.entityCutout(this.getTextureLocation(renderState)));
        this.model.setupAnim(renderState);
        this.model.renderToBuffer(poseStack, vertexconsumer, p_113824_, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.submit(renderState, poseStack, multiBufferSource, p_113824_);
    }

    protected abstract Identifier getTextureLocation(S var1);

    public void extractRenderState(T p_361771_, S p_364204_, float p_360538_) {
        super.extractRenderState(p_361771_, p_364204_, p_360538_);
        p_364204_.xRot = p_361771_.getXRot(p_360538_);
        p_364204_.yRot = p_361771_.getYRot(p_360538_);
    }
}
