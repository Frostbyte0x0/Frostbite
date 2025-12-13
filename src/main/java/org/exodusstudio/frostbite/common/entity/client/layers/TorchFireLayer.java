package org.exodusstudio.frostbite.common.entity.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.exodusstudio.frostbite.common.entity.client.models.TorchModel;
import org.exodusstudio.frostbite.common.entity.client.states.TorchRenderState;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TorchFireLayer extends RenderLayer<TorchRenderState, TorchModel> {
    public TorchFireLayer(RenderLayerParent<TorchRenderState, TorchModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, TorchRenderState state, float v, float v1) {
        Quaternionf quaternionf = new Quaternionf();
        quaternionf.rotationX((float) -Math.PI / 2);
        this.renderRotatedQuad(multiBufferSource.getBuffer(RenderType.translucentParticle(TextureAtlas.LOCATION_PARTICLES)),
                quaternionf, (float) state.x, (float) state.y, (float) state.z, v1);
        quaternionf.rotationX((float) Math.PI / 2);
        this.renderRotatedQuad(multiBufferSource.getBuffer(RenderType.translucentParticle(TextureAtlas.LOCATION_PARTICLES)),
                quaternionf, 0, 0, 0, v1);
    }

    protected void renderRotatedQuad(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float partialTicks) {
        float f = 10;
        float f1 = 0; // U0
        float f2 = 0.1f; // U1
        float f3 = 0; // V0
        float f4 = 0.1f; // V1
        int i = 15728880;
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, -1.0F, f, f2, f4, i);
        this.renderVertex(buffer, quaternion, x, y, z, 1.0F, 1.0F, f, f2, f3, i);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, 1.0F, f, f1, f3, i);
        this.renderVertex(buffer, quaternion, x, y, z, -1.0F, -1.0F, f, f1, f4, i);
    }

    private void renderVertex(VertexConsumer buffer, Quaternionf quaternion, float x, float y, float z, float xOffset, float yOffset, float quadSize, float u, float v, int packedLight) {
        Vector3f vector3f = (new Vector3f(xOffset, yOffset, 0.0F)).rotate(quaternion).mul(quadSize).add(x, y, z);
        buffer.addVertex(vector3f.x(), vector3f.y(), vector3f.z()).setUv(u, v).setColor(255, 255, 255, 255).setLight(packedLight);
    }
}
