package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.BabyModelTransform;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.SheepRenderState;

import java.util.Set;

public class WoollySheepModel extends QuadrupedModel<SheepRenderState> {
    public static final MeshTransformer BABY_TRANSFORMER = new BabyModelTransform(false, 8.0F, 4.0F, 2.0F, 2.0F, 24.0F, Set.of("head"));

    private final ModelPart body;
    private final ModelPart rotation;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart head;
    private final ModelPart head2;
    private final ModelPart mirror;
    private final ModelPart nose;
    private final ModelPart right_horn;
    private final ModelPart left_horn;

    public WoollySheepModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.rotation = this.body.getChild("rotation");
        this.rightHindLeg = root.getChild("right_hind_leg");
        this.leftHindLeg = root.getChild("left_hind_leg");
        this.rightFrontLeg = root.getChild("right_front_leg");
        this.leftFrontLeg = root.getChild("left_front_leg");
        this.head = root.getChild("head");
        this.head2 = this.head.getChild("head2");
        this.mirror = this.head2.getChild("mirror");
        this.nose = this.head.getChild("nose");
        this.right_horn = this.head.getChild("right_horn");
        this.left_horn = this.head.getChild("left_horn");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 5.0F, 2.0F));

        PartDefinition rotation = body.addOrReplaceChild("rotation", CubeListBuilder.create().texOffs(20, 4).addBox(-6.0F, -12.0F, -10.0F, 12.0F, 20.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        PartDefinition rightHindLeg = partdefinition.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 12.0F, 7.0F));

        PartDefinition leftHindLeg = partdefinition.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 12.0F, 7.0F));

        PartDefinition rightFrontLeg = partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, 12.0F, -5.0F));

        PartDefinition leftFrontLeg = partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, 12.0F, -5.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head2 = head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(23, 52).addBox(-0.5F, -3.0F, -14.0F, 0.0F, 7.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(2, 61).addBox(-6.0F, -11.0F, -10.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -12.0F, -1.0F));

        PartDefinition mirror = head2.addOrReplaceChild("mirror", CubeListBuilder.create().texOffs(2, 61).mirror().addBox(2.5F, -21.0F, -10.0F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-0.5F, 10.0F, 0.0F));

        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(34, 46).addBox(-3.0F, -4.0F, -8.0F, 5.0F, 7.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, -20.0F, -9.0F, 0.9599F, 0.0F, 0.0F));

        PartDefinition right_horn = head.addOrReplaceChild("right_horn", CubeListBuilder.create().texOffs(12, 55).addBox(-2.99F, -16.0F, -10.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -12.0F, -1.0F));

        PartDefinition left_horn = head.addOrReplaceChild("left_horn", CubeListBuilder.create().texOffs(12, 55).addBox(-0.01F, -16.0F, -10.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -12.0F, -1.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    public void setupAnim(SheepRenderState p_360805_) {
        super.setupAnim(p_360805_);
        this.head.y += p_360805_.headEatPositionScale * 9.0F * p_360805_.ageScale;
        this.head.xRot = p_360805_.headEatAngleScale;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        rightHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        leftHindLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        rightFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        leftFrontLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
