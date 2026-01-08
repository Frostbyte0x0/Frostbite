package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.*;
import net.minecraft.client.model.effects.SpearAnimations;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Ease;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;

import java.util.Set;
import java.util.function.Function;

public class CustomizableHumanoidModel<T extends HumanoidRenderState> extends EntityModel<T> implements ArmedModel<T>, HeadedModel {
    protected ModelPart head;
    protected ModelPart hat;
    protected ModelPart body;
    protected ModelPart rightArm;
    protected ModelPart leftArm;
    protected ModelPart rightLeg;
    protected ModelPart leftLeg;

    public CustomizableHumanoidModel(ModelPart root) {
        this(root, RenderTypes::entityCutoutNoCull);
    }

    public CustomizableHumanoidModel(ModelPart root, Function<Identifier, RenderType> renderType) {
        super(root, renderType);
        this.head = tryGetChild(root, "head");
        this.hat = tryGetChild(this.head, "hat");
        this.body = tryGetChild(root, "body");
        this.rightArm = tryGetChild(root, "right_arm");
        this.leftArm = tryGetChild(root, "left_arm");
        this.rightLeg = tryGetChild(root, "right_leg");
        this.leftLeg = tryGetChild(root, "left_leg");
    }

    protected ModelPart tryGetChild(ModelPart root, String name) {
        if (root == null) return null;
        return root.hasChild(name) ? root.getChild(name) : null;
    }

