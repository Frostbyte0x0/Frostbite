package org.exodusstudio.frostbite.common.block.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.block_entities.LodestarBlockEntity;
import org.exodusstudio.frostbite.common.block.states.LodestarRenderState;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

public class LodestarRenderer implements BlockEntityRenderer<LodestarBlockEntity, LodestarRenderState> {
    public static final Identifier BEAM_LOCATION = Identifier.withDefaultNamespace("textures/entity/beacon_beam.png");
    public static final Material CAGE_TEXTURE;
    public static final Material EYE_TEXTURE;
    private final ModelPart eye;
    private final ModelPart cage;
    private final MaterialSet materials;

    public LodestarRenderer(BlockEntityRendererProvider.Context context) {
        this.materials = context.materials();
        this.eye = context.bakeLayer(ModModelLayers.LODESTAR_EYE);
        this.cage = context.bakeLayer(ModModelLayers.LODESTAR_CAGE);
    }

    public static LayerDefinition createEyeLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    public static LayerDefinition createCageLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("cage", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 32, 16);
    }

    public static LayerDefinition createShellLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(-16, -16)
                .addBox(0, 0, 0, 16.0F, 16.0F, 16.0F), PartPose.ZERO);
        return LayerDefinition.create(meshdefinition, 16, 16);
    }

    @Override
    public LodestarRenderState createRenderState() {
        return new LodestarRenderState();
    }

    @Override
    public void extractRenderState(LodestarBlockEntity entity, LodestarRenderState state, float partialTick, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(entity, state, breakProgress);
        state.activeRotation = entity.getActiveRotation(partialTick);
        state.animTime = (float)entity.tickCount + partialTick;
        state.animationPhase = entity.tickCount / 66 % 3;
    }

    @Override
    public void submit(LodestarRenderState state, PoseStack stack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        float f = state.activeRotation * (180F / (float)Math.PI);
        float f1 = Mth.sin((state.animTime * 0.1F)) / 2;
        f1 = f1 * f1 + f1;

        // Cage
        stack.pushPose();
        stack.translate(0.5, 0.45 + f1 * 0.2, 0.5);
        Vector3f vector3f = (new Vector3f(0.5F, 1.0F, 0.5F)).normalize();
        stack.mulPose((new Quaternionf()).rotationAxis(f * ((float)Math.PI / 180F), vector3f));
        submitNodeCollector.submitModelPart(this.cage, stack, CAGE_TEXTURE.renderType(RenderTypes::entityCutoutNoCull), state.lightCoords, OverlayTexture.NO_OVERLAY, this.materials.get(CAGE_TEXTURE), -1, state.breakProgress);
        stack.popPose();

        // Eye
        stack.pushPose();
        stack.translate(0.5, 0.45 + f1 * 0.2, 0.5);
        stack.scale(0.5F, 0.5F, 0.5F);
        stack.mulPose(cameraRenderState.orientation);
        stack.mulPose((new Quaternionf()).rotationZ((float)Math.PI).rotateY((float)Math.PI));
        stack.scale(1.3333334F, 1.3333334F, 1.3333334F);
        submitNodeCollector.submitModelPart(this.eye, stack, EYE_TEXTURE.renderType(RenderTypes::entityCutoutNoCull), state.lightCoords, OverlayTexture.NO_OVERLAY, this.materials.get(EYE_TEXTURE));
        stack.popPose();

        float f2 = Mth.sin(state.animTime * 0.1F) / 2.0F + 0.5F;
        f2 = f2 * f2 + f2;

        // Beam
        int colour = ARGB.color(0, 100, (int) Mth.lerp(f2 / 2, 150, 200));
        for (int i = 1; i < 318; i++) {
            BeaconRenderer.submitBeaconBeam(stack, submitNodeCollector, BEAM_LOCATION, 1, state.animTime,
                    i, 1, colour, 0.2f, 0.25f);
        }

//        submitBeaconBeam(poseStack, nodeCollector, BEAM_LOCATION, 1.0F, state.animTime, yOffset, height,
//                color, 0.2F * radiusScale, 0.25F * radiusScale);
    }

    public AABB getRenderBoundingBox(LodestarBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, 1024, pos.getZ() + 1);
    }

    @Override
    public boolean shouldRender(LodestarBlockEntity blockEntity, Vec3 cameraPos) {
        return true;
    }

    static {
        CAGE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "block/lodestar/cage"));
        EYE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "block/lodestar/eye"));
    }
}
