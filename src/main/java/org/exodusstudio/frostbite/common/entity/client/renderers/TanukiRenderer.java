package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.TanukiModel;
import org.exodusstudio.frostbite.common.entity.client.states.TanukiRenderState;
import org.exodusstudio.frostbite.common.entity.custom.TanukiEntity;

public class TanukiRenderer extends MobRenderer<TanukiEntity, TanukiRenderState, TanukiModel> {
    public TanukiRenderer(EntityRendererProvider.Context context) {
        super(context, new TanukiModel(context.bakeLayer(ModModelLayers.TANUKI)), 0.45f);
    }

    @Override
    public TanukiRenderState createRenderState() {
        return new TanukiRenderState();
    }

    @Override
    public void extractRenderState(TanukiEntity tanuki, TanukiRenderState state, float partialTick) {
        super.extractRenderState(tanuki, state, partialTick);
        state.isEating = tanuki.isEating();
        state.sitAmount = tanuki.getSitAmount(partialTick);
    }

    @Override
    protected void setupRotations(TanukiRenderState state, PoseStack stack, float bodyRot, float scale) {
        super.setupRotations(state, stack, bodyRot, scale);
        float f7 = state.sitAmount;
        if (f7 > 0.0F) {
            stack.translate(0.0F, 0.8F * f7, 0.0F);
            stack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(f7, state.xRot, state.xRot + 90.0F)));
            stack.translate(0.0F, -1.0F * f7, 0.0F);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(TanukiRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/tanuki/tanuki.png");
    }
}
