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

public class EtherealHammerModel extends EntityModel<EtherealRenderState> {
    private final ModelPart hammer;
    private final KeyframeAnimation hitAnimation;
    private float yRot;

    public EtherealHammerModel(ModelPart root) {
        super(root);
        this.hammer = root.getChild("hammer");
        this.hitAnimation = EtherealAnimations.HAMMER_HIT.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("hammer", CubeListBuilder.create().texOffs(0, 22).addBox(-1.0F, -13.0F, -1.0F, 2.0F, 13.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-4.0F, -21.0F, -7.0F, 8.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(EtherealRenderState renderState) {
        super.setupAnim(renderState);
        yRot = 180 - renderState.yRot;
        hammer.zRot += (float) Math.PI;
        hitAnimation.apply(renderState.animationState, renderState.ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
        hammer.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
