package org.exodusstudio.frostbite.common.entity.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import org.exodusstudio.frostbite.Frostbite;
import org.exodusstudio.frostbite.common.entity.client.layers.ModModelLayers;
import org.exodusstudio.frostbite.common.entity.client.models.EtherealHammerModel;
import org.exodusstudio.frostbite.common.entity.client.states.EtherealAnimationState;
import org.exodusstudio.frostbite.common.entity.custom.misc.EtherealHammerEntity;

public class EtherealHammerRenderer extends EtherealRenderer<EtherealHammerEntity, EtherealHammerModel> {
    public EtherealHammerRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EtherealHammerModel createModel(EntityRendererProvider.Context context) {
        return new EtherealHammerModel(context.bakeLayer(ModModelLayers.ETHEREAL_HAMMER));
    }

    @Override
    public void extractRenderState(EtherealHammerEntity entity, EtherealAnimationState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.animationState.copyFrom(entity.animationState);
        reusedState.yRot = entity.getYRot(partialTick);
    }

    @Override
    public Identifier getTextureLocation() {
        return Identifier.fromNamespaceAndPath(Frostbite.MOD_ID, "textures/entity/ethereal_hammer/ethereal_hammer.png");
    }
}
