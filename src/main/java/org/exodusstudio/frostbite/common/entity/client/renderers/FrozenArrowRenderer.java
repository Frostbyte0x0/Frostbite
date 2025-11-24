package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.TippableArrowRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.custom.projectiles.FrozenArrow;

public class FrozenArrowRenderer extends ArrowRenderer<FrozenArrow, TippableArrowRenderState> {
    public FrozenArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    protected ResourceLocation getTextureLocation(TippableArrowRenderState p_362029_) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/frozen_arrow/frozen_arrow.png");
    }

    @Override
    public void render(TippableArrowRenderState state, PoseStack stack, MultiBufferSource source, int p_113824_) {
        super.render(state, stack, source, p_113824_);

        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90));
        stack.mulPose(Axis.ZP.rotationDegrees(state.xRot));
        stack.mulPose(Axis.XP.rotationDegrees(180));
        VertexConsumer vertexconsumer = source.getBuffer(RenderType.entityCutout(this.getTextureLocation(state)));
        this.model.setupAnim(state);
        this.model.renderToBuffer(stack, vertexconsumer, p_113824_, OverlayTexture.NO_OVERLAY);
        stack.popPose();
    }

    public TippableArrowRenderState createRenderState() {
        return new TippableArrowRenderState();
    }

    public void extractRenderState(FrozenArrow arrow, TippableArrowRenderState state, float color) {
        super.extractRenderState(arrow, state, color);
    }
}
