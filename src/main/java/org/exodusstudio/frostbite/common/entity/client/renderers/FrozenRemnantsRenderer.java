package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.FrozenRemnantsModel;
import org.exodusstudio.frostbite.common.entity.client.states.FrozenRemnantsRenderState;
import org.exodusstudio.frostbite.common.entity.custom.misc.FrozenRemnantsEntity;

public class FrozenRemnantsRenderer extends LivingEntityRenderer<FrozenRemnantsEntity, FrozenRemnantsRenderState, FrozenRemnantsModel> {
    public FrozenRemnantsRenderer(EntityRendererProvider.Context context) {
        super(context, new FrozenRemnantsModel(context.bakeLayer(ModModelLayers.FROZEN_REMNANTS)), 0.45f);
    }

    @Override
    public FrozenRemnantsRenderState createRenderState() {
        return new FrozenRemnantsRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(FrozenRemnantsRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/frozen_remnants/frozen_remnants.png");
    }

    @Override
    public void extractRenderState(FrozenRemnantsEntity frozenRemnants, FrozenRemnantsRenderState renderState, float p_361157_) {
        super.extractRenderState(frozenRemnants, renderState, p_361157_);
        Frustum frustum = Minecraft.getInstance().levelRenderer.getFrustum();
        renderState.isOnScreen = shouldRender(frozenRemnants, frustum, frustum.getCamX(), frustum.getCamY(), frustum.getCamZ());
        frozenRemnants.setOnScreen(renderState.isOnScreen);
        renderState.headPitch = frozenRemnants.getHeadPitch();
        renderState.bodyRot = frozenRemnants.getYRot();
    }
}
