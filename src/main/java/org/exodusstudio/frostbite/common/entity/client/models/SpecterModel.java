package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.states.SpecterRenderState;

public class SpecterModel extends EntityModel<SpecterRenderState> {
    private final ModelPart Torso;
    private final ModelPart Toga;

    public SpecterModel(ModelPart root) {
        super(root);
        this.Torso = root.getChild("Torso");
        this.Toga = root.getChild("Toga");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Torso = partdefinition.addOrReplaceChild("Torso", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, -2.0F));
        Torso.addOrReplaceChild("Toga top_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -2.5F, -4.5F, 18.0F, 20.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, -10.0F, 1.5F, 0.5236F, 0.0F, 0.0F));
        Torso.addOrReplaceChild("Hunch", CubeListBuilder.create(), PartPose.offset(-2.5F, -7.5F, 5.0F));
        Torso.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(56, 0).addBox(-5.5F, -13.0F, 0.0F, 13.0F, 13.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.5F, -12.5F, -5.0F));
        PartDefinition Arms = Torso.addOrReplaceChild("Arms", CubeListBuilder.create(), PartPose.offset(0.0F, -12.5F, 0.0F));
        Arms.addOrReplaceChild("Left Arm", CubeListBuilder.create().texOffs(56, 23).addBox(-2.4F, 0.0F, -2.5F, 5.0F, 18.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, 0.0F, 0.0F));
        Arms.addOrReplaceChild("Right Arm", CubeListBuilder.create().texOffs(56, 46).addBox(-3.1F, 0.0F, -2.5F, 5.0F, 18.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.5F, 0.0F, 0.0F));
        PartDefinition Toga = partdefinition.addOrReplaceChild("Toga", CubeListBuilder.create(), PartPose.offset(0.0F, 6.5F, 3.0F));
        Toga.addOrReplaceChild("Toga bottom_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-8.0F, -2.6F, -6.5F, 18.0F, 18.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, 0.5F, 7.5F, 1.0472F, 0.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        Torso.render(poseStack, buffer, packedLight, packedOverlay, color);
        Toga.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
