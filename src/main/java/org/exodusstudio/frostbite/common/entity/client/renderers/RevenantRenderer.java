package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.RevenantModel;
import org.exodusstudio.frostbite.common.entity.client.states.RevenantRenderState;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.RevenantEntity;
import org.jetbrains.annotations.Nullable;

public class RevenantRenderer extends HumanoidMobRenderer<RevenantEntity, RevenantRenderState, RevenantModel> {
    private final Identifier REVENANT_TEXTURE =
            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/revenant/revenant.png");

    public RevenantRenderer(EntityRendererProvider.Context context) {
        super(context, new RevenantModel(context.bakeLayer(ModModelLayers.REVENANT)), 0.45f);
    }

    @Override
    public void submit(RevenantRenderState state, PoseStack stack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        if (state.isRecovering) {
            stack.mulPose(Axis.ZN.rotationDegrees(180));
            stack.translate(0, -2, 0);
        }
        super.submit(state, stack, nodeCollector, cameraRenderState);
    }

    @Override
    public void extractRenderState(RevenantEntity revenant, RevenantRenderState state, float p_363123_) {
        super.extractRenderState(revenant, state, p_363123_);
        state.isRising = revenant.isRising();
        state.isRecovering = revenant.isRecovering();
        state.risingAnimationState.copyFrom(revenant.risingAnimationState);
    }

    @Override
    protected @Nullable RenderType getRenderType(RevenantRenderState renderState, boolean isVisible, boolean renderTranslucent, boolean appearsGlowing) {
        return RenderTypes.entityTranslucent(REVENANT_TEXTURE);
    }

    @Override
    public Identifier getTextureLocation(RevenantRenderState renderState) {
        return REVENANT_TEXTURE;
    }

    @Override
    public RevenantRenderState createRenderState() {
        return new RevenantRenderState();
    }
}
