package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.common.entity.client.states.FireSliceRenderState;

public class FireSliceModel extends EntityModel<FireSliceRenderState> {
    private final ModelPart bb_main;

    public FireSliceModel(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -1.0F, -4.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(10, 6).addBox(2.0F, -1.0F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(6, 12).addBox(1.0F, -1.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 12).addBox(4.0F, -1.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 12).addBox(3.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 3).addBox(4.0F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(5.0F, -1.0F, 1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 14).addBox(6.0F, -1.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(4, 14).addBox(-2.0F, -1.0F, -2.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(10, 9).addBox(-4.0F, -1.0F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 0).addBox(-6.0F, -1.0F, -2.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(8, 14).addBox(-4.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 9).addBox(-7.0F, -1.0F, -1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(10, 3).addBox(-8.0F, -1.0F, 1.0F, 3.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(12, 14).addBox(-7.0F, -1.0F, 3.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        bb_main.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
