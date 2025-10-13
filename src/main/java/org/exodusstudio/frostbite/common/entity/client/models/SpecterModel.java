package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.states.SpecterRenderState;

public class SpecterModel extends EntityModel<SpecterRenderState> {
    private final ModelPart bb_main;

    public SpecterModel(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -31.0F, -4.0F, 16.0F, 20.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(0, 48).addBox(-7.0F, -41.0F, -4.0F, 14.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(48, 23).addBox(3.0F, -18.0F, -4.0F, 5.0F, 18.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(48, 0).addBox(-18.0F, -18.0F, -4.0F, 5.0F, 18.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.0F, -24.0F, -16.0F, -1.5708F, 0.0F, 0.0F));

        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 28).addBox(-7.99F, -16.0F, -4.0F, 16.0F, 12.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -1.0F, 6.0F, 0.3927F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        bb_main.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