    public static MeshDefinition createMesh(CubeDeformation cubeDeformation, float yOffset) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition partdefinition1 = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubeDeformation), PartPose.offset(0.0F, 0.0F + yOffset, 0.0F));
        partdefinition1.addOrReplaceChild("hat", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, cubeDeformation.extend(0.5F)), PartPose.ZERO);
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(0.0F, 0.0F + yOffset, 0.0F));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(-5.0F, 2.0F + yOffset, 0.0F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(5.0F, 2.0F + yOffset, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(-1.9F, 12.0F + yOffset, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation), PartPose.offset(1.9F, 12.0F + yOffset, 0.0F));
        return meshdefinition;
    }

    protected static ArmorModelSet<MeshDefinition> createArmorMeshSet(Function<CubeDeformation, MeshDefinition> meshCreator, CubeDeformation innerCubeDeformation, CubeDeformation outerCubeDeformation) {
        MeshDefinition meshdefinition = meshCreator.apply(outerCubeDeformation);
        meshdefinition.getRoot().retainPartsAndChildren(Set.of("head"));
        MeshDefinition meshdefinition1 = meshCreator.apply(outerCubeDeformation);
        meshdefinition1.getRoot().retainExactParts(Set.of("body", "left_arm", "right_arm"));
        MeshDefinition meshdefinition2 = meshCreator.apply(innerCubeDeformation);
        meshdefinition2.getRoot().retainExactParts(Set.of("left_leg", "right_leg", "body"));
        MeshDefinition meshdefinition3 = meshCreator.apply(outerCubeDeformation);
        meshdefinition3.getRoot().retainExactParts(Set.of("left_leg", "right_leg"));
        return new ArmorModelSet<>(meshdefinition, meshdefinition1, meshdefinition2, meshdefinition3);
    }

    private static MeshDefinition createBaseArmorMesh(CubeDeformation cubeDeformation) {
        MeshDefinition meshdefinition = createMesh(cubeDeformation, 0.0F);
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(-0.1F)), PartPose.offset(-1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, cubeDeformation.extend(-0.1F)), PartPose.offset(1.9F, 12.0F, 0.0F));
        return meshdefinition;
    }

    public void setupAnim(T p_361833_) {
        super.setupAnim(p_361833_);
        HumanoidModel.ArmPose humanoidmodel$armpose = p_361833_.leftArmPose;
        HumanoidModel.ArmPose humanoidmodel$armpose1 = p_361833_.rightArmPose;
        float f = p_361833_.swimAmount;
        boolean flag = p_361833_.isFallFlying;
        this.head.xRot = p_361833_.xRot * ((float)Math.PI / 180F);
        this.head.yRot = p_361833_.yRot * ((float)Math.PI / 180F);
        if (flag) {
            this.head.xRot = (-(float)Math.PI / 4F);
        } else if (f > 0.0F) {
            this.head.xRot = Mth.rotLerpRad(f, this.head.xRot, (-(float)Math.PI / 4F));
        }

        float f1 = p_361833_.walkAnimationPos;
        float f2 = p_361833_.walkAnimationSpeed;
        this.rightArm.xRot = Mth.cos(f1 * 0.6662F + (float)Math.PI) * 2.0F * f2 * 0.5F / p_361833_.speedValue;
        this.leftArm.xRot = Mth.cos(f1 * 0.6662F) * 2.0F * f2 * 0.5F / p_361833_.speedValue;
        this.rightLeg.xRot = Mth.cos(f1 * 0.6662F) * 1.4F * f2 / p_361833_.speedValue;
        this.leftLeg.xRot = Mth.cos(f1 * 0.6662F + (float)Math.PI) * 1.4F * f2 / p_361833_.speedValue;
        this.rightLeg.yRot = 0.005F;
        this.leftLeg.yRot = -0.005F;
        this.rightLeg.zRot = 0.005F;
        this.leftLeg.zRot = -0.005F;
        if (p_361833_.isPassenger) {
            ModelPart var10000 = this.rightArm;
            var10000.xRot += (-(float)Math.PI / 5F);
            var10000 = this.leftArm;
            var10000.xRot += (-(float)Math.PI / 5F);
            this.rightLeg.xRot = -1.4137167F;
            this.rightLeg.yRot = ((float)Math.PI / 10F);
            this.rightLeg.zRot = 0.07853982F;
            this.leftLeg.xRot = -1.4137167F;
            this.leftLeg.yRot = (-(float)Math.PI / 10F);
            this.leftLeg.zRot = -0.07853982F;
        }

        boolean flag1 = p_361833_.mainArm == HumanoidArm.RIGHT;
        if (p_361833_.isUsingItem) {
            boolean flag2 = p_361833_.useItemHand == InteractionHand.MAIN_HAND;
            if (flag2 == flag1) {
                this.poseRightArm(p_361833_);
                if (!p_361833_.rightArmPose.affectsOffhandPose()) {
                    this.poseLeftArm(p_361833_);
                }
            } else {
                this.poseLeftArm(p_361833_);
                if (!p_361833_.leftArmPose.affectsOffhandPose()) {
                    this.poseRightArm(p_361833_);
                }
            }
        } else {
            boolean flag3 = flag1 ? humanoidmodel$armpose.isTwoHanded() : humanoidmodel$armpose1.isTwoHanded();
            if (flag1 != flag3) {
                this.poseLeftArm(p_361833_);
                if (!p_361833_.leftArmPose.affectsOffhandPose()) {
                    this.poseRightArm(p_361833_);
                }
            } else {
                this.poseRightArm(p_361833_);
                if (!p_361833_.rightArmPose.affectsOffhandPose()) {
                    this.poseLeftArm(p_361833_);
                }
            }
        }

        this.setupAttackAnimation(p_361833_);
        if (p_361833_.isCrouching) {
            this.body.xRot = 0.5F;
            ModelPart var20 = this.rightArm;
            var20.xRot += 0.4F;
            var20 = this.leftArm;
            var20.xRot += 0.4F;
            var20 = this.rightLeg;
            var20.z += 4.0F;
            var20 = this.leftLeg;
            var20.z += 4.0F;
            var20 = this.head;
            var20.y += 4.2F;
            var20 = this.body;
            var20.y += 3.2F;
            var20 = this.leftArm;
            var20.y += 3.2F;
            var20 = this.rightArm;
            var20.y += 3.2F;
        }

        if (humanoidmodel$armpose1 != HumanoidModel.ArmPose.SPYGLASS) {
            AnimationUtils.bobModelPart(this.rightArm, p_361833_.ageInTicks, 1.0F);
        }

        if (humanoidmodel$armpose != HumanoidModel.ArmPose.SPYGLASS) {
            AnimationUtils.bobModelPart(this.leftArm, p_361833_.ageInTicks, -1.0F);
        }

        if (f > 0.0F) {
            float f7 = f1 % 26.0F;
            HumanoidArm humanoidarm = p_361833_.attackArm;
            float f3 = p_361833_.rightArmPose == HumanoidModel.ArmPose.SPEAR || humanoidarm == HumanoidArm.RIGHT && p_361833_.attackTime > 0.0F ? 0.0F : f;
            float f4 = p_361833_.leftArmPose == HumanoidModel.ArmPose.SPEAR || humanoidarm == HumanoidArm.LEFT && p_361833_.attackTime > 0.0F ? 0.0F : f;
            if (!p_361833_.isUsingItem) {
                if (f7 < 14.0F) {
                    this.leftArm.xRot = Mth.rotLerpRad(f4, this.leftArm.xRot, 0.0F);
                    this.rightArm.xRot = Mth.lerp(f3, this.rightArm.xRot, 0.0F);
                    this.leftArm.yRot = Mth.rotLerpRad(f4, this.leftArm.yRot, (float)Math.PI);
                    this.rightArm.yRot = Mth.lerp(f3, this.rightArm.yRot, (float)Math.PI);
                    this.leftArm.zRot = Mth.rotLerpRad(f4, this.leftArm.zRot, (float)Math.PI + 1.8707964F * this.quadraticArmUpdate(f7) / this.quadraticArmUpdate(14.0F));
                    this.rightArm.zRot = Mth.lerp(f3, this.rightArm.zRot, (float)Math.PI - 1.8707964F * this.quadraticArmUpdate(f7) / this.quadraticArmUpdate(14.0F));
                } else if (f7 >= 14.0F && f7 < 22.0F) {
                    float f8 = (f7 - 14.0F) / 8.0F;
                    this.leftArm.xRot = Mth.rotLerpRad(f4, this.leftArm.xRot, ((float)Math.PI / 2F) * f8);
                    this.rightArm.xRot = Mth.lerp(f3, this.rightArm.xRot, ((float)Math.PI / 2F) * f8);
                    this.leftArm.yRot = Mth.rotLerpRad(f4, this.leftArm.yRot, (float)Math.PI);
                    this.rightArm.yRot = Mth.lerp(f3, this.rightArm.yRot, (float)Math.PI);
                    this.leftArm.zRot = Mth.rotLerpRad(f4, this.leftArm.zRot, 5.012389F - 1.8707964F * f8);
                    this.rightArm.zRot = Mth.lerp(f3, this.rightArm.zRot, 1.2707963F + 1.8707964F * f8);
                } else if (f7 >= 22.0F && f7 < 26.0F) {
                    float f5 = (f7 - 22.0F) / 4.0F;
                    this.leftArm.xRot = Mth.rotLerpRad(f4, this.leftArm.xRot, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f5);
                    this.rightArm.xRot = Mth.lerp(f3, this.rightArm.xRot, ((float)Math.PI / 2F) - ((float)Math.PI / 2F) * f5);
                    this.leftArm.yRot = Mth.rotLerpRad(f4, this.leftArm.yRot, (float)Math.PI);
                    this.rightArm.yRot = Mth.lerp(f3, this.rightArm.yRot, (float)Math.PI);
                    this.leftArm.zRot = Mth.rotLerpRad(f4, this.leftArm.zRot, (float)Math.PI);
                    this.rightArm.zRot = Mth.lerp(f3, this.rightArm.zRot, (float)Math.PI);
                }
            }
            this.leftLeg.xRot = Mth.lerp(f, this.leftLeg.xRot, 0.3F * Mth.cos(f1 * 0.33333334F + (float)Math.PI));
            this.rightLeg.xRot = Mth.lerp(f, this.rightLeg.xRot, 0.3F * Mth.cos(f1 * 0.33333334F));
        }

    }

    private void poseRightArm(T p_364666_) {
        switch (p_364666_.rightArmPose.ordinal()) {
            case 0:
                this.rightArm.yRot = 0.0F;
                break;
            case 1:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float)Math.PI / 10F);
                this.rightArm.yRot = 0.0F;
                break;
            case 2:
                this.poseBlockingArm(this.rightArm, true);
                break;
            case 3:
                this.rightArm.yRot = -0.1F + this.head.yRot;
                this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
                this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
                this.leftArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
                break;
            case 4:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float)Math.PI;
                this.rightArm.yRot = 0.0F;
                break;
            case 5:
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, p_364666_.maxCrossbowChargeDuration, p_364666_.ticksUsingItem, true);
                break;
            case 6:
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
                break;
            case 7:
                this.rightArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (p_364666_.isCrouching ? 0.2617994F : 0.0F), -2.4F, 3.3F);
                this.rightArm.yRot = this.head.yRot - 0.2617994F;
                break;
            case 8:
                this.rightArm.xRot = Mth.clamp(this.head.xRot, -1.2F, 1.2F) - 1.4835298F;
                this.rightArm.yRot = this.head.yRot - ((float)Math.PI / 6F);
                break;
            case 9:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - ((float)Math.PI / 5F);
                this.rightArm.yRot = 0.0F;
                break;
            case 10:
                SpearAnimations.thirdPersonHandUse(this.rightArm, this.head, true, p_364666_.getUseItemStackForArm(HumanoidArm.RIGHT), p_364666_);
                break;
