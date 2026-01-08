package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.common.entity.client.animations.ElfAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.ElfRenderState;
import org.exodusstudio.frostbite.common.entity.custom.guards.ChiefGuardEntity;
import org.exodusstudio.frostbite.common.util.Util;

import java.util.Map;

public class ElfModel extends CustomizableHumanoidModel<ElfRenderState> {
    private final ModelPart elf;
    protected Map<String, KeyframeAnimation> animations;

    public ElfModel(ModelPart root) {
        super(root.getChild("elf").getChild("model"));
        this.elf = root.getChild("elf");
        this.animations = Map.of(
                "idle", ElfAnimations.IDLE.bake(root),
                "jumping", ElfAnimations.JUMP.bake(root),
                "walking", ElfAnimations.WALK.bake(root)
        );
        this.head = this.elf.getChild("model").getChild("body").getChild("head");
        this.leftArm = this.elf.getChild("model").getChild("body").getChild("left_arm");
        this.rightArm = this.elf.getChild("model").getChild("body").getChild("right_arm");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

//        PartDefinition elf = partdefinition.addOrReplaceChild("elf", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
//        PartDefinition model = elf.addOrReplaceChild("model", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
//        PartDefinition body = model.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 17).addBox(-4.0F, -10.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.0F, 0.0F));
//        PartDefinition arm_r = model.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -9.0F, 0.0F));
//        arm_r.addOrReplaceChild("arm_r_r1", CubeListBuilder.create().texOffs(17, 49).addBox(-2.8706F, -1.1489F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1396F));
//        arm_r.addOrReplaceChild("hand_r", CubeListBuilder.create().texOffs(31, 30).mirror().addBox(-1.5F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.75F, 8.0F, -0.5F));
//        PartDefinition arm_l = model.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -9.0F, 0.0F));
//        arm_l.addOrReplaceChild("arm_l_r1", CubeListBuilder.create().texOffs(0, 49).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.1396F));
//        PartDefinition hand_l = arm_l.addOrReplaceChild("hand_l", CubeListBuilder.create().texOffs(31, 30).addBox(-1.5F, -0.5F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.75F, 8.5F, -0.5F));
//        PartDefinition staff = hand_l.addOrReplaceChild("staff", CubeListBuilder.create().texOffs(119, 17).addBox(-1.0F, -9.5F, -1.0F, 2.0F, 28.0F, 2.0F, new CubeDeformation(-0.25F))
//                .texOffs(107, 9).addBox(-4.0F, -14.0F, -1.0F, 8.0F, 5.0F, 2.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-0.25F, 1.25F, 0.0F, 1.5708F, 0.0F, -1.5708F));
//        staff.addOrReplaceChild("staff_orb", CubeListBuilder.create().texOffs(106, 17).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.5F, 0.0F));
//        PartDefinition cape = body.addOrReplaceChild("cape", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 2.25F));
//        cape.addOrReplaceChild("cape_r1", CubeListBuilder.create().texOffs(44, 19).addBox(-7.0F, 0.0F, -1.0F, 10.0F, 21.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));
//        PartDefinition head = model.addOrReplaceChild("head", CubeListBuilder.create().texOffs(64, 32).addBox(-8.0F, -10.75F, -9.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(-4.5F)), PartPose.offsetAndRotation(0.0F, -10.75F, 1.0F, 0.0873F, 0.0F, 0.0F));
//        head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(25, 18).addBox(-2.0F, -5.0F, 0.0F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, -2.25F, -0.6068F, 0.0749F, 0.1074F));
//        head.addOrReplaceChild("hat1_r1", CubeListBuilder.create().texOffs(59, 0).addBox(-3.0F, -4.0F, -1.0F, 7.0F, 4.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-0.5F, -4.5F, -4.0F, -0.3924F, 0.0167F, 0.0403F));
//        head.addOrReplaceChild("hatrim_r1", CubeListBuilder.create().texOffs(22, 5).addBox(-6.0F, -1.0F, -2.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, -5.0F, -0.1745F, 0.0F, 0.0F));
//        PartDefinition leg_l = model.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(47, 47).addBox(-1.25F, 0.0F, -1.5F, 3.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -13.0F, 0.0F));
//        PartDefinition leg_robe_l = leg_l.addOrReplaceChild("leg_robe_l", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, -2.0F));
//        leg_robe_l.addOrReplaceChild("leg_robe_l_r1", CubeListBuilder.create().texOffs(0, 33).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 0.0F, -0.0873F));
//        PartDefinition leg_r = model.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(34, 47).addBox(-1.75F, 0.0F, -1.5F, 3.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -13.0F, 0.0F));
//        PartDefinition leg_robe_r = leg_r.addOrReplaceChild("leg_robe_r", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, -2.0F));
//        leg_robe_r.addOrReplaceChild("leg_robe_r_r1", CubeListBuilder.create().texOffs(17, 33).addBox(0.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.0873F));
//        return LayerDefinition.create(meshdefinition, 128, 64);

        PartDefinition elf = partdefinition.addOrReplaceChild("elf", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition model = elf.addOrReplaceChild("model", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition body = model.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 17).addBox(-4.0F, -10.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.0F, 0.0F));
        PartDefinition arm_r = body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-4.0F, -9.0F, 0.0F));
        arm_r.addOrReplaceChild("arm_r_r1", CubeListBuilder.create().texOffs(17, 49).addBox(-2.8706F, -1.1489F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1396F));
        arm_r.addOrReplaceChild("hand_r", CubeListBuilder.create().texOffs(31, 30).mirror().addBox(-1.5F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.75F, 8.0F, -0.5F));
        PartDefinition arm_l = body.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(4.0F, -9.0F, 0.0F));
        arm_l.addOrReplaceChild("arm_l_r1", CubeListBuilder.create().texOffs(0, 49).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(-0.1F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, -0.1396F));
        PartDefinition hand_l = arm_l.addOrReplaceChild("hand_l", CubeListBuilder.create().texOffs(31, 30).addBox(-1.5F, -0.5F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.75F, 8.5F, -0.5F));
        PartDefinition staff = hand_l.addOrReplaceChild("staff", CubeListBuilder.create().texOffs(119, 17).addBox(-1.0F, -9.5F, -1.0F, 2.0F, 28.0F, 2.0F, new CubeDeformation(-0.25F))
                .texOffs(107, 9).addBox(-4.0F, -14.0F, -1.0F, 8.0F, 5.0F, 2.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-0.25F, 1.25F, 0.0F, 1.5708F, 0.0F, -1.5708F));
        staff.addOrReplaceChild("staff_orb", CubeListBuilder.create().texOffs(106, 17).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.5F, 0.0F));
        PartDefinition cape = body.addOrReplaceChild("cape", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 2.25F));
        cape.addOrReplaceChild("cape_r1", CubeListBuilder.create().texOffs(44, 19).addBox(-7.0F, 0.0F, -1.0F, 10.0F, 21.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.1309F, 0.0F, 0.0F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(64, 32).addBox(-8.0F, -10.75F, -9.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(-4.5F)), PartPose.offsetAndRotation(0.0F, -10.75F, 1.0F, 0.0873F, 0.0F, 0.0F));
        head.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(25, 18).addBox(-2.0F, -5.0F, 0.0F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, -2.25F, -0.6068F, 0.0749F, 0.1074F));
        head.addOrReplaceChild("hat1_r1", CubeListBuilder.create().texOffs(59, 0).addBox(-3.0F, -4.0F, -1.0F, 7.0F, 4.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offsetAndRotation(-0.5F, -4.5F, -4.0F, -0.3924F, 0.0167F, 0.0403F));
        head.addOrReplaceChild("hatrim_r1", CubeListBuilder.create().texOffs(22, 5).addBox(-6.0F, -1.0F, -2.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.0F, -5.0F, -0.1745F, 0.0F, 0.0F));
        PartDefinition leg_l = model.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(47, 47).addBox(-1.25F, 0.0F, -1.5F, 3.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -13.0F, 0.0F));
        PartDefinition leg_robe_l = leg_l.addOrReplaceChild("leg_robe_l", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, -2.0F));
        leg_robe_l.addOrReplaceChild("leg_robe_l_r1", CubeListBuilder.create().texOffs(0, 33).addBox(-4.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 0.0F, -0.0873F));
        PartDefinition leg_r = model.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(34, 47).addBox(-1.75F, 0.0F, -1.5F, 3.0F, 13.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -13.0F, 0.0F));
        PartDefinition leg_robe_r = leg_r.addOrReplaceChild("leg_robe_r", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, -2.0F));
        leg_robe_r.addOrReplaceChild("leg_robe_r_r1", CubeListBuilder.create().texOffs(17, 33).addBox(0.0F, 0.0F, -2.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.0873F));
        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(ElfRenderState state) {
        super.setupAnim(state);

        if (state.currentState.equals("attacking")) {
            rightArm.xRot -= (float) (Math.PI / 5);
            rightArm.xRot -= Mth.lerp(state.ticksSinceLastChange, 0, (float) (Math.PI / 40));
        }

        if (state.isJumping) {
            animations.get("jumping").apply(state.jumpingAnimationState, state.ageInTicks);
        }

        if (state.ticksSinceLastChange < ChiefGuardEntity.BLEND_TICKS) {
            KeyframeAnimation currentAnimation = animations.get(state.currentState);
            KeyframeAnimation lastAnimation = animations.get(state.lastState);
            Util.blendAnimations(
                    state.ticksSinceLastChange,
                    ChiefGuardEntity.BLEND_TICKS,
                    state.partialTick,
                    state.ageInTicks,
                    lastAnimation, state.lastAnimationState,
                    currentAnimation, state.currentAnimationState);
        } else {
            animations.get(state.currentState).apply(state.currentAnimationState, state.ageInTicks);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        elf.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
