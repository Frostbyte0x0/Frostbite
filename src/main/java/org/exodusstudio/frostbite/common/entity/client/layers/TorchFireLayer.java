package org.exodusstudio.frostbite.common.entity.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.common.entity.client.models.TorchModel;
import org.exodusstudio.frostbite.common.entity.client.states.TorchRenderState;

public class TorchFireLayer extends RenderLayer<TorchRenderState, TorchModel> {
    public TorchFireLayer(RenderLayerParent<TorchRenderState, TorchModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack stack, SubmitNodeCollector submitNodeCollector, int i, TorchRenderState state, float yRot, float xRot) {
        ItemStackRenderState itemstackrenderstate = state.heldItem;
        if (!itemstackrenderstate.isEmpty()) {
            stack.pushPose();

            Vec3 vec3 = new Vec3(
                    state.x,
                    state.y,
                    state.z
            ).subtract(Minecraft.getInstance().player.getPosition(state.partialTick));
            Vec3 n = vec3.normalize();

            stack.translate(0, 0.75, 0.1);
            stack.mulPose(Axis.XP.rotationDegrees(180));
            stack.mulPose(Axis.YP.rotationDegrees(state.bodyRot));
            stack.translate(n.x, 0, n.z);
            stack.mulPose(Axis.YP.rotationDegrees(90));
            stack.mulPose(Axis.YP.rotationDegrees((float) (Math.atan2(vec3.z, vec3.x) * (-180 / Math.PI))));
            stack.scale(7, 7, 7);

            itemstackrenderstate.submit(stack, submitNodeCollector, i, OverlayTexture.NO_OVERLAY, 0);
            stack.popPose();
        }
    }
}
