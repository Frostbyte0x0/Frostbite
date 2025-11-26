package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.animations.LevitatingJellyfishAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.LevitatingJellyfishRenderState;

public class LevitatingJellyfishModel extends EntityModel<LevitatingJellyfishRenderState> {
    private final ModelPart Model;
    private final KeyframeAnimation idleAnimation;

    public LevitatingJellyfishModel(ModelPart root) {
        super(root);
        this.Model = root.getChild("Model");
        this.idleAnimation = LevitatingJellyfishAnimations.IDLE.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Model = partdefinition.addOrReplaceChild("Model", CubeListBuilder.create(), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition Cap = Model.addOrReplaceChild("Cap", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -2.0F, -5.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        Cap.addOrReplaceChild("Tendrils", CubeListBuilder.create().texOffs(17, 34).addBox(-4.0F, 1.0F, -4.0F, 8.0F, 5.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        Cap.addOrReplaceChild("Cap_Top", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, -26.0F, -4.0F, 8.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 0.0F));
        PartDefinition Core = Cap.addOrReplaceChild("Core", CubeListBuilder.create().texOffs(0, 25).addBox(-3.0F, -2.0F, -3.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));
        PartDefinition Core_tail1 = Core.addOrReplaceChild("Core_tail1", CubeListBuilder.create().texOffs(0, 47).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.5F, 0.0F));
        Core_tail1.addOrReplaceChild("Core_tail2", CubeListBuilder.create().texOffs(13, 48).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.5F, 0.0F));
        PartDefinition Antenna_fl = Cap.addOrReplaceChild("Antenna_fl", CubeListBuilder.create().texOffs(30, 25).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.5F, -4.0F));
        PartDefinition Antenna_fl2 = Antenna_fl.addOrReplaceChild("Antenna_fl2", CubeListBuilder.create(), PartPose.offset(0.0F, 6.25F, 0.0F));
        Antenna_fl2.addOrReplaceChild("a_fl2_r1", CubeListBuilder.create().texOffs(25, 25).addBox(-0.5F, -0.25F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition Antenna_br = Cap.addOrReplaceChild("Antenna_br", CubeListBuilder.create().texOffs(30, 25).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.5F, 4.0F));
        PartDefinition Antenna_br2 = Antenna_br.addOrReplaceChild("Antenna_br2", CubeListBuilder.create(), PartPose.offset(0.0F, 6.25F, 0.0F));
        Antenna_br2.addOrReplaceChild("a_br2_r1", CubeListBuilder.create().texOffs(25, 25).addBox(-0.5F, -0.25F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, -1.5708F, 0.0F));
        PartDefinition Antenna_fr = Cap.addOrReplaceChild("Antenna_fr", CubeListBuilder.create().texOffs(30, 25).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.5F, -4.0F));
        Antenna_fr.addOrReplaceChild("Antenna_fr2", CubeListBuilder.create().texOffs(25, 25).addBox(-0.5F, 0.75F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.25F, 0.0F));
        PartDefinition Antenna_bl = Cap.addOrReplaceChild("Antenna_bl", CubeListBuilder.create().texOffs(30, 25).addBox(-0.5F, 0.5F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.5F, 4.0F));
        Antenna_bl.addOrReplaceChild("Antenna_bl1", CubeListBuilder.create().texOffs(25, 25).addBox(-0.5F, 0.75F, -0.5F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.25F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(LevitatingJellyfishRenderState renderState) {
        super.setupAnim(renderState);
        idleAnimation.apply(renderState.idleAnimationState, renderState.ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        poseStack.translate(0, 0.6, 0);
        Model.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
