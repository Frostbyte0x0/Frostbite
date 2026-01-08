package org.exodusstudio.frostbite.common.entity.client.renderers.bullet;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.common.entity.custom.bullets.AbstractBullet;

public abstract class BulletRenderer<T extends AbstractBullet, S extends ArrowRenderState> extends EntityRenderer<T, S> {
    private final EntityModel<ArrowRenderState> model;

    protected BulletRenderer(EntityRendererProvider.Context context, EntityModel<ArrowRenderState> model) {
        super(context);
        this.model = model;
    }

    @Override
    public void submit(S renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(renderState.yRot - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.xRot));

        nodeCollector.submitModel(model, renderState, poseStack, RenderTypes.entityCutout(getTextureLocation(renderState)),
                renderState.lightCoords, 0, 0, null, renderState.outlineColor, null);
        poseStack.popPose();
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
    }

    protected abstract Identifier getTextureLocation(S ignored);

    public void extractRenderState(T p_361771_, S p_364204_, float p_360538_) {
        super.extractRenderState(p_361771_, p_364204_, p_360538_);
        p_364204_.xRot = p_361771_.getXRot(p_360538_);
        p_364204_.yRot = p_361771_.getYRot(p_360538_);
    }
}
