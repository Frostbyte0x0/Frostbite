package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.animations.EtherealAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.EtherealAnimationState;

public class EtherealSwordModel extends EntityModel<EtherealAnimationState> {
    private final ModelPart sword;
    private final KeyframeAnimation hitAnimation;
    private float yRot = 0f;

    public EtherealSwordModel(ModelPart root) {
        super(root);
        this.sword = root.getChild("sword");
        this.hitAnimation = EtherealAnimations.SWORD_SLICE.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(14, 0).addBox(-1.0F, -6.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-3.0F, -21.0F, -0.5F, 6.0F, 15.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(EtherealAnimationState renderState) {
        super.setupAnim(renderState);
        yRot = 180 - renderState.yRot;
        sword.zRot += (float) Math.PI;
        hitAnimation.apply(renderState.animationState, renderState.ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        sword.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
