package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.animations.GuardAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.GuardRenderState;
import org.exodusstudio.frostbite.common.entity.custom.guards.ChiefGuardEntity;
import org.exodusstudio.frostbite.common.util.Util;

public class HeavyGuardModel extends HumanoidModel<GuardRenderState> {
    private final ModelPart head;
    private final ModelPart headwear;
    private final ModelPart body;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final ModelPart right_leg;
    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation wakingUpAnimation;
    private final KeyframeAnimation attackingAnimation;
    private final KeyframeAnimation guardingAnimation;
    private final KeyframeAnimation asleepPose;

    public HeavyGuardModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.headwear = head.getChild("hat");
        this.body = root.getChild("body");
        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");
        this.left_leg = root.getChild("left_leg");
        this.right_leg = root.getChild("right_leg");
        this.idleAnimation = GuardAnimations.IDLE.bake(root);
        this.wakingUpAnimation = GuardAnimations.WAKING_UP.bake(root);
        this.attackingAnimation = GuardAnimations.GUARD_ATTACK.bake(root);
        this.guardingAnimation = GuardAnimations.GUARD_GUARD.bake(root);
        this.asleepPose = GuardAnimations.ASLEEP.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));
        PartDefinition shield = left_arm.addOrReplaceChild("shield", CubeListBuilder.create(), PartPose.offset(1.0F, 11.0F, -3.0F));
        shield.addOrReplaceChild("shield_r1", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, -6.0F, -1.01F, 8.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, 1.5708F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(GuardRenderState state) {
        super.setupAnim(state);

        if (state.currentState.equals("attacking") || state.currentState.equals("guarding") || state.currentState.equals("asleep")) {
            leftArm.xRot = 0;
            leftArm.yRot = 0;
            leftArm.zRot = 0;
            rightArm.xRot = 0;
            rightArm.yRot = 0;
            rightArm.zRot = 0;
        }

        this.head.xRot = state.xRot * ((float)Math.PI / 180F);
        this.head.yRot = state.yRot * ((float)Math.PI / 180F);

        if (state.ticksSinceLastChange < ChiefGuardEntity.BLEND_TICKS) {
            KeyframeAnimation currentAnimation = switch (state.currentState) {
                case "guarding" -> guardingAnimation;
                case "wakingUp" -> wakingUpAnimation;
                case "attacking" -> attackingAnimation;
                case "asleep" -> asleepPose;
                default -> idleAnimation;
            };
            KeyframeAnimation lastAnimation = switch (state.lastState) {
                case "guarding" -> guardingAnimation;
                case "wakingUp" -> wakingUpAnimation;
                case "attacking" -> attackingAnimation;
                case "asleep" -> asleepPose;
                default -> idleAnimation;
            };
            Util.blendAnimations(
                    state.ticksSinceLastChange,
                    ChiefGuardEntity.BLEND_TICKS,
                    state.partialTick,
                    state.ageInTicks,
                    lastAnimation, state.lastAnimationState,
                    currentAnimation, state.currentAnimationState);
        } else {
            switch (state.currentState) {
                case "idle" -> idleAnimation.apply(state.currentAnimationState, state.ageInTicks);
                case "guarding" -> guardingAnimation.apply(state.currentAnimationState, state.ageInTicks);
                case "wakingUp" -> wakingUpAnimation.apply(state.currentAnimationState, state.ageInTicks);
                case "attacking" -> attackingAnimation.apply(state.currentAnimationState, state.ageInTicks);
                case "asleep" -> asleepPose.apply(state.currentAnimationState, state.ageInTicks);
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        head.render(poseStack, buffer, packedLight, packedOverlay, color);
        headwear.render(poseStack, buffer, packedLight, packedOverlay, color);
        body.render(poseStack, buffer, packedLight, packedOverlay, color);
        left_arm.render(poseStack, buffer, packedLight, packedOverlay, color);
        right_arm.render(poseStack, buffer, packedLight, packedOverlay, color);
        left_leg.render(poseStack, buffer, packedLight, packedOverlay, color);
        right_leg.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
