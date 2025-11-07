package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.states.ElfRenderState;

public class SummonerElfModel extends HumanoidModel<ElfRenderState> {
    public SummonerElfModel(ModelPart root) {
        super(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 46).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 32).addBox(-6.0F, -34.0F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(32, 46).addBox(-4.0F, -36.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(48, 40).addBox(-2.0F, -38.0F, -2.0F, 4.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 56).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(56, 56).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(0, 62).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition staff = right_arm.addOrReplaceChild("staff", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, 7.0F, -14.0F, 2.0F, 2.0F, 30.0F, new CubeDeformation(0.0F))
                .texOffs(48, 32).addBox(-3.0F, 6.0F, -18.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 62).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(64, 0).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(ElfRenderState state) {
        super.setupAnim(state);
        rightArm.xRot -= (float) (Math.PI / 5);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        head.render(poseStack, buffer, packedLight, packedOverlay, color);
        body.render(poseStack, buffer, packedLight, packedOverlay, color);
        leftArm.render(poseStack, buffer, packedLight, packedOverlay, color);
        rightArm.render(poseStack, buffer, packedLight, packedOverlay, color);
        leftLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
        rightLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
