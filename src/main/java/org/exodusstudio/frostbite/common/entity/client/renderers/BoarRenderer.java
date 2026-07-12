package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.BoarModel;
import org.exodusstudio.frostbite.common.entity.custom.animals.BoarEntity;
import org.exodusstudio.frostbite.common.rendering.RenderToolkit;
import org.joml.Vector3f;

import java.util.Optional;

public class BoarRenderer extends MobRenderer<BoarEntity, LivingEntityRenderState, BoarModel> {
    static RenderToolkit.SkyBoxSetting skyBoxSetting = new RenderToolkit.SkyBoxSetting(
            25,
            Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/environement/celestial/space_blue.png"),
            new Vector3f(0, 0, 0),
            Optional.empty()
    );
    static RenderToolkit.SkyboxMesh mesh = RenderToolkit.buildSkyboxMesh(skyBoxSetting, RenderToolkit.celestialsAtlas);

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
//        SubmitNodeStorage collector = new SubmitNodeStorage();

        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/environement/celestial/space_blue.png")),
                (pose, buffer) -> {
//                    RenderToolkit.renderQuad(pose, buffer,
//                            new Vector3f(-0.5f, 0.0f, -0.5f),
//                            new Vector3f(0.5f, 0.0f, -0.5f),
//                            new Vector3f(0.5f, 0.0f, 0.5f),
//                            new Vector3f(-0.5f, 0.0f, 0.5f),
//                            0.0f, 0.0f, 1.0f, 1.0f);
//                    RenderToolkit.renderSphere(pose, buffer, 3.0f, 32);
                    RenderToolkit.renderSkybox(skyBoxSetting, buffer, 5, pose);
                });
        PoseStack skyboxPoseStack = new PoseStack();
        skyboxPoseStack.pushPose();
        skyboxPoseStack.translate(state.x, state.y, state.z);

        GpuBuffer skyboxBuffer = mesh.buffer();
        int skyboxIndexCount = mesh.indexCount();
//        RenderToolkit.renderSkybox(skyboxPoseStack, skyBoxSetting, skyboxBuffer, skyboxIndexCount);

        skyboxPoseStack.popPose();

        submitNodeCollector.submitCustomGeometry(poseStack, RenderType.create("thingy", RenderSetup.builder(RenderPipelines.END_GATEWAY).withTexture("Sampler0", AbstractEndPortalRenderer.END_SKY_LOCATION).withTexture("Sampler1", AbstractEndPortalRenderer.END_PORTAL_LOCATION).createRenderSetup()),
                (pose, buffer) -> {/*RenderToolkit.renderSphere(pose, buffer, 2.999f, 32)*/});

        // Render the features through the dispatcher
//        Minecraft.getInstance().gameRenderer.featureRenderDispatcher().renderAllFeatures(collector);

        super.submit(state, poseStack, submitNodeCollector, camera);
    }
}