//            default:
//                p_364666_.rightArmPose.applyTransform(this, p_364666_, HumanoidArm.RIGHT);
        }

    }

    private void poseLeftArm(T p_361741_) {
        switch (p_361741_.leftArmPose.ordinal()) {
            case 0:
                this.leftArm.yRot = 0.0F;
                break;
            case 1:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float)Math.PI / 10F);
                this.leftArm.yRot = 0.0F;
                break;
            case 2:
                this.poseBlockingArm(this.leftArm, false);
                break;
            case 3:
                this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
                this.leftArm.yRot = 0.1F + this.head.yRot;
                this.rightArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
                this.leftArm.xRot = (-(float)Math.PI / 2F) + this.head.xRot;
                break;
            case 4:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float)Math.PI;
                this.leftArm.yRot = 0.0F;
                break;
            case 5:
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, p_361741_.maxCrossbowChargeDuration, p_361741_.ticksUsingItem, false);
                break;
            case 6:
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
                break;
            case 7:
                this.leftArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (p_361741_.isCrouching ? 0.2617994F : 0.0F), -2.4F, 3.3F);
                this.leftArm.yRot = this.head.yRot + 0.2617994F;
                break;
            case 8:
                this.leftArm.xRot = Mth.clamp(this.head.xRot, -1.2F, 1.2F) - 1.4835298F;
                this.leftArm.yRot = this.head.yRot + ((float)Math.PI / 6F);
                break;
            case 9:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - ((float)Math.PI / 5F);
                this.leftArm.yRot = 0.0F;
                break;
            case 10:
                SpearAnimations.thirdPersonHandUse(this.leftArm, this.head, false, p_361741_.getUseItemStackForArm(HumanoidArm.LEFT), p_361741_);
                break;
