package org.exodusstudio.frostbite.common.rendering;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Optional;
import java.util.OptionalDouble;

public class RenderToolkit {
    public static void renderSphere(PoseStack.Pose pose, VertexConsumer buffer, float radius, int d) {
        renderSphere(buffer, pose, radius, d, d, 0.0f, 0.0f, 1f, 1.0f, true);
        renderSphere(buffer, pose, radius + 0.001f, d, d, 0.0f, 0.0f, 1f, 1.0f, false);
    }

    public static void renderQuad(
            PoseStack.Pose pose,
            VertexConsumer buffer,
            Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3,
            float u0, float v0,
            float u1, float v1,
            boolean inside
    ) {
        vertex(buffer, pose, u0, v1, p0, inside);
        vertex(buffer, pose, u1, v1, p1, inside);
        vertex(buffer, pose, u1, v0, p2, inside);
        vertex(buffer, pose, u0, v0, p3, inside);
    }

    public static void renderSphere(
            VertexConsumer buffer,
            PoseStack.Pose pose,
            float radius,
            int rows,
            int columns,
            float u0, float v0,
            float u1, float v1,
            boolean inside
    ) {
        float endU = Mth.PI * 2 * u1;
        float endV = Mth.PI * v1;
        float dU = (endU - u0) / rows;
        float dV = (endV - v0) / columns;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                float u = i * dU + u0;
                float v = j * dV + v0;
                float uo = (i + 1 == rows) ? endU : (i + 1) * dU + u0;
                float vo = (j + 1 == columns) ? endV : (j + 1) * dV + v0;

                Vector3f p0 = spherePoint(u, v, radius);
                Vector3f p1 = spherePoint(u, vo, radius);
                Vector3f p2 = spherePoint(uo, v, radius);
                Vector3f p3 = spherePoint(uo, vo, radius);

                float textureU = u / endU;
                float textureV = v / endV;
                float textureUN = uo / endU;
                float textureVN = vo / endV;
                renderQuad(pose, buffer, p0, p1, p3, p2, textureV, textureUN, textureVN, textureU, inside);
            }
        }
    }

    public static void renderSkybox(
            SkyBoxSetting setting,
            VertexConsumer bb,
            float radius,
            PoseStack.Pose pose
    ) {
        int g = Math.max(4, setting.gradation());
        float PI = (float) Math.PI;

        TextureAtlasSprite sprite = celestialsAtlas.getSprite(setting.texture());

        for (int i = 0; i < g; i++) {
            float alpha1 = i * PI / g;
            float alpha2 = (i + 1) * PI / g;

            for (int j = 0; j < g * 2; j++) {
                float beta1 = j * 2 * PI / (g * 2);
                float beta2 = (j + 1) * 2 * PI / (g * 2);

                float x1 = radius * (float)(Math.sin(alpha1) * Math.cos(beta1));
                float y1 = radius * (float)(Math.sin(alpha1) * Math.sin(beta1));
                float z1 = radius * (float) Math.cos(alpha1);

                float x2 = radius * (float)(Math.sin(alpha1) * Math.cos(beta2));
                float y2 = radius * (float)(Math.sin(alpha1) * Math.sin(beta2));

                float x3 = radius * (float)(Math.sin(alpha2) * Math.cos(beta1));
                float y3 = radius * (float)(Math.sin(alpha2) * Math.sin(beta1));
                float z3 = radius * (float) Math.cos(alpha2);

                float x4 = radius * (float)(Math.sin(alpha2) * Math.cos(beta2));
                float y4 = radius * (float)(Math.sin(alpha2) * Math.sin(beta2));

//                float u1 = sprite.getU(beta1 / (2 * PI));
//                float u2 = sprite.getU(beta2 / (2 * PI));
//                float v1 = sprite.getV(alpha1 / PI);
//                float v2 = sprite.getV(alpha2 / PI);
                float u1 = beta1 / (2 * PI);
                float u2 = beta2 / (2 * PI);
                float v1 = alpha1 / PI;
                float v2 = alpha2 / PI;

                vertex(bb, pose, u1, v1, new Vector3f(x1, y1, z1), true);
                vertex(bb, pose, u2, v1, new Vector3f(x2, y2, z1), true);
                vertex(bb, pose, u2, v2, new Vector3f(x4, y4, z3), true);
                vertex(bb, pose, u1, v2, new Vector3f(x3, y3, z3), true);

//                bb.addVertex(x1, y1, z1).setUv(u1, v1);
//                bb.addVertex(x2, y2, z1).setUv(u2, v1);
//                bb.addVertex(x4, y4, z3).setUv(u2, v2);
//                bb.addVertex(x3, y3, z3).setUv(u1, v2);
            }
        }
    }

    public record SkyBoxSetting(int gradation, Identifier texture, Vector3f rotation, Optional<Rotation> dynamicRotation) {}
    public static final RenderPipeline CELESTIAL_NO_BLEND;
    public static final TextureAtlas celestialsAtlas =
            Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.CELESTIALS);

    public static void renderSkybox(PoseStack poseStack, SkyBoxSetting skyBoxSetting, GpuBuffer skyboxBuffer, int skyboxIndexCount) {
        if (skyBoxSetting == null || skyboxBuffer == null || skyboxIndexCount == 0) return;

        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(skyBoxSetting.rotation().x));
        poseStack.mulPose(Axis.YP.rotationDegrees(skyBoxSetting.rotation().y));
        poseStack.mulPose(Axis.ZP.rotationDegrees(skyBoxSetting.rotation().z));

        Matrix4fStack matrix4fStack = RenderSystem.getModelViewStack();
        matrix4fStack.pushMatrix();
        matrix4fStack.mul(poseStack.last().pose());

        RenderSystem.AutoStorageIndexBuffer quadIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
        GpuBuffer indexBuffer = quadIndices.getBuffer(skyboxIndexCount);

        GpuBufferSlice dynamicSlice = RenderSystem.getDynamicUniforms()
                .writeTransform(matrix4fStack, new Vector4f(1f, 1f, 1f, 1f), new Vector3f(), new Matrix4f());

        GpuTextureView colorView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTextureView();
        GpuTextureView depthView = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTextureView();

        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder()
                .createRenderPass(() -> "Skybox", colorView, Optional.empty(), depthView, OptionalDouble.empty())) {
            renderPass.setPipeline(CELESTIAL_NO_BLEND);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicSlice);
            renderPass.bindTexture("Sampler0", celestialsAtlas.getTextureView(), celestialsAtlas.getSampler());
            renderPass.setVertexBuffer(0, skyboxBuffer.slice());
            renderPass.setIndexBuffer(indexBuffer, quadIndices.type());
            renderPass.drawIndexed(0, 0, skyboxIndexCount, 1, 0);
        }

        matrix4fStack.popMatrix();
        poseStack.popPose();
    }

    public record SkyboxMesh(GpuBuffer buffer, int indexCount) {}

    public static SkyboxMesh buildSkyboxMesh(SkyBoxSetting setting, TextureAtlas atlas) {
        int g = Math.max(4, setting.gradation());
        int totalCells = g * (g * 2);
        int totalVertices = totalCells * 4;
        float PI = (float) Math.PI;

        TextureAtlasSprite sprite = atlas.getSprite(setting.texture());

        try (ByteBufferBuilder bbb = ByteBufferBuilder.exactlySized(
                DefaultVertexFormat.POSITION_TEX.getVertexSize() * totalVertices)) {
            BufferBuilder bb = new BufferBuilder(bbb, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION_TEX);

            for (int i = 0; i < g; i++) {
                float alpha1 = i * PI / g;
                float alpha2 = (i + 1) * PI / g;

                for (int j = 0; j < g * 2; j++) {
                    float beta1 = j * 2 * PI / (g * 2);
                    float beta2 = (j + 1) * 2 * PI / (g * 2);

                    float x1 = 100f * (float)(Math.sin(alpha1) * Math.cos(beta1));
                    float y1 = 100f * (float)(Math.sin(alpha1) * Math.sin(beta1));
                    float z1 = 100f * (float) Math.cos(alpha1);

                    float x2 = 100f * (float)(Math.sin(alpha1) * Math.cos(beta2));
                    float y2 = 100f * (float)(Math.sin(alpha1) * Math.sin(beta2));

                    float x3 = 100f * (float)(Math.sin(alpha2) * Math.cos(beta1));
                    float y3 = 100f * (float)(Math.sin(alpha2) * Math.sin(beta1));
                    float z3 = 100f * (float) Math.cos(alpha2);

                    float x4 = 100f * (float)(Math.sin(alpha2) * Math.cos(beta2));
                    float y4 = 100f * (float)(Math.sin(alpha2) * Math.sin(beta2));

                    float u1 = sprite.getU(beta1 / (2 * PI));
                    float u2 = sprite.getU(beta2 / (2 * PI));
                    float v1 = sprite.getV(alpha1 / PI);
                    float v2 = sprite.getV(alpha2 / PI);

                    bb.addVertex(x1, y1, z1).setUv(u1, v1);
                    bb.addVertex(x2, y2, z1).setUv(u2, v1);
                    bb.addVertex(x4, y4, z3).setUv(u2, v2);
                    bb.addVertex(x3, y3, z3).setUv(u1, v2);
                }
            }

            try (MeshData mesh = bb.buildOrThrow()) {
                int indexCount = mesh.drawState().indexCount();
                GpuBuffer gpuBuffer = RenderSystem.getDevice().createBuffer(
                        () -> "Skybox sphere", 40, mesh.vertexBuffer());
                return new SkyboxMesh(gpuBuffer, indexCount);
            }
        }
    }

    static {
        CELESTIAL_NO_BLEND = RenderPipelines.register(RenderPipeline.builder()
                .withLocation("pipeline/skyaesthetics_celestial_blend")
                .withVertexShader("core/position_tex")
                .withFragmentShader("core/position_tex")
                .withPrimitiveTopology(PrimitiveTopology.QUADS)
                .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX)
                .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
//                .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
                .build());
    }

    public static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float textureU, float textureV, Vector3f p, boolean inside) {
        buffer.addVertex(pose, p.x(), p.y(), p.z());
        buffer.setUv(textureU, textureV);
        buffer.setLineWidth(1.0f);
        buffer.setColor(1, 1, 1, 1f);
        buffer.setLight(0xFFFFFF);
        buffer.setOverlay(OverlayTexture.NO_OVERLAY);
        buffer.setNormal(pose, 0.0f, !inside ? -1.0f : 1.0f, 0.0f);
    }

    public static Vector3f spherePoint(float u, float v, float radius) {
        return new Vector3f(
                Mth.cos(u) * Mth.sin(v) * radius,
                Mth.cos(v) * radius,
                Mth.sin(u) * Mth.sin(v) * radius
        );
    }
}
