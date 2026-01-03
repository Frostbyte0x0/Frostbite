package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.animations.ShamanAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.StateRenderState;
import org.exodusstudio.frostbite.common.entity.custom.shaman.ShamanEntity;
import org.exodusstudio.frostbite.common.util.Util;

public class ShamanModel extends HumanoidModel<StateRenderState> {
    private final ModelPart right_leg;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation summoningAnimation;
    private final KeyframeAnimation whirlpoolingAnimation;
    private final KeyframeAnimation cursingAnimation;
    private final KeyframeAnimation etherealingAnimation;

    public ShamanModel(ModelPart root) {
        super(root);
        this.right_leg = root.getChild("right_leg");
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");
        this.left_leg = root.getChild("left_leg");
        this.idleAnimation = ShamanAnimations.PLAY.bake(root);
        this.summoningAnimation = ShamanAnimations.PLAY.bake(root);
        this.whirlpoolingAnimation = ShamanAnimations.PLAY.bake(root);
        this.cursingAnimation = ShamanAnimations.PLAY.bake(root);
        this.etherealingAnimation = ShamanAnimations.PLAY.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(32, 0).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 16).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(32, 16).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(24, 32).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(40, 32).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(StateRenderState state) {
        super.setupAnim(state);

        if (state.currentState.contains("attacking") || state.currentState.contains("guarding")) {
            leftArm.xRot = 0;
            leftArm.yRot = 0;
            leftArm.zRot = 0;
            rightArm.xRot = 0;
            rightArm.yRot = 0;
            rightArm.zRot = 0;
        }

        this.head.xRot = state.xRot * ((float)Math.PI / 180F);
        this.head.yRot = state.yRot * ((float)Math.PI / 180F);

        if (state.ticksSinceLastChange < ShamanEntity.BLEND_TICKS) {
            KeyframeAnimation currentAnimation = switch (state.currentState) {
                case "whirlpooling" -> whirlpoolingAnimation;
                case "cursing" -> cursingAnimation;
                case "summoning" -> summoningAnimation;
                case "etherealing" -> etherealingAnimation;
                default -> idleAnimation;
            };
            KeyframeAnimation lastAnimation = switch (state.lastState) {
                case "whirlpooling" -> whirlpoolingAnimation;
                case "cursing" -> cursingAnimation;
                case "summoning" -> summoningAnimation;
                case "etherealing" -> etherealingAnimation;
                default -> idleAnimation;
            };
            Util.blendAnimations(
                    state.ticksSinceLastChange,
                    ShamanEntity.BLEND_TICKS,
                    state.partialTick,
                    state.ageInTicks,
                    lastAnimation, state.lastAnimationState,
                    currentAnimation, state.currentAnimationState);
        } else {
            switch (state.currentState) {
                case "idle" -> idleAnimation.apply(state.currentAnimationState, state.ageInTicks);
                case "whirlpooling" -> whirlpoolingAnimation.apply(state.currentAnimationState, state.ageInTicks);
                case "cursing" -> cursingAnimation.apply(state.currentAnimationState, state.ageInTicks);
                case "summoning" -> summoningAnimation.apply(state.currentAnimationState, state.ageInTicks);
                case "etherealing" -> etherealingAnimation.apply(state.currentAnimationState, state.ageInTicks);
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        right_leg.render(poseStack, buffer, packedLight, packedOverlay, color);
        head.render(poseStack, buffer, packedLight, packedOverlay, color);
        body.render(poseStack, buffer, packedLight, packedOverlay, color);
        left_arm.render(poseStack, buffer, packedLight, packedOverlay, color);
        right_arm.render(poseStack, buffer, packedLight, packedOverlay, color);
        left_leg.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
