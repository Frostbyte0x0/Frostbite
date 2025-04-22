package org.exodusstudio.frostbite.common.entity.client.models.bullet;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;

public class SniperBulletModel extends EntityModel<ArrowRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "sniper_bullet_model"), "main");
    private final ModelPart bb_main;

    public SniperBulletModel(ModelPart root) {
        super(root, RenderType::entityCutout);
        this.bb_main = root.getChild("bb_main");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition cube_r1 = bb_main.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(-8, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -22.0F, 0.0F, 1.5708F, 0.0F, 2.3562F));

        PartDefinition cube_r2 = bb_main.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(-8, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 0.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -22.0F, 0.0F, -3.1416F, 0.7854F, 3.1416F));

        return LayerDefinition.create(meshdefinition, 8, 8);
    }
}
