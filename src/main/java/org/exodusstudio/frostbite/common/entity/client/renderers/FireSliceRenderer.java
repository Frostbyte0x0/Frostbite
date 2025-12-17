package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.FireSliceModel;
import org.exodusstudio.frostbite.common.entity.client.states.FireSliceRenderState;
import org.exodusstudio.frostbite.common.entity.custom.projectiles.FireSliceEntity;

public class FireSliceRenderer extends EntityRenderer<FireSliceEntity, FireSliceRenderState> {
    protected final FireSliceModel model;

    public FireSliceRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new FireSliceModel(context.bakeLayer(ModModelLayers.FIRE_SLICE));
    }

    @Override
    public FireSliceRenderState createRenderState() {
        return new FireSliceRenderState();
    }

    @Override
    public void extractRenderState(FireSliceEntity entity, FireSliceRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.xRot = entity.getXRot();
        reusedState.yRot = entity.getYRot();
    }

    @Override
    public void submit(FireSliceRenderState state, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.scale(2, 2, 2);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.yRot + 180));
        poseStack.mulPose(Axis.XP.rotationDegrees(-state.xRot));
        poseStack.translate(0, -1.4, 0);
        VertexConsumer vertexconsumer = source.getBuffer(RenderTypes.entityCutout(this.getTextureLocation(state)));
        this.model.setupAnim(state);
        this.model.renderToBuffer(poseStack, vertexconsumer, state.lightCoords, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.submit(state, poseStack, nodeCollector, cameraRenderState);
    }

    public Identifier getTextureLocation(FireSliceRenderState renderState) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/fire_slice/fire_slice.png");
    }
}
