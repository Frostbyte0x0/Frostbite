package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.animations.TorchAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.TorchRenderState;
import org.exodusstudio.frostbite.common.entity.custom.ennemies.TorchEntity;
import org.exodusstudio.frostbite.common.util.Util;

public class TorchModel extends EntityModel<TorchRenderState> {
    private final ModelPart model;
    private final ModelPart body;
    private final ModelPart head;
    private final KeyframeAnimation slicingAnimationState;
    private final KeyframeAnimation idleAnimationState;

    public TorchModel(ModelPart root) {
        super(root.getChild("torch").getChild("model"));
        this.slicingAnimationState = TorchAnimations.SLICE.bake(root);
        this.idleAnimationState = TorchAnimations.IDLE.bake(root);
        ModelPart torch = root.getChild("torch");
        this.model = torch.getChild("model");
        this.body = this.model.getChild("body");
        this.head = body.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition torch = partdefinition.addOrReplaceChild("torch", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition model = torch.addOrReplaceChild("model", CubeListBuilder.create(), PartPose.offset(0.0F, -18.0F, 0.0F));
        PartDefinition body = model.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 31).addBox(-4.0F, -11.75F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(-0.25F))
                .texOffs(0, 48).addBox(-4.0F, -11.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 0.0F));
        body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(34, 34).addBox(-1.0086F, -0.8805F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(33, 0).addBox(-1.5086F, -1.1305F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.5F, -10.0F, 0.0F));
        PartDefinition arm_l = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(25, 34).addBox(-1.0F, -0.75F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(48, 0).addBox(-1.5F, -1.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(5.5F, -10.0F, 0.0F));
        arm_l.addOrReplaceChild("fireballbinding", CubeListBuilder.create().texOffs(6, 49).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.5F, 0.0F));
        body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 16).addBox(-3.5F, -7.5F, -3.5F, 7.0F, 7.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -11.0F, 0.0F));
        model.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(34, 49).addBox(-0.994F, 0.2314F, -1.2146F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(46, 17).addBox(-1.994F, -0.0186F, -2.2146F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.25F)), PartPose.offset(2.0F, 5.5F, 0.0F));
        model.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(25, 49).addBox(-1.0F, 0.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(29, 17).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(-0.25F)), PartPose.offset(-2.0F, 5.5F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(TorchRenderState state) {
        super.setupAnim(state);
        head.xRot = state.xRot * ((float) Math.PI / 180F);
        head.yRot = state.yRot * ((float) Math.PI / 180F);

        KeyframeAnimation currentAnimation = state.isSlicing ? slicingAnimationState : idleAnimationState;
        KeyframeAnimation lastAnimation = state.isSlicing ? idleAnimationState : slicingAnimationState;

        if (state.ticksSinceLastChange < TorchEntity.BLEND_TICKS) {
            Util.blendAnimations(
                    state.ticksSinceLastChange,
                    TorchEntity.BLEND_TICKS,
                    state.partialTick,
                    state.ageInTicks,
                    lastAnimation, state.lastAnimationState,
                    currentAnimation, state.currentAnimationState);
        } else {
            currentAnimation.apply(state.currentAnimationState, state.ageInTicks);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        poseStack.pushPose();
        poseStack.translate(0, 1.5, 0);
        model.render(poseStack, buffer, packedLight, packedOverlay, color);
        poseStack.popPose();
    }

    public ModelPart getModel() {
        return model;
    }

    public ModelPart getBody() {
        return body;
    }
}
