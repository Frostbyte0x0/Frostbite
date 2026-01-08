package org.exodusstudio.frostbite.common.entity.client.models.bullet;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class RevolverBulletModel extends EntityModel<ArrowRenderState> {
    public RevolverBulletModel(ModelPart root) {
        super(root, RenderTypes::entityCutout);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-8, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -22.0F, 0.0F, 1.5708F, 0.0F, 2.3562F));
        bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-8, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -22.0F, 0.0F, -3.1416F, 0.7854F, 3.1416F));
        return LayerDefinition.create(meshdefinition, 8, 8);
    }
}
