package com.frostbyte.frostbite.entity.client;

import com.frostbyte.frostbite.entity.custom.MurdershroomEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class MurdershroomModel<T extends MurdershroomEntity> extends EntityModel<T> {
    private final ModelPart murdershroom;
    private final ModelPart upper_body_group;
    private final ModelPart body;
    private final ModelPart head_group;
    private final ModelPart headmain;
    private final ModelPart feet;
    private final ModelPart right;
    private final ModelPart left;

    public MurdershroomModel(ModelPart root) {
        this.murdershroom = root.getChild("murdershroom");
        this.upper_body_group = this.murdershroom.getChild("upper_body_group");
        this.body = this.upper_body_group.getChild("body");
        this.head_group = this.body.getChild("head_group");
        this.headmain = this.head_group.getChild("headmain");
        this.feet = this.murdershroom.getChild("feet");
        this.right = this.feet.getChild("right");
        this.left = this.feet.getChild("left");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition murdershroom = partdefinition.addOrReplaceChild("murdershroom", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition upper_body_group = murdershroom.addOrReplaceChild("upper_body_group", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body = upper_body_group.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-2.0F, -7.0F, -1.0F, 4.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head_group = body.addOrReplaceChild("head_group", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition headmain = head_group.addOrReplaceChild("headmain", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -11.0F, -5.0F, 10.0F, 4.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition feet = murdershroom.addOrReplaceChild("feet", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right = feet.addOrReplaceChild("right", CubeListBuilder.create().texOffs(12, 14).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(20, 14).addBox(-1.0F, 4.0F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -5.0F, 0.0F));

        PartDefinition left = feet.addOrReplaceChild("left", CubeListBuilder.create().texOffs(20, 17).addBox(-1.0F, 4.0F, -3.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 18).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.0F, -5.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        murdershroom.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
