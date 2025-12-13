package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
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
    public void render(FireSliceRenderState state, PoseStack stack, MultiBufferSource source, int i) {
        stack.pushPose();
        stack.scale(2, 2, 2);
        stack.mulPose(Axis.YP.rotationDegrees(-state.yRot + 180));
        stack.mulPose(Axis.XP.rotationDegrees(-state.xRot));
        stack.translate(0, -1.4, 0);
        VertexConsumer vertexconsumer = source.getBuffer(RenderType.entityCutout(this.getTextureLocation(state)));
        this.model.setupAnim(state);
        this.model.renderToBuffer(stack, vertexconsumer, i, OverlayTexture.NO_OVERLAY);
        stack.popPose();
        super.render(state, stack, source, i);
    }

    public ResourceLocation getTextureLocation(FireSliceRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/fire_slice/fire_slice.png");
    }
}
