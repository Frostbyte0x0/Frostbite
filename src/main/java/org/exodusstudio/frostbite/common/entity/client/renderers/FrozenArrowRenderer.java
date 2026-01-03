package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.TippableArrowRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.custom.projectiles.FrozenArrow;

public class FrozenArrowRenderer extends ArrowRenderer<FrozenArrow, TippableArrowRenderState> {
    public FrozenArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    protected Identifier getTextureLocation(TippableArrowRenderState p_362029_) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/frozen_arrow/frozen_arrow.png");
    }

    @Override
    public void submit(TippableArrowRenderState state, PoseStack stack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        super.submit(state, stack, nodeCollector, cameraRenderState);

        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90));
        stack.mulPose(Axis.ZP.rotationDegrees(state.xRot));
        stack.mulPose(Axis.XP.rotationDegrees(180));
        nodeCollector.submitModel(model, state, stack, RenderTypes.entityCutout(getTextureLocation(state)),
                state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
        stack.popPose();
    }

    public TippableArrowRenderState createRenderState() {
        return new TippableArrowRenderState();
    }

    public void extractRenderState(FrozenArrow arrow, TippableArrowRenderState state, float color) {
        super.extractRenderState(arrow, state, color);
    }
}
