package org.exodusstudio.frostbite.common.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

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
