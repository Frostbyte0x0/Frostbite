package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.animations.BorealBearAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.BorealBearRenderState;

public class BorealBearModel extends QuadrupedModel<BorealBearRenderState> {
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart claws;
    private boolean isAttacking;
    private final KeyframeAnimation attackAnimation;

    public BorealBearModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.claws = root.getChild("claws");
        this.attackAnimation = BorealBearAnimations.ATTACK.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -3.0F, -3.0F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 44).addBox(-2.5F, 1.0F, -6.0F, 5.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(26, 0).addBox(2.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(26, 0).addBox(-4.5F, -4.0F, -1.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, -16.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 19).addBox(-5.0F, -13.0F, -7.0F, 14.0F, 14.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(39, 0).addBox(-4.0F, -25.0F, -7.0F, 12.0F, 12.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 9.0F, 12.0F, 1.5708F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(50, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 14.0F, 6.0F));
        partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(50, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, 14.0F, 6.0F));
        partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(50, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 14.0F, -8.0F));
        partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(50, 40).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 14.0F, -8.0F));

        PartDefinition claws = partdefinition.addOrReplaceChild("claws", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, -3.0F));
        claws.addOrReplaceChild("claw3_r1", CubeListBuilder.create().texOffs(23, 54).addBox(-0.6037F, -3.4163F, -23.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.6981F));
        claws.addOrReplaceChild("claw2_r1", CubeListBuilder.create().texOffs(23, 54).addBox(-0.6037F, -3.4163F, -23.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.6981F));
        claws.addOrReplaceChild("claw1_r1", CubeListBuilder.create().texOffs(23, 54).addBox(-0.6037F, -3.4163F, -23.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.6981F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(BorealBearRenderState state) {
        super.setupAnim(state);
        attackAnimation.apply(state.attackAnimationState, state.ageInTicks);
        isAttacking = state.isAttacking;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        head.render(poseStack, buffer, packedLight, packedOverlay, color);
        body.render(poseStack, buffer, packedLight, packedOverlay, color);
        leftHindLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
        rightHindLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
        leftFrontLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
        rightFrontLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
        if (isAttacking) {
            claws.render(poseStack, buffer, packedLight, packedOverlay, color);
        }
    }
}
