package org.exodusstudio.frostbite.common.entity.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.animations.LevitatingJellyfishAnimations;
import org.exodusstudio.frostbite.common.entity.client.states.LevitatingJellyfishState;

public class LevitatingJellyfishModel extends EntityModel<LevitatingJellyfishState> {
    private final ModelPart head;
    private final ModelPart tentacles;
    private final ModelPart tentacle1;
    private final ModelPart tentacle2;
    private final ModelPart tentacle3;
    private final ModelPart tentacle4;

    public LevitatingJellyfishModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.tentacles = root.getChild("tentacles");
        this.tentacle1 = this.tentacles.getChild("tentacle1");
        this.tentacle2 = this.tentacles.getChild("tentacle2");
        this.tentacle3 = this.tentacles.getChild("tentacle3");
        this.tentacle4 = this.tentacles.getChild("tentacle4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -9.0F, -1.0F, 12.0F, 7.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 19.0F, -5.0F));

        PartDefinition tentacles = partdefinition.addOrReplaceChild("tentacles", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition tentacle1 = tentacles.addOrReplaceChild("tentacle1", CubeListBuilder.create().texOffs(0, 19).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -7.0F, -3.0F));

        PartDefinition tentacle2 = tentacles.addOrReplaceChild("tentacle2", CubeListBuilder.create().texOffs(24, 19).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -7.0F, -3.0F));

        PartDefinition tentacle3 = tentacles.addOrReplaceChild("tentacle3", CubeListBuilder.create().texOffs(16, 19).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -7.0F, 3.0F));

        PartDefinition tentacle4 = tentacles.addOrReplaceChild("tentacle4", CubeListBuilder.create().texOffs(8, 19).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -7.0F, 3.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(LevitatingJellyfishState renderState) {
        super.setupAnim(renderState);
        this.animate(renderState.swimmingAnimationState, LevitatingJellyfishAnimations.SWIM, renderState.ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
