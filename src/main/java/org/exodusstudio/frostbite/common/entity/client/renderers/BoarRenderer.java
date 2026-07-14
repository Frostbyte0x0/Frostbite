package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.BoarModel;
import org.exodusstudio.frostbite.common.entity.custom.animals.BoarEntity;
import org.exodusstudio.frostbite.common.rendering.RenderToolkit;

public class BoarRenderer extends MobRenderer<BoarEntity, LivingEntityRenderState, BoarModel> {
    public BoarRenderer(EntityRendererProvider.Context context) {
        super(context, new BoarModel(context.bakeLayer(ModModelLayers.BOAR)), 0.45f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState renderState) {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/boar/boar.png");
    }

    @Override
    public void submit(LivingEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/environement/celestial/space_blue.png")),
                (pose, buffer) -> {
                    RenderToolkit.renderSphere(pose, buffer, 5, 32);
                });

        poseStack.pushPose();
        poseStack.mulPose(camera.orientation);
        poseStack.scale(0.1f, -0.1f, 0.1f);

//        SubmitNodeStorage collector = new SubmitNodeStorage();
        submitNodeCollector.submitText(poseStack, -22, -18, Component.literal("this.text").getVisualOrderText(), false,
                Font.DisplayMode.POLYGON_OFFSET, 15728880,
                ARGB.colorFromFloat(1, 1, 1, 1), 0x00000000, 0x00000000);

//        Minecraft.getInstance().gameRenderer.featureRenderDispatcher().renderAllFeatures(collector);
        poseStack.popPose();

        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
