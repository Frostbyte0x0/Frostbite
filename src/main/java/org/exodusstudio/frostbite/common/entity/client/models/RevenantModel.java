package org.exodusstudio.frostbite.common.entity.client.models;

import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.item.SwingAnimationType;
import org.exodusstudio.frostbite.common.entity.client.animations.RevenantAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.RevenantRenderState;

public class RevenantModel extends HumanoidModel<RevenantRenderState> {
    private final KeyframeAnimation risingAnimation;

    public RevenantModel(ModelPart root) {
        super(root);
        this.risingAnimation = RevenantAnimations.RISING.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.01F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-0.99F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.99F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-1.9F, 0.01F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.1F, 0.01F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(RevenantRenderState state) {
        super.setupAnim(state);
        
        boolean flag = state.swingAnimationType != SwingAnimationType.STAB;
        if (flag) {
            float f = state.attackTime;
            float f1 = -(float)Math.PI / (state.isAggressive ? 1.5F : 2.25F);
            float f2 = Mth.sin(f * (float)Math.PI);
            float f3 = Mth.sin((1.0F - (1.0F - f) * (1.0F - f)) * (float)Math.PI);
            this.rightArm.zRot = 0.0F;
            this.rightArm.yRot = -(0.1F - f2 * 0.6F);
            this.rightArm.xRot = f1;
            this.rightArm.xRot += f2 * 1.2F - f3 * 0.4F;
            this.leftArm.zRot = 0.0F;
            this.leftArm.yRot = 0.1F - f2 * 0.6F;
            this.leftArm.xRot = f1;
            this.leftArm.xRot += f2 * 1.2F - f3 * 0.4F;
        }

        AnimationUtils.bobArms(this.rightArm, this.leftArm, state.ageInTicks);
        if (state.isRising) {
            risingAnimation.apply(state.risingAnimationState, state.ageInTicks);
        }
    }
}
