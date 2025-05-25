package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EvokerFangsRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.IceSpikeModel;
import org.exodusstudio.frostbite.common.entity.custom.IceSpikeEntity;

public class IceSpikeRenderer extends EntityRenderer<IceSpikeEntity, EvokerFangsRenderState> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(
            Frostbite.MOD_ID, "textures/entity/ice_spike/ice_spike.png");
    private final IceSpikeModel model;

    public IceSpikeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new IceSpikeModel(context.bakeLayer(ModModelLayers.ICE_SPIKE));
    }

    public void render(EvokerFangsRenderState p_363441_, PoseStack p_114531_, MultiBufferSource p_114532_, int p_114533_) {
        float f = p_363441_.biteProgress;
        if (f != 0.0F) {
            p_114531_.pushPose();
            p_114531_.mulPose(Axis.YP.rotationDegrees(90.0F - p_363441_.yRot));
            p_114531_.scale(-1.0F, -1.0F, 1.0F);
            p_114531_.translate(0.0F, -1.501F, 0.0F);
            this.model.setupAnim(p_363441_);
            VertexConsumer vertexconsumer = p_114532_.getBuffer(this.model.renderType(TEXTURE_LOCATION));
            this.model.renderToBuffer(p_114531_, vertexconsumer, p_114533_, OverlayTexture.NO_OVERLAY);
            p_114531_.popPose();
            super.render(p_363441_, p_114531_, p_114532_, p_114533_);
        }

    }

    public EvokerFangsRenderState createRenderState() {
        return new EvokerFangsRenderState();
    }

    public void extractRenderState(IceSpikeEntity p_360791_, EvokerFangsRenderState p_362754_, float p_363764_) {
        super.extractRenderState(p_360791_, p_362754_, p_363764_);
        p_362754_.yRot = p_360791_.getYRot();
        p_362754_.biteProgress = p_360791_.getAnimationProgress(p_363764_);
    }
}
