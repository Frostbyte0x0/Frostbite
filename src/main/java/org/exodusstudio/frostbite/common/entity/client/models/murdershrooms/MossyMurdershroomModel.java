package org.exodusstudio.frostbite.common.entity.client.models.murdershrooms;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class MossyMurdershroomModel extends EntityModel<LivingEntityRenderState> {
    private final ModelPart murdershroom;
    private final ModelPart head;

    public MossyMurdershroomModel(ModelPart root) {
        super(root);
        this.murdershroom = root.getChild("murdershroom");
        this.head = this.murdershroom.getChild("upper_body_group").getChild("body").getChild("head_group");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition murdershroom = partdefinition.addOrReplaceChild("murdershroom", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition upper_body_group = murdershroom.addOrReplaceChild("upper_body_group", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = upper_body_group.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, -7.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head_group = body.addOrReplaceChild("head_group", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition headmain = head_group.addOrReplaceChild("headmain", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -11.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition feet = murdershroom.addOrReplaceChild("feet", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right = feet.addOrReplaceChild("right", CubeListBuilder.create().texOffs(12, 14).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(20, 14).addBox(-1.0F, 4.0F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -5.0F, 0.0F));

        PartDefinition left = feet.addOrReplaceChild("left", CubeListBuilder.create().texOffs(20, 17).addBox(-1.0F, 4.0F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -5.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public void setupAnim(LivingEntityRenderState renderState) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        //this.applyHeadRotation(netHeadYaw, headPitch);

        //this.animateWalk(MossyMurdershroomAnimations.WALKING, limbSwing, limbSwingAmount, 2f, 2.5f);
        //this.animate(entity.idleAnimationState, MossyMurdershroomAnimations.IDLE, ageInTicks, 1f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        murdershroom.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}