package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.animations.BanditAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.BanditRenderState;

public class BanditModel extends EntityModel<BanditRenderState> {
    private final KeyframeAnimation stealingAnimation;
    private final KeyframeAnimation walkingAnimation;
    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation fleeingAnimation;
    private final ModelPart model;
    private final ModelPart head;

    public BanditModel(ModelPart root) {
        super(root);
        this.stealingAnimation = BanditAnimations.IDLE_PLACEHOLDER.bake(root);
        this.walkingAnimation = BanditAnimations.WALK_PLACEHOLDER.bake(root);
        this.idleAnimation = BanditAnimations.IDLE_PLACEHOLDER.bake(root);
        this.fleeingAnimation = BanditAnimations.FLEEING_DOUBLE_PEEK.bake(root);
        this.model = root.getChild("Model");
        this.head = this.model.getChild("Body").getChild("BodyBottom")
                .getChild("BodyTop").getChild("Head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Model = partdefinition.addOrReplaceChild("Model", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition Body = Model.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(-1.5F, -2.75F, -3.5F));
        PartDefinition BodyBottom = Body.addOrReplaceChild("BodyBottom", CubeListBuilder.create(), PartPose.offset(1.5F, -3.0F, 3.5F));
        BodyBottom.addOrReplaceChild("BodyBottom_r1", CubeListBuilder.create().texOffs(0, 31).addBox(-4.5F, -5.5632F, -2.5862F, 9.0F, 7.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, -0.3054F, 0.0F, 0.0F));
        PartDefinition BodyTop = BodyBottom.addOrReplaceChild("BodyTop", CubeListBuilder.create(), PartPose.offset(0.0F, -4.5F, 1.25F));
        BodyTop.addOrReplaceChild("BodyTop_r1", CubeListBuilder.create().texOffs(0, 19).addBox(-4.0F, -5.8248F, -1.8869F, 8.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, 0.0F, 0.5095F, 0.0F, 0.0F));
        PartDefinition Head = BodyTop.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 8).addBox(-3.5F, -3.0F, -4.0F, 7.0F, 4.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-2.5F, -4.0F, -4.0F, 5.0F, 1.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -3.75F));
        Head.addOrReplaceChild("HeadSnout_r1", CubeListBuilder.create().texOffs(17, 1).addBox(-1.5F, -0.8993F, -1.3547F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.75F, 0.6981F, 0.0F, 0.0F));
        PartDefinition Ears = Head.addOrReplaceChild("Ears", CubeListBuilder.create(), PartPose.offset(-0.5F, -2.5F, -0.5F));
        Ears.addOrReplaceChild("EarR_r1", CubeListBuilder.create().texOffs(1, 10).addBox(-0.3374F, -1.7016F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.25F, -0.75F, 0.0F, 0.0F, 0.0F, -0.5585F));
        Ears.addOrReplaceChild("EarL_r1", CubeListBuilder.create().texOffs(1, 10).addBox(-0.4506F, -1.834F, -2.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -0.75F, 0.0F, 0.0F, 0.0F, 0.5585F));
        PartDefinition ArmL = BodyTop.addOrReplaceChild("ArmL", CubeListBuilder.create(), PartPose.offset(2.65F, -3.0F, -2.05F));
        ArmL.addOrReplaceChild("ArmL_r1", CubeListBuilder.create().texOffs(32, 21).addBox(-0.9156F, -0.7493F, -0.4241F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.15F, -0.25F, 0.05F, -0.5411F, 0.0F, 0.0F));
        PartDefinition ArmR = BodyTop.addOrReplaceChild("ArmR", CubeListBuilder.create(), PartPose.offset(-2.4F, -3.0F, -1.95F));
        ArmR.addOrReplaceChild("ArmR_r1", CubeListBuilder.create().texOffs(25, 21).addBox(-0.9156F, -0.7493F, -0.4241F, 2.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1F, -0.25F, -0.05F, -0.5411F, 0.0F, 0.0F));
        PartDefinition Bag = BodyBottom.addOrReplaceChild("Bag", CubeListBuilder.create(), PartPose.offset(4.5F, -1.25F, -0.5F));
        Bag.addOrReplaceChild("Bag_r1", CubeListBuilder.create().texOffs(21, 6).addBox(-2.5F, -0.7282F, -1.9762F, 5.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, -0.0873F));
        PartDefinition Tail = Body.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offset(1.5F, -3.25F, 6.0F));
        Tail.addOrReplaceChild("TailBase_r1", CubeListBuilder.create().texOffs(27, 14).addBox(-0.4512F, -0.9998F, -1.5F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.75F, 2.25F, -1.5708F, 0.6632F, -1.5708F));
        PartDefinition TailEnd = Tail.addOrReplaceChild("TailEnd", CubeListBuilder.create(), PartPose.offset(0.0F, 2.75F, 2.25F));
        TailEnd.addOrReplaceChild("TailEnd_r1", CubeListBuilder.create().texOffs(27, 14).addBox(-4.3979F, -1.0488F, -1.51F, 5.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.6632F, -1.5708F));
        PartDefinition LegL = Model.addOrReplaceChild("LegL", CubeListBuilder.create().texOffs(29, 35).addBox(-1.0F, -1.0F, -1.0481F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -5.0F, 0.0F));
        LegL.addOrReplaceChild("FootL_r1", CubeListBuilder.create().texOffs(24, 33).addBox(-0.8F, -0.447F, -0.553F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.25F, 4.75F, -1.0F, -0.7854F, 0.0F, 0.0F));
        PartDefinition LegR = Model.addOrReplaceChild("LegR", CubeListBuilder.create().texOffs(29, 35).addBox(-1.0F, -1.0F, -1.0481F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -5.0F, 0.0F));
        LegR.addOrReplaceChild("FootR_r1", CubeListBuilder.create().texOffs(24, 33).addBox(-1.2F, -0.447F, -0.553F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.25F, 4.75F, -1.0F, -0.7854F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(BanditRenderState state) {
        super.setupAnim(state);
        this.head.xRot = state.xRot * ((float)Math.PI / 180F);
        this.head.yRot = state.yRot * ((float)Math.PI / 180F);
        switch (state.currentState) {
            case "idle" -> idleAnimation.apply(state.idleAnimationState, state.ageInTicks);
            case "walking" -> walkingAnimation.apply(state.walkingAnimationState, state.ageInTicks);
            case "fleeing" -> fleeingAnimation.apply(state.fleeingAnimationState, state.ageInTicks);
            case "stealing" -> stealingAnimation.apply(state.stealingAnimationState, state.ageInTicks);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        model.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