//            default:
//                p_361741_.leftArmPose.applyTransform(this, p_361741_, HumanoidArm.LEFT);
        }

    }

    private void poseBlockingArm(ModelPart arm, boolean isRightArm) {
        arm.xRot = arm.xRot * 0.5F - 0.9424779F + Mth.clamp(this.head.xRot, -1.3962634F, 0.43633232F);
        arm.yRot = (isRightArm ? -30.0F : 30.0F) * ((float)Math.PI / 180F) + Mth.clamp(this.head.yRot, (-(float)Math.PI / 6F), ((float)Math.PI / 6F));
    }

    protected void setupAttackAnimation(T p_361366_) {
        float f = p_361366_.attackTime;
        if (!(f <= 0.0F)) {
            this.body.yRot = Mth.sin(Mth.sqrt(f) * ((float)Math.PI * 2F)) * 0.2F;
            if (p_361366_.attackArm == HumanoidArm.LEFT) {
                this.body.yRot *= -1.0F;
            }

            float f1 = p_361366_.ageScale;
            this.rightArm.z = Mth.sin(this.body.yRot) * 5.0F * f1;
            this.rightArm.x = -Mth.cos(this.body.yRot) * 5.0F * f1;
            this.leftArm.z = -Mth.sin(this.body.yRot) * 5.0F * f1;
            this.leftArm.x = Mth.cos(this.body.yRot) * 5.0F * f1;
            this.rightArm.yRot += this.body.yRot;
            this.leftArm.yRot += this.body.yRot;
            this.leftArm.xRot += this.body.yRot;
            switch (p_361366_.swingAnimationType) {
                case WHACK:
                    float f2 = Ease.outQuart(f);
                    float f3 = Mth.sin(f2 * (float)Math.PI);
                    float f4 = Mth.sin(f * (float)Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
                    ModelPart modelpart = this.getArm(p_361366_.attackArm);
                    modelpart.xRot -= f3 * 1.2F + f4;
                    modelpart.yRot += this.body.yRot * 2.0F;
                    modelpart.zRot += Mth.sin(f * (float)Math.PI) * -0.4F;
                case NONE:
                default:
                    break;
            }
        }

    }

    private float quadraticArmUpdate(float limbSwing) {
        return -65.0F * limbSwing + limbSwing * limbSwing;
    }

    public void translateToHand(HumanoidRenderState p_434082_, HumanoidArm p_102854_, PoseStack p_102855_) {
        this.root.translateAndRotate(p_102855_);
        this.getArm(p_102854_).translateAndRotate(p_102855_);
    }

    public ModelPart getArm(HumanoidArm side) {
        return side == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
    }

    public ModelPart getHead() {
        return this.head;
    }
}
