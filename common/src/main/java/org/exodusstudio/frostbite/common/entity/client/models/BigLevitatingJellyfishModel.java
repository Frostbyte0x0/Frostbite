package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.animations.BigLevitatingJellyfishAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.LevitatingJellyfishRenderState;

public class BigLevitatingJellyfishModel extends EntityModel<LevitatingJellyfishRenderState> {
    private final ModelPart bigjellyfish;
    private final KeyframeAnimation idleAnimation;

    public BigLevitatingJellyfishModel(ModelPart root) {
        super(root);
        this.bigjellyfish = root.getChild("bigjellyfish");
        this.idleAnimation = BigLevitatingJellyfishAnimations.IDLE.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bigjellyfish = partdefinition.addOrReplaceChild("bigjellyfish", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition model = bigjellyfish.addOrReplaceChild("model", CubeListBuilder.create(), PartPose.offset(0.0F, -14.0F, 0.0F));
        PartDefinition head = model.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, -1.0F, 0.0F));
        PartDefinition core = head.addOrReplaceChild("core", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -3.5F, -7.0F, 14.0F, 9.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 0.0F));
        PartDefinition tail = core.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 5.5F, 0.0F));
        tail.addOrReplaceChild("tail1b_r1", CubeListBuilder.create().texOffs(0, 50).addBox(-0.75F, -20.0F, 0.0F, 0.0F, 16.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5F, 20.0F, -6.5F, 0.0F, -0.7854F, 0.0F));
        tail.addOrReplaceChild("tail1a_r1", CubeListBuilder.create().texOffs(0, 70).addBox(-12.0F, -20.0F, 0.0F, 20.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 20.0F, 1.5F, 0.0F, -0.7854F, 0.0F));
        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create(), PartPose.offset(0.0F, 15.5F, 0.0F));
        tail2.addOrReplaceChild("tail2b_r1", CubeListBuilder.create().texOffs(0, 67).addBox(-0.75F, -20.0F, 0.0F, 0.0F, 16.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.5F, 19.5F, -6.5F, 0.0F, -0.7854F, 0.0F));
        tail2.addOrReplaceChild("tail2a_r1", CubeListBuilder.create().texOffs(0, 87).addBox(-12.0F, -20.0F, 0.0F, 20.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 19.5F, 1.5F, 0.0F, -0.7854F, 0.0F));
        PartDefinition cap = head.addOrReplaceChild("cap", CubeListBuilder.create().texOffs(0, 24).addBox(-8.0F, -6.0F, -8.0F, 16.0F, 3.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 44).addBox(-10.0F, -3.0F, -10.0F, 20.0F, 5.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));
        cap.addOrReplaceChild("tendrils", CubeListBuilder.create().texOffs(49, 6).addBox(-9.0F, -0.5F, -9.0F, 18.0F, 10.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));
        PartDefinition tentacle_n = cap.addOrReplaceChild("tentacle_n", CubeListBuilder.create().texOffs(41, 70).addBox(-0.5F, 0.0F, -0.25F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -9.25F));
        PartDefinition tentacle_n2 = tentacle_n.addOrReplaceChild("tentacle_n2", CubeListBuilder.create().texOffs(41, 88).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.5F, 0.25F));
        tentacle_n2.addOrReplaceChild("tentacle_n3", CubeListBuilder.create().texOffs(41, 106).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));
        PartDefinition tentacle_s = cap.addOrReplaceChild("tentacle_s", CubeListBuilder.create().texOffs(41, 70).addBox(-0.5F, 0.0F, -0.25F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 8.75F));
        PartDefinition tentacle_s2 = tentacle_s.addOrReplaceChild("tentacle_s2", CubeListBuilder.create().texOffs(41, 88).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.5F, 0.25F));
        tentacle_s2.addOrReplaceChild("tentacle_s3", CubeListBuilder.create().texOffs(41, 106).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));
        PartDefinition tentacle_w = cap.addOrReplaceChild("tentacle_w", CubeListBuilder.create().texOffs(41, 70).addBox(-0.5F, 0.0F, -0.25F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 2.0F, -0.25F));
        PartDefinition tentacle_w2 = tentacle_w.addOrReplaceChild("tentacle_w2", CubeListBuilder.create().texOffs(41, 88).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.5F, 0.25F));
        tentacle_w2.addOrReplaceChild("tentacle_w3", CubeListBuilder.create().texOffs(41, 106).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));
        PartDefinition tentacle_e = cap.addOrReplaceChild("tentacle_e", CubeListBuilder.create().texOffs(41, 70).addBox(-0.5F, 0.0F, -0.25F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 2.0F, -0.25F));
        PartDefinition tentacle_e2 = tentacle_e.addOrReplaceChild("tentacle_e2", CubeListBuilder.create().texOffs(41, 88).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.5F, 0.25F));
        tentacle_e2.addOrReplaceChild("tentacle_e3", CubeListBuilder.create().texOffs(41, 106).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));
        PartDefinition tentacle_nw = cap.addOrReplaceChild("tentacle_nw", CubeListBuilder.create().texOffs(41, 70).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 2.0F, -9.0F));
        PartDefinition tentacle_nw2 = tentacle_nw.addOrReplaceChild("tentacle_nw2", CubeListBuilder.create().texOffs(41, 88).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.5F, 0.0F));
        tentacle_nw2.addOrReplaceChild("tentacle_nw3", CubeListBuilder.create().texOffs(41, 106).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));
        PartDefinition tentacle_ne = cap.addOrReplaceChild("tentacle_ne", CubeListBuilder.create().texOffs(41, 70).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 2.0F, -9.0F));
        PartDefinition tentacle_ne2 = tentacle_ne.addOrReplaceChild("tentacle_ne2", CubeListBuilder.create().texOffs(41, 88).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.5F, 0.0F));
        tentacle_ne2.addOrReplaceChild("tentacle_ne3", CubeListBuilder.create().texOffs(41, 106).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));
        PartDefinition tentacle_sw = cap.addOrReplaceChild("tentacle_sw", CubeListBuilder.create().texOffs(41, 70).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 2.0F, 9.0F));
        PartDefinition tentacle_sw2 = tentacle_sw.addOrReplaceChild("tentacle_sw2", CubeListBuilder.create().texOffs(41, 88).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.5F, 0.0F));
        tentacle_sw2.addOrReplaceChild("tentacle_sw3", CubeListBuilder.create().texOffs(41, 106).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));
        PartDefinition tentacle_se = cap.addOrReplaceChild("tentacle_se", CubeListBuilder.create().texOffs(41, 70).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 14.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-9.0F, 2.0F, 9.0F));
        PartDefinition tentacle_se2 = tentacle_se.addOrReplaceChild("tentacle_se2", CubeListBuilder.create().texOffs(41, 88).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.5F, 0.0F));
        tentacle_se2.addOrReplaceChild("tentacle_se3", CubeListBuilder.create().texOffs(41, 106).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 15.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(LevitatingJellyfishRenderState renderState) {
        super.setupAnim(renderState);
        idleAnimation.apply(renderState.idleAnimationState, renderState.ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        poseStack.translate(0, -1.5, 0);
        bigjellyfish.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
