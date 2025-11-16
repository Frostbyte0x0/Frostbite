package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.BorealBearModel;
import org.exodusstudio.frostbite.common.entity.client.states.BorealBearRenderState;
import org.exodusstudio.frostbite.common.entity.custom.BorealBearEntity;

public class BorealBearRenderer extends MobRenderer<BorealBearEntity, BorealBearRenderState, BorealBearModel> {
    public BorealBearRenderer(EntityRendererProvider.Context context) {
        super(context, new BorealBearModel(context.bakeLayer(ModModelLayers.BOREAL_BEAR)), 0.45f);
    }

    @Override
    public BorealBearRenderState createRenderState() {
        return new BorealBearRenderState();
    }

    @Override
    public void extractRenderState(BorealBearEntity bear, BorealBearRenderState state, float partialTick) {
        super.extractRenderState(bear, state, partialTick);
        state.isAttacking = bear.isAttacking();
        state.attackAnimationState.copyFrom(bear.attackingAnimationState);
    }

    @Override
    public ResourceLocation getTextureLocation(BorealBearRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/boreal_bear/boreal_bear.png");
    }
}
