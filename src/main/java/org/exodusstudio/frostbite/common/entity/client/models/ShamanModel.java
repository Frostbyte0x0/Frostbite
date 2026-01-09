package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.animations.ShamanAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.ShamanRenderState;

import java.util.Map;

public class ShamanModel extends StateHumanoidModel<ShamanRenderState> {
    private final ModelPart right_leg;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final ModelPart shield;

    public ShamanModel(ModelPart root) {
        super(root);
        this.right_leg = root.getChild("right_leg");
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.left_arm = root.getChild("left_arm");
        this.right_arm = root.getChild("right_arm");
        this.left_leg = root.getChild("left_leg");
        this.shield = root.getChild("shield");
        this.animations = Map.of(
                "idle", ShamanAnimations.IDLE.bake(root),
                "summoning", ShamanAnimations.SUMMON.bake(root),
                "whirlpooling", ShamanAnimations.WHIRLPOOL.bake(root),
                "cursing", ShamanAnimations.CURSE.bake(root),
                "etherealing", ShamanAnimations.ETHEREAL.bake(root),
                "weakened", ShamanAnimations.WEAKENED.bake(root)
        );
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
        partdefinition.addOrReplaceChild("shield", CubeListBuilder.create().texOffs(4, 23).addBox(-6.0F, -9.0F, -12.0F, 12.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(ShamanRenderState state) {
        super.setupAnim(state);

        this.shield.visible = state.showShield;

        if (!state.currentState.contains("idle")) {
            leftArm.xRot = 0;
            leftArm.yRot = 0;
            leftArm.zRot = 0;
            rightArm.xRot = 0;
            rightArm.yRot = 0;
            rightArm.zRot = 0;
        }

        this.head.xRot = state.xRot * ((float)Math.PI / 180F);
        this.head.yRot = state.yRot * ((float)Math.PI / 180F);

        applyAnimation(state);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        right_leg.render(poseStack, buffer, packedLight, packedOverlay, color);
        head.render(poseStack, buffer, packedLight, packedOverlay, color);
        body.render(poseStack, buffer, packedLight, packedOverlay, color);
        left_arm.render(poseStack, buffer, packedLight, packedOverlay, color);
        right_arm.render(poseStack, buffer, packedLight, packedOverlay, color);
        left_leg.render(poseStack, buffer, packedLight, packedOverlay, color);
        shield.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
