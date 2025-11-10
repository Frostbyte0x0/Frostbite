package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.SheepRenderState;

public class WoollySheepFurModel extends QuadrupedModel<SheepRenderState> {
    private final ModelPart leftFrontLeg;
    private final ModelPart body;
    private final ModelPart rotation2;
    private final ModelPart head;
    private final ModelPart bone;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;

    public WoollySheepFurModel(ModelPart root) {
        super(root);
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.body = root.getChild("body");
        this.rotation2 = this.body.getChild("rotation2");
        this.head = root.getChild("head");
        this.bone = this.head.getChild("bone");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
    }

    public static LayerDefinition createFurLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 52).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(3.0F, 12.0F, -5.0F));
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 2.0F));
        body.addOrReplaceChild("rotation2", CubeListBuilder.create().texOffs(20, 36).addBox(-6.0F, -11.0F, -10.0F, 12.0F, 18.0F, 10.0F, new CubeDeformation(1.75F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));
        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -4.0F, -6.0F, 6.0F, 6.0F, 9.0F, new CubeDeformation(0.6F)), PartPose.offsetAndRotation(0.0F, 6.0F, -8.0F, -0.5672F, 0.0F, 0.0F));
        head.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 52).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-3.0F, 12.0F, 7.0F));
        partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 52).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(3.0F, 12.0F, 7.0F));
        partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 52).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.5F)), PartPose.offset(-3.0F, 12.0F, -5.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public void setupAnim(SheepRenderState p_365221_) {
        super.setupAnim(p_365221_);
        this.head.y += p_365221_.headEatPositionScale * 9.0F * p_365221_.ageScale;
        this.head.xRot = p_365221_.headEatAngleScale;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
        leftFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        rightHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        leftHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        rightFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
