package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.animations.EtherealAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.EtherealRenderState;

public class EtherealHandsModel extends EntityModel<EtherealRenderState> {
    private final ModelPart hand2;
    private final ModelPart hand3;
    private final KeyframeAnimation clappingAnimation;
    private float yRot;

    public EtherealHandsModel(ModelPart root) {
        super(root);
        this.hand2 = root.getChild("hand2");
        this.hand3 = root.getChild("hand3");
        this.clappingAnimation = EtherealAnimations.HANDS_CLAP.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("hand2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -6.0868F, -21.648F, 2.0F, 11.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 12.0F, 0.6109F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("hand3", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -6.0868F, -21.648F, 2.0F, 11.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 4.0F, 12.0F, 0.6109F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(EtherealRenderState renderState) {
        super.setupAnim(renderState);
        yRot = 180 - renderState.yRot;
        hand2.zRot += (float) Math.PI;
        hand2.zRot += (float) Math.PI;
        clappingAnimation.apply(renderState.animationState, renderState.ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        hand2.render(poseStack, buffer, packedLight, packedOverlay, color);
        hand3.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
