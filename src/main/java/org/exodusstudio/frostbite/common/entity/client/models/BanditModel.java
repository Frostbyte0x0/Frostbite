package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.animations.BanditAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.BanditRenderState;

public class BanditModel extends QuadrupedModel<BanditRenderState> {
    private final ModelPart all;
    private final KeyframeAnimation stealingAnimation;

    public BanditModel(ModelPart root) {
        super(root.getChild("all"));
        this.all = root.getChild("all");
        this.stealingAnimation = BanditAnimations.STEAL.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg1 = all.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(28, 27).addBox(-1.0F, -3.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 6.0F));

        PartDefinition leg2 = all.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -3.0F, 6.0F));

        PartDefinition leg3 = all.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(8, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -3.0F, -6.0F));

        PartDefinition leg4 = all.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(20, 27).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -3.0F, -6.0F));

        PartDefinition body = all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 10.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, -6.0F));

        PartDefinition head = all.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, -2.0F, -1.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 32).addBox(-2.99F, -3.5F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(30, 32).addBox(0.99F, -3.5F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 32).addBox(-1.0F, -0.01F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, -9.0F));

        PartDefinition tail = all.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(20, 20).addBox(-1.0F, -8.0F, 7.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(BanditRenderState state) {
        super.setupAnim(state);
//        this.head.xRot = state.xRot * ((float)Math.PI / 180F);
//        this.head.yRot = state.yRot * ((float)Math.PI / 180F);
//        float f = state.walkAnimationPos;
//        float f1 = state.walkAnimationSpeed;
//        this.rightHindLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * f1;
//        this.leftHindLeg.xRot = Mth.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
//        this.rightFrontLeg.xRot = Mth.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
//        this.leftFrontLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * f1;
        stealingAnimation.apply(state.stealingAnimationState, state.ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        all.render(poseStack, buffer, packedLight, packedOverlay, color);
    }

    public ModelPart getHead() {
        return this.head;
    }
}
