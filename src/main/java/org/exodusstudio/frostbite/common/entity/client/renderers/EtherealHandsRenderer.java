package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.EtherealHandsModel;
import org.exodusstudio.frostbite.common.entity.client.states.EtherealAnimationState;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealHammerEntity;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealHandsEntity;

public class EtherealHandsRenderer extends EtherealRenderer<EtherealHandsEntity, EtherealHandsModel> {
    public EtherealHandsRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EtherealHandsModel createModel(EntityRendererProvider.Context context) {
        return new EtherealHandsModel(context.bakeLayer(ModModelLayers.ETHEREAL_HANDS));
    }

    @Override
    public void extractRenderState(EtherealHandsEntity entity, EtherealAnimationState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.animationState.copyFrom(entity.animationState);
        reusedState.yRot = entity.getYRot(partialTick);
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return ResourceLocation.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/ethereal_hands/ethereal_hands.png");
    }
}
