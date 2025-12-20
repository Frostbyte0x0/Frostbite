package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.exodusstudio.frostbite.common.entity.client.states.EtherealRenderState;

public class EtherealRenderer<T extends Entity, M extends EntityModel<EtherealRenderState>> extends EntityRenderer<T, EtherealRenderState> {
    private final M model;

    public EtherealRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = createModel(context);
    }

    public M createModel(EntityRendererProvider.Context context) {
        return null;
    }

    @Override
    public void submit(EtherealRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();

        nodeCollector.submitModel(model, renderState, poseStack, RenderTypes.entityCutout(getTextureLocation()),
                renderState.lightCoords, 0, 0, null, renderState.outlineColor, null);
        poseStack.popPose();
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
    }

    public Identifier getTextureLocation() {
        return null;
    }

    @Override
    public EtherealRenderState createRenderState() {
        return new EtherealRenderState();
    }
}
