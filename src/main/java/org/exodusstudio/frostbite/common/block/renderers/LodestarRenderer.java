package org.exodusstudio.frostbite.common.block.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.block.block_entities.LodestarBlockEntity;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class LodestarRenderer implements BlockEntityRenderer<LodestarBlockEntity> {
    public static final ResourceLocation BEAM_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/beacon_beam.png");
    public static final Material SHELL_TEXTURE;
    public static final Material CAGE_TEXTURE;
    public static final Material EYE_TEXTURE;
    private final ModelPart eye;
    private final ModelPart cage;
    private final ModelPart shell;
    private final BlockEntityRenderDispatcher renderer;

    public LodestarRenderer(BlockEntityRendererProvider.Context context) {
        this.renderer = context.getBlockEntityRenderDispatcher();
        this.eye = context.bakeLayer(ModModelLayers.LODESTAR_EYE);
        this.cage = context.bakeLayer(ModModelLayers.LODESTAR_CAGE);
        this.shell = context.bakeLayer(ModModelLayers.LODESTAR_SHELL);
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
    public void render(LodestarBlockEntity lodestar, float partialTick, PoseStack pose, MultiBufferSource bufferSource, int i1, int i2) {
        float f = (float) lodestar.tickCount + partialTick;
        float f1 = lodestar.getActiveRotation(partialTick) * (180F / (float) Math.PI);
        float f2 = Mth.sin(f * 0.1F) / 2.0F + 0.5F;
        f2 = f2 * f2 + f2;

        // Cage
        pose.pushPose();
        pose.translate(0.5F, 0.3F + f2 * 0.15F, 0.5F);
        Vector3f vector3f = (new Vector3f(0.5F, 1.0F, 0.5F)).normalize();
        pose.mulPose((new Quaternionf()).rotationAxis(f1 * ((float) Math.PI / 180F), vector3f));
        this.cage.render(pose, CAGE_TEXTURE.buffer(bufferSource, RenderType::entityCutoutNoCull), i1, i2);
        pose.popPose();

        // Eye
        Camera camera = this.renderer.camera;
        pose.pushPose();
        pose.translate(0.5F, 0.3F + f2 * 0.15F, 0.5F);
        pose.scale(0.5F, 0.5F, 0.5F);
        float f3 = -camera.getYRot();
        pose.mulPose((new Quaternionf()).rotationYXZ(f3 * ((float) Math.PI / 180F), camera.getXRot() * ((float) Math.PI / 180F), (float) Math.PI));
        pose.scale(1.3333334F, 1.3333334F, 1.3333334F);
        this.eye.render(pose, EYE_TEXTURE.buffer(bufferSource, RenderType::entityCutoutNoCull), i1, i2);
        pose.popPose();

        // Beam
        int colour = ARGB.color(0, 100, (int) Mth.lerp(f2 / 2, 150, 200));
        BeaconRenderer.renderBeaconBeam(pose, bufferSource, BEAM_LOCATION, partialTick, 0.3f,
                lodestar.getLevel().getGameTime(), 1, 320, colour, 0.2f, 0.25f);
    }

    public AABB getRenderBoundingBox(LodestarBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), (double)pos.getX() + (double)1.0F, 1024.0F, pos.getZ() + 1.0F);
    }

    @Override
    public boolean shouldRender(LodestarBlockEntity blockEntity, Vec3 cameraPos) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(LodestarBlockEntity blockEntity) {
        return true;
    }

    static {
        SHELL_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "block/lodestar/shell"));
        CAGE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "block/lodestar/cage"));
        EYE_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "block/lodestar/eye"));
    }
}
