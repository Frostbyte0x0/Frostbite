package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.common.entity.client.states.TanukiRenderState;

public class TanukiModel extends QuadrupedModel<TanukiRenderState> {
    private final ModelPart all;

    public TanukiModel(ModelPart root) {
        super(root.getChild("all"));
        this.all = root.getChild("all");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild("all", CubeListBuilder.create(), PartPose.offset(0.0F, 18.0F, 0.0F));
        all.addOrReplaceChild("right_hind_leg", CubeListBuilder.create().texOffs(28, 27).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 3.0F, 6.0F));
        all.addOrReplaceChild("left_hind_leg", CubeListBuilder.create().texOffs(0, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 3.0F, 6.0F));
        all.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(8, 28).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 3.0F, -6.0F));
        all.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(20, 27).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 3.0F, -6.0F));
        all.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -3.0F, -7.0F, 10.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        all.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 20).addBox(-3.0F, -2.0F, -1.0F, 6.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 32).addBox(-2.99F, -3.5F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(30, 32).addBox(0.99F, -3.5F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 32).addBox(-1.0F, -0.01F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -9.0F));
        all.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(20, 20).addBox(-1.0F, -8.0F, 7.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(TanukiRenderState state) {
        super.setupAnim(state);

        if (state.sitAmount > 0.0F) {
            this.body.xRot = Mth.rotLerpRad(state.sitAmount, this.body.xRot, -1.7407963F * 0.1f);
            this.head.xRot = Mth.rotLerpRad(state.sitAmount, this.head.xRot, ((float)Math.PI / 2F));
            this.rightFrontLeg.zRot = -0.27079642F;
            this.leftFrontLeg.zRot = 0.27079642F;
            this.rightHindLeg.zRot = 0.5707964F;
            this.leftHindLeg.zRot = -0.5707964F;
            if (state.isEating) {
                this.head.xRot = ((float)Math.PI / 2F) + 0.2F * Mth.sin(state.ageInTicks * 0.6F);
                this.rightFrontLeg.xRot = -0.4F - 0.2F * Mth.sin(state.ageInTicks * 0.6F);
                this.leftFrontLeg.xRot = -0.4F - 0.2F * Mth.sin(state.ageInTicks * 0.6F);
            }
        } else {
            this.rightHindLeg.zRot = 0.0F;
            this.leftHindLeg.zRot = 0.0F;
            this.rightFrontLeg.zRot = 0.0F;
            this.leftFrontLeg.zRot = 0.0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        all.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
