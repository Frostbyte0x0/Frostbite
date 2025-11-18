package org.exodusstudio.frostbite.common.entity.client.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.exodusstudio.frostbite.common.entity.client.models.BanditModel;
import org.exodusstudio.frostbite.common.entity.client.states.BanditRenderState;

public class BanditHeldItemLayer extends RenderLayer<BanditRenderState, BanditModel> {
    public BanditHeldItemLayer(RenderLayerParent<BanditRenderState, BanditModel> parent) {
        super(parent);
    }

    public void render(PoseStack stack, MultiBufferSource source, int p_117009_, BanditRenderState state, float p_117011_, float p_117012_) {
        ItemStackRenderState itemstackrenderstate = state.heldItem;
        if (!itemstackrenderstate.isEmpty()) {
            boolean flag1 = state.isBaby;
            stack.pushPose();

            //stack.translate(getParentModel().getHead().x / 16.0F, getParentModel().getHead().y / 16.0F + 1.2, getParentModel().getHead().z / 16.0F);
            stack.scale(0.75f, 0.75f, 0.75f);
            if (flag1) {
                stack.scale(0.75F, 0.75F, 0.75F);
            }

            //stack.mulPose(Axis.ZP.rotation(state.headRollAngle));
            stack.mulPose(Axis.YP.rotationDegrees(p_117011_));
            stack.mulPose(Axis.XP.rotationDegrees(p_117012_));
            if (state.isBaby) {
                stack.translate(0.06, 0.26, -0.5);
            } else {
                stack.translate(0.06, 0.27, -0.5);
            }

            stack.mulPose(Axis.XP.rotationDegrees(90));
            stack.translate(0, 0.1, 0);

            itemstackrenderstate.render(stack, source, p_117009_, OverlayTexture.NO_OVERLAY);
            stack.popPose();
        }

    }
}
