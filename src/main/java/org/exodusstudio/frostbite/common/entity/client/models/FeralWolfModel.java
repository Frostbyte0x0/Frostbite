package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.common.entity.client.animations.FeralWolfAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.FeralWolfRenderState;

public class FeralWolfModel extends EntityModel<FeralWolfRenderState> {
    private final ModelPart head;
    private final ModelPart upperBody;
    private final ModelPart body;
    private final ModelPart rightHindLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart tail;
    private final ModelPart bite;
    private boolean isBiting;
    private final KeyframeAnimation bitingAnimation;

    public FeralWolfModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.upperBody = root.getChild("mane");
        this.body = root.getChild("body");
        this.rightHindLeg = root.getChild("leg1");
        this.leftHindLeg = root.getChild("leg2");
        this.rightFrontLeg = root.getChild("leg3");
        this.leftFrontLeg = root.getChild("leg4");
        this.tail = root.getChild("tail");
        this.bite = root.getChild("bite");
        this.bitingAnimation = FeralWolfAnimations.BITE.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(24, 13).addBox(-2.0F, -3.0F, -2.0F, 6.0F, 6.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(30, 10).addBox(2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(30, 10).addBox(-2.0F, -5.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 23).addBox(-0.5F, -0.02F, -5.0F, 3.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 13.5F, -7.0F));

        partdefinition.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -3.0F, 8.0F, 6.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 14.0F, -3.0F, 1.5708F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 13).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.0F, 2.0F, 1.5708F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(16, 28).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 16.0F, 7.0F));
        partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(16, 28).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 16.0F, 7.0F));
        partdefinition.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(16, 28).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, 16.0F, -4.0F));
        partdefinition.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(16, 28).addBox(0.0F, 0.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 16.0F, -4.0F));
        partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(30, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, 9.0F));

        PartDefinition bite = partdefinition.addOrReplaceChild("bite", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition upper = bite.addOrReplaceChild("upper", CubeListBuilder.create(), PartPose.offset(0.0F, -9.0F, -10.0F));
        upper.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 28).addBox(-3.1F, -1.1F, -1.01F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, -2.0F, -1.0F, 1.5708F, 0.7854F, -3.1416F));
        upper.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 28).addBox(-3.1F, -1.1F, -1.01F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, -2.0F, -2.0F, -1.5708F, 0.7854F, 0.0F));
        upper.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 28).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -4.0F, -1.5708F, 0.0F, 0.0F));
        PartDefinition lower = bite.addOrReplaceChild("lower", CubeListBuilder.create(), PartPose.offset(0.0F, -9.0F, -10.0F));
        lower.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 28).addBox(-3.0F, -1.0F, -1.01F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 2.0F, -1.0F, 1.5708F, 0.7854F, -3.1416F));
        lower.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 28).addBox(-3.0F, -1.0F, -1.01F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 2.0F, -2.0F, -1.5708F, 0.7854F, 0.0F));
        lower.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 28).addBox(-3.0F, -1.0F, -1.0F, 6.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -4.0F, -1.5708F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(FeralWolfRenderState state) {
        super.setupAnim(state);
        bitingAnimation.apply(state.bitingAnimationState, state.ageInTicks);

        float f = state.walkAnimationPos;
        float f1 = state.walkAnimationSpeed;

        this.tail.yRot = Mth.cos(f * 0.6662F) * 1.4F * f1;

        this.rightHindLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * f1;
        this.leftHindLeg.xRot = Mth.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        this.rightFrontLeg.xRot = Mth.cos(f * 0.6662F + (float)Math.PI) * 1.4F * f1;
        this.leftFrontLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * f1;

        this.head.xRot = state.xRot * ((float)Math.PI / 180);
        this.head.yRot = state.yRot * ((float)Math.PI / 180);
        this.tail.xRot = (float) (Math.PI / 1.9f);
        this.tail.z -= 1.5f;

        this.isBiting = state.isBiting;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        head.render(poseStack, buffer, packedLight, packedOverlay, color);
        upperBody.render(poseStack, buffer, packedLight, packedOverlay, color);
        body.render(poseStack, buffer, packedLight, packedOverlay, color);
        rightHindLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
        leftHindLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
        rightFrontLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
        leftFrontLeg.render(poseStack, buffer, packedLight, packedOverlay, color);
        tail.render(poseStack, buffer, packedLight, packedOverlay, color);
        if (isBiting) {
            bite.render(poseStack, buffer, packedLight, packedOverlay, color);
        }
    }
}
