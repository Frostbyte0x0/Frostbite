package org.exodusstudio.frostbite.common.entity.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.CurseBallModel;
import org.exodusstudio.frostbite.common.entity.client.states.CurseBallRenderState;
import org.exodusstudio.frostbite.common.entity.custom.misc.CurseBallEntity;
import org.exodusstudio.frostbite.common.util.Util;

public class CurseBallRenderer extends EntityRenderer<CurseBallEntity, CurseBallRenderState> {
    private final CurseBallModel model;

    public CurseBallRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new CurseBallModel(context.bakeLayer(ModModelLayers.CURSE_BALL));
    }

    @Override
    public void submit(CurseBallRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
        nodeCollector.submitModel(model, renderState, poseStack, RenderTypes.entityTranslucent(getTextureLocation(renderState)),
                renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, renderState.outlineColor, null);
    }

    @Override
    public void extractRenderState(CurseBallEntity entity, CurseBallRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);

        if (entity.getCursedEntity() == null) {
            reusedState.xRot = entity.getXRot() * ((float)Math.PI / 180F);
            reusedState.yRot = entity.getYRot() * ((float)Math.PI / 180F);
        } else {
            float[] angles = Util.getXYRot(entity.getCursedEntity().getPosition(partialTick).subtract(entity.getPosition(partialTick)).add(0, 1, 0));
            reusedState.xRot = angles[0] * ((float)Math.PI / 180F);
            reusedState.yRot = angles[1] * ((float)Math.PI / 180F);
            entity.setXRot(reusedState.xRot / ((float)Math.PI / 180F));
            entity.setYRot(reusedState.yRot / ((float)Math.PI / 180F));
        }

        Vec3 pos = entity.getPosition(partialTick);
        reusedState.x = pos.x;
        reusedState.y = pos.y;
        reusedState.z = pos.z;

        reusedState.curse = entity.getCurse();
    }

    @Override
    public boolean shouldRender(CurseBallEntity livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    protected boolean affectedByCulling(CurseBallEntity display) {
        return false;
    }

    @Override
    public CurseBallRenderState createRenderState() {
        return new CurseBallRenderState();
    }

    public Identifier getTextureLocation(CurseBallRenderState renderState) {
        return switch (renderState.curse) {
            case "leech" ->
                    Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/curse_ball/curse_ball_leech.png");
            case "static" ->
                    Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/curse_ball/curse_ball_static.png");
            case "perpetual" ->
                    Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/curse_ball/curse_ball_perpetual.png");
            default -> Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/curse_ball/curse_ball.png");
        };
    }
}
