package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.exodusstudio.frostbite.common.entity.client.states.IceSpikeRenderState;

public class IceSpikeModel extends EntityModel<IceSpikeRenderState> {
    private final ModelPart bb_main;

    public IceSpikeModel(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-32, 0).addBox(-8.2426F, 0.0F, -8.0F, 16.0F, 0.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, -1.5708F, -2.3562F, 3.1416F));
        bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-32, 0).addBox(-4.0F, 0.0F, -8.0F, 16.0F, 0.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -8.0F, -3.0F, -1.5708F, -0.7854F, -3.1416F));

        return LayerDefinition.create(meshdefinition, 16, 32);
    }

    public void setupAnim(IceSpikeRenderState state) {
        super.setupAnim(state);
        float f = state.biteProgress;
        if (state.isRising) {
            this.bb_main.y = ((10.5f + Math.max(-Math.max(Mth.abs(1 / (f - 0.6f)), -4), -4f)) * 5);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        bb_main.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
